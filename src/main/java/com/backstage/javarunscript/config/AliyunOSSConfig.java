package com.backstage.javarunscript.config;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @Author: 711lxsky
 * @Date: 2024-02-21
 */

@Getter
@Configuration
public class AliyunOSSConfig {

    @Value("${aliyunoss.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyunoss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyunoss.endpoint}")
    private String endpoint;

    @Value("${aliyunoss.bucket}")
    private String bucket;

    @Value("${aliyunoss.uploadFilePath}")
    private String uploadFilePath;

    @Value("${aliyunoss.analysisFilePath}")
    private String analysisFilePath;

    @Value("${aliyunoss.partSize}")
    private long partSize;

    @Value("${aliyunoss.connectionTimeout}")
    private  int connectionTimeout;

    @Value("${aliyunoss.requestTimeout}")
    private int requestTimeout;

    @Value("${aliyunoss.socketTimeout}")
    private int socketTimeout;

    @Bean("AliyunOssClient")
    @Scope("prototype") // 这里是多例，因为Spring默认全局共享一个Bean,第一次shutdown之后导致第二次上传的时候连接断了报错
    public OSS getOSSClient() {
        ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
        conf.setConnectionTimeout(connectionTimeout);
        conf.setRequestTimeout(requestTimeout);
        conf.setSocketTimeout(socketTimeout);
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret,conf);
    }

}
