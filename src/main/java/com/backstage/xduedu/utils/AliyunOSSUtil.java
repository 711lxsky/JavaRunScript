package com.backstage.xduedu.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.*;
import com.backstage.xduedu.Exception.HttpException;
import com.backstage.xduedu.config.AliyunOSSConfig;
import com.backstage.xduedu.setting_enum.ExceptionConstant;
import com.backstage.xduedu.setting_enum.StringConstant;
import com.backstage.xduedu.setting_enum.WebSetting;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @Author: 711lxsky
 * @Date: 2024-02-21
 */

@Component
@Log4j2
public class AliyunOSSUtil {

    @Resource
    private AliyunOSSConfig aliyunOSSConfig;

    @Resource
    ThreadPoolTaskExecutor taskExecutor;

    @Resource
    private FileUtil fileUtil;

    public String getPrefixUrl(){
        return WebSetting.getHttps()
                + aliyunOSSConfig.getBucket()
                + StringConstant.Dot.getValue()
                + aliyunOSSConfig.getEndpoint()
                + StringConstant.Separator.getValue();
    }

    public String saveDateToUploadFile(File file) throws HttpException{
        return this.uploadDateToOSSForFragment(file, this.getPrefixUrl() ,aliyunOSSConfig.getUploadFilePath());
    }

    public String saveDateToAnalysisFile(File file) throws HttpException{
        return this.uploadDateToOSSForFragment(file, this.getPrefixUrl() ,aliyunOSSConfig.getAnalysisFilePath());
    }

    public String uploadDateToOSSForFragment(File file, String prefixUrl, String fileDir) throws HttpException{
        OSS ossClient = aliyunOSSConfig.getOSSClient();
        try {
            // 此处需要分开进行文件流的读取，分别对流文件类型判断和分片上传，因为同时进行可能对concurrent异步调度造成阻塞
            String fileOriginName = file.getName();
            String fileFullName = fileDir + fileOriginName;
            String bucket = aliyunOSSConfig.getBucket();
            // 创建InitiateMultipartUploadRequest对象。
            InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucket, fileFullName);
            // 初始化分片。
            InitiateMultipartUploadResult uploadResult = ossClient.initiateMultipartUpload(request);
            // 返回uploadId，它是分片上传事件的唯一标识，您可以根据这个ID来发起相关的操作，如取消分片上传、查询分片上传等。
            String uploadId = uploadResult.getUploadId();
            // partETags是PartETag的集合。PartETag由分片的ETag和分片号组成。
            List<PartETag> partETags =  new ArrayList<>();
            // 单个分片大小
            final long partSize = aliyunOSSConfig.getPartSize();
            // 文件长度
            long fileLength = file.length();
            // 计算分片数，把末尾片加上
            int partCount = (int) (fileLength / partSize);
            if(fileLength % partSize != 0){
                partCount ++;
            }

            // 遍历分片上传
            List<Future<PartETag>> futureList = Collections.synchronizedList(new ArrayList<>());
            CountDownLatch countDownLatch = new CountDownLatch(partCount);
            for(int i = 0; i < partCount; i++) {
                InputStream uploadInputStream = Files.newInputStream(file.toPath());

                // 末尾可能是不足分片大小的
                long startPos = i * partSize;
                long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
                long skipBytes = uploadInputStream.skip(startPos);
                if(i != 0 && skipBytes == 0){
                    throw new HttpException(ExceptionConstant.FilePartUploadError.getMessage_EN());
                }
                int partNum = i + 1;
                Future<PartETag> partETagFuture = taskExecutor.submit(()
                        -> this.getUploadPartETag(
                                fileFullName,
                                bucket,
                                uploadId,
                                uploadInputStream,
                                curPartSize,
                                partNum,
                                ossClient,
                                countDownLatch
                        )
                );
                futureList.add(partETagFuture);
            }
            countDownLatch.await();
            for(Future<PartETag> tagFuture : futureList) {
                partETags.add(tagFuture.get());
            }
            // 创建CompleteMultipartUploadRequest对象。
            List<PartETag> collect = partETags.stream().sorted(Comparator.comparing(PartETag::getPartNumber)).collect(Collectors.toList());
            // 在执行完成分片上传操作时，需要提供所有有效的partETags。OSS收到提交的partETags后，会逐一验证每个分片的有效性。当所有的数据分片验证通过后，OSS将把这些分片组合成一个完整的文件。
            CompleteMultipartUploadRequest completeMultipartUploadRequest =
                    new CompleteMultipartUploadRequest(bucket, fileFullName, uploadId, collect);
            Map<String, String> header = new HashMap<>();
            header.put(WebSetting.getContentMD5(),WebSetting.getNullString());
            completeMultipartUploadRequest.setHeaders(header);

            // 如果需要在完成文件上传的同时设置文件访问权限，请参考以下示例代码。
            //completeMultipartUploadRequest.setObjectACL(CannedAccessControlList.PublicRead);
            // 完成上传。
            CompleteMultipartUploadResult completeMultipartUploadResult = ossClient.completeMultipartUpload(completeMultipartUploadRequest);
            log.info("uploadRequestId:  {}", completeMultipartUploadResult.getRequestId());
            // 关闭OSSClient。
//            ossClient.shutdown();
            return prefixUrl + fileFullName;

        } catch (IOException e) {
            throw new HttpException(
                    e.getMessage()
                    + "  "
                    + ExceptionConstant.FileStreamError.getMessage_EN()
            );
        } catch (InterruptedException e) {
            throw new HttpException(
                    e.getMessage()
                    + "  "
                    + ExceptionConstant.ThreadInterruptError.getMessage_EN()
            );
        } catch (ExecutionException e) {
            throw new HttpException(e.getMessage() +"  "+ e.getCause());
        }
        finally {
            if(Objects.nonNull(ossClient)){
                ossClient.shutdown();
            }
        }
    }

    /*
     * 上传超大文件手动计算分片，调度多线程处理
     * */
    private PartETag getUploadPartETag(String objectName, String bucketName, String uploadId,
                               InputStream inputStream, Long curPartSize,Integer partNum,
                               OSS ossClient, CountDownLatch countDownLatch){
        //long before = System.currentTimeMillis();
        UploadPartRequest uploadPartRequest;
        try {
            uploadPartRequest = new UploadPartRequest();
            uploadPartRequest.setBucketName(bucketName);
            uploadPartRequest.setKey(objectName);
            uploadPartRequest.setUploadId(uploadId);
            uploadPartRequest.setInputStream(inputStream);
            // 设置分片大小。除了最后一个分片没有大小限制，其他的分片最小为100KB。
            uploadPartRequest.setPartSize(curPartSize);
            // 设置分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出这个范围，OSS将返回InvalidArgument的错误码。
            uploadPartRequest.setPartNumber(partNum);
            // 每个分片不需要按顺序上传，甚至可以在不同客户端上传，OSS会按照分片号排序组成完整的文件。
            UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
            // 每次上传分片之后，OSS的返回结果会包含一个PartETag。PartETag将被保存到partETags中。
            return uploadPartResult.getPartETag();
        }finally {
            countDownLatch.countDown();
        }
    }

}
