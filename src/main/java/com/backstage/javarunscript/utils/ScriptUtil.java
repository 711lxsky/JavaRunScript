package com.backstage.javarunscript.utils;

import com.backstage.javarunscript.Exception.HttpException;
import com.backstage.javarunscript.setting_enum.ExceptionConstant;
import com.backstage.javarunscript.setting_enum.FileSetting;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 711lxsky
 * @Date: 2024-03-16
 */

@Log4j2
@Component
public class ScriptUtil {

    @Resource
    private FileUtil fileUtil;

    public String runPyScript() throws HttpException {
        Path scriptFullPath = Paths.get(fileUtil.getPyScriptFullPath());
        List<String> scriptCommand = new ArrayList<>();
        scriptCommand.add(FileSetting.PythonScriptCmd.getValue());
        scriptCommand.add(scriptFullPath.toString());
        // 初始化ProcessBuilder
        ProcessBuilder processBuilder = new ProcessBuilder(scriptCommand);
        processBuilder.redirectErrorStream(true); // Redirect error stream to the output stream
        StringBuilder errorInfo = new StringBuilder();
//        StringBuilder outputInfo = new StringBuilder();
        try {
            Process process = processBuilder.start();

            // 使用单独的线程来读取错误流
            Thread errorThread = new Thread(() -> {
                try (BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                    String info;
                    while ((info = stdError.readLine()) != null) {
                        errorInfo.append(info);
                        log.info(info);
                    }
                } catch (IOException e) {
                    throw new HttpException(e.getMessage());
                }
            });

            /*
            // 使用单独的线程来读取输出流
            Thread outputThread = new Thread(() -> {
                try (BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String info;
                    while ((info = stdInput.readLine()) != null) {
                        outputInfo.append(info);
                        log.info(info);
                    }
                } catch (IOException e) {
                    throw new HttpException(e.getMessage());
                }
            });
            */
            // 启动两个线程
            errorThread.start();
//            outputThread.start();

            // 确保在进程结束之前读取完毕
            int exitCode = process.waitFor();
            errorThread.join();
//            outputThread.join();

            if (exitCode != 0) {
                throw new HttpException(
                        ExceptionConstant.ScriptRunError.getMessage_EN() + errorInfo
                );
            }
            return errorInfo.toString();
        } catch (IOException | InterruptedException e) {
            throw new HttpException(e.getMessage());
        }
    }

}
