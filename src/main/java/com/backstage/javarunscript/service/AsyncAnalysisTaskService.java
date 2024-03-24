package com.backstage.javarunscript.service;

import java.io.File;
import java.util.concurrent.CompletableFuture;

/**
 * @Author: 711lxsky
 * @Description: 异步执行分析任务服务层
 */

public interface AsyncAnalysisTaskService {

    CompletableFuture<String> analysisForZip(File uploadFile, String zipFileOriginName, Long analysisId);
}