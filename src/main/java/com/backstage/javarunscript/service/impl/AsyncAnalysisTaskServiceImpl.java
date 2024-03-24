package com.backstage.javarunscript.service.impl;

import cn.hutool.core.date.DateUtil;
import com.backstage.javarunscript.Exception.HttpException;
import com.backstage.javarunscript.config.AnalysisConfig;
import com.backstage.javarunscript.domain.entity.Analysis;
import com.backstage.javarunscript.service.AnalysisService;
import com.backstage.javarunscript.service.AsyncAnalysisTaskService;
import com.backstage.javarunscript.setting_enum.ResultCodeAndMessage;
import com.backstage.javarunscript.utils.AliyunOSSUtil;
import com.backstage.javarunscript.utils.FileUtil;
import com.backstage.javarunscript.utils.ScriptUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.concurrent.CompletableFuture;

/**
 * @Author: 711lxsky
 * @Description: 异步任务执行实现类
 */

@Service
public class AsyncAnalysisTaskServiceImpl implements AsyncAnalysisTaskService {

    @Resource
    private FileUtil fileUtil;

    @Resource
    private ScriptUtil scriptUtil;

    @Resource
    private AnalysisConfig analysisConfig;

    @Resource
    private AliyunOSSUtil aliyunOSSUtil;

    @Resource
    private AnalysisService analysisService;

    @Override
    @Async
    public CompletableFuture<String> analysisForZip(File uploadFile, String zipFileOriginName, Long analysisId) {
        Analysis record = new Analysis();
        record.setRecordId(analysisId);
        // 解压文件
        String zipFileMainName = fileUtil.getFileMainName(zipFileOriginName);
        String unZipDir = fileUtil.getUnZipDir(zipFileMainName);
        fileUtil.upZipFileToDest(uploadFile, unZipDir);// 这里解压完成
        record.setRecordDate(DateUtil.date(System.currentTimeMillis()));
        // 运行脚本
        try{
            String scriptRunInfo = scriptUtil.runPyScript();
            record.setAnalysisRemark(scriptRunInfo);
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
            record.setAnalysisFileOssUrl(analysisFileUrl);
            record.setAnalysisStatus(analysisConfig.getAnalysisSuccess());
        }
        catch (HttpException e){
            record.setAnalysisStatus(analysisConfig.getAnalysisFail());
            return CompletableFuture.completedFuture(ResultCodeAndMessage.AnalysisComplete.getDescription() + ResultCodeAndMessage.Fail.getDescription());
        }
        finally {
            analysisService.updateById(record);
        }
        return CompletableFuture.completedFuture(ResultCodeAndMessage.AnalysisComplete.getDescription() + ResultCodeAndMessage.SUCCESS.getDescription());
    }
}
