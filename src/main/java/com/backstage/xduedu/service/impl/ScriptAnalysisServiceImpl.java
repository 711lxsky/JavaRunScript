package com.backstage.xduedu.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import com.backstage.xduedu.Exception.HttpException;
import com.backstage.xduedu.domain.Result;
import com.backstage.xduedu.domain.entity.Analysis;
import com.backstage.xduedu.service.AnalysisService;
import com.backstage.xduedu.service.ScriptAnalysisService;
import com.backstage.xduedu.setting_enum.ResultCodeAndMessage;
import com.backstage.xduedu.utils.AliyunOSSUtil;
import com.backstage.xduedu.utils.FileUtil;
import com.backstage.xduedu.utils.ScriptUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;

/**
 * @Author: 711lxsky
 * @Date: 2024-03-16
 */

@Service
public class ScriptAnalysisServiceImpl implements ScriptAnalysisService {

    @Resource
    private FileUtil fileUtil;

    @Resource
    private ScriptUtil scriptUtil;

    @Resource
    private AliyunOSSUtil aliyunOSSUtil;

    @Resource
    private AnalysisService analysisService;

    @Override
    public Result<?> analysisForZip(MultipartFile file) throws HttpException {
        Analysis newRecord = new Analysis();
        Long userId = Long.parseLong(StpUtil.getLoginId().toString());
        newRecord.setRecordUserId(userId);
        newRecord.setRecordDate(DateUtil.date(System.currentTimeMillis()));
        // 先分析文件类型
        InputStream checkStreamForZip = fileUtil.getFileStream(file);
        String fileOriginName = fileUtil.getFileOriginalName(file);
        fileUtil.judFileTypeForZip(checkStreamForZip, fileOriginName);
        // 校验没有问题之后先传到OSS
        File uploadFile = fileUtil.convertMultipartFileToFile(file);
        String uploadFileUrl = aliyunOSSUtil.saveDateToUploadFile(uploadFile);
        newRecord.setUploadFileOssUrl(uploadFileUrl);
        // 解压文件
        String zipFileMainName = fileUtil.getFileMainName(fileOriginName);
        String unZipDir = fileUtil.getUnZipDir(zipFileMainName);
        fileUtil.upZipFileToDest(uploadFile, unZipDir);// 这里解压完成
        // 运行脚本
        scriptUtil.runPyScript();
        // 压缩运行结果
        String zipFileDir = fileUtil.getUnZipDirForRandomName();
        fileUtil.zipFileForDir(fileUtil.getAnalysisDir(), zipFileDir);
        // 删除解压文件夹和解压文件
        fileUtil.deleteFile(uploadFile);
        fileUtil.deleteDirectory(unZipDir);
        // 将运行结果作为.zip文件上传到OSS指定bucket目录下
        File analysisFile = fileUtil.createFileForZip(zipFileDir);
        String analysisFileUrl = aliyunOSSUtil.saveDateToAnalysisFile(analysisFile);
        // 删除压缩文件
        fileUtil.deleteFile(analysisFile);
        fileUtil.deleteDirectory(zipFileDir);
        newRecord.setAnalysisFileOssUrl(analysisFileUrl);
        if(!analysisService.saveRecord(newRecord)){
            throw new HttpException();
        }
        return Result.success(ResultCodeAndMessage.AnalysisSuccess.getDescription(), analysisService.parseAnalysisToAnalysisVO(newRecord));
    }
}
