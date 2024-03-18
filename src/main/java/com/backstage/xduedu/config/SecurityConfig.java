package com.backstage.xduedu.config;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

/**
 * @Author: 711lxsky
 * @Date: 2024-03-17
 */

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "security-settings")
public class SecurityConfig {

    private String pwdKey;

    private String pwdIv;

    private String saltKey;

    private String saltIv;

    private int saltLen;

    @Bean(name = "pwdAES")
    public AES getAESForPwd(){
        return new AES(
                Mode.CBC,
                Padding.PKCS5Padding,
                pwdKey.getBytes(StandardCharsets.UTF_8),
                pwdIv.getBytes(StandardCharsets.UTF_8)
        );
    }

    @Bean(name = "saltAES")
    public AES getAESForSalt(){
        return new AES(
                Mode.CBC,
                Padding.PKCS5Padding,
                saltKey.getBytes(StandardCharsets.UTF_8),
                saltIv.getBytes(StandardCharsets.UTF_8)
        );
    }

}
