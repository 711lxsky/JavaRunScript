package com.backstage.javarunscript.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import com.backstage.javarunscript.Exception.HttpException;
import com.backstage.javarunscript.config.AnalysisConfig;
import com.backstage.javarunscript.domain.Result;
import com.backstage.javarunscript.domain.entity.Analysis;
import com.backstage.javarunscript.service.AnalysisService;
import com.backstage.javarunscript.service.AsyncAnalysisTaskService;
import com.backstage.javarunscript.service.ScriptAnalysisService;
import com.backstage.javarunscript.setting_enum.ResultCodeAndMessage;
import com.backstage.javarunscript.utils.AliyunOSSUtil;
import com.backstage.javarunscript.utils.FileUtil;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class ScriptAnalysisServiceImpl implements ScriptAnalysisService {

    @Resource
    private FileUtil fileUtil;

    @Resource
    private AnalysisConfig analysisConfig;

    @Resource
    private AliyunOSSUtil aliyunOSSUtil;

    @Resource
    private AnalysisService analysisService;

    @Resource
    private AsyncAnalysisTaskService asyncAnalysisTaskService;

    @Override
    public Result<?> analysisForZip(MultipartFile file) throws HttpException {
        // 这里作异步调用，上传文件和解压、分析文件分开，当前只负责上传文件逻辑，后续任务交由异步任务队列调用处理
        Analysis newRecord = new Analysis();
        Long userId = Long.parseLong(StpUtil.getLoginId().toString());
        newRecord.setRecordUserId(userId);
        newRecord.setUploadDate(DateUtil.date(System.currentTimeMillis()));
        // 先分析文件类型
        InputStream checkStreamForZip = fileUtil.getFileStream(file);
        String fileOriginName = fileUtil.getFileOriginalName(file);
        fileUtil.judFileTypeForZip(checkStreamForZip, fileOriginName);
        // 校验没有问题之后先传到OSS
        File uploadFile = fileUtil.convertMultipartFileToFile(file);
        String uploadFileUrl = aliyunOSSUtil.saveDateToUploadFile(uploadFile);
        newRecord.setUploadFileOssUrl(uploadFileUrl);
        newRecord.setAnalysisStatus(analysisConfig.getAnalysisRunning());
        if(!analysisService.saveRecord(newRecord)){
            throw new HttpException();
        }
        asyncAnalysisTaskService.analysisForZip(uploadFile, fileOriginName, newRecord.getRecordId()).thenAccept(
                log::info
        );
        return Result.success(ResultCodeAndMessage.UploadSuccess.getDescription(), analysisService.parseAnalysisToUploadAnalysisVO(newRecord));

    }
}
