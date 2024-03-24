package com.backstage.javarunscript.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: 711lxsky
 * @Description: 针对分析记录的配置项
 */

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "analysis-settings")
public class AnalysisConfig {

    private Integer analysisFail;

    private Integer analysisSuccess;

    private Integer analysisRunning;
}
