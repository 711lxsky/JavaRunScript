package com.backstage.javarunscript.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: 711lxsky
 * @Date: 2024-03-16
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "file-settings")
public class FileConfig {

    private int bufferLength;

    private int randomZipFileNameLength;

    private String worker;

    private String workerFolder;

    private String scriptsDir;

    private String scriptName;

    private String scriptResFileName;

    private String contentType;

    private String uploadFilePrefix;

}
