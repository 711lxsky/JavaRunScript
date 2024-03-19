package com.backstage.javarunscript.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.backstage.javarunscript.Exception.HttpException;
import com.backstage.javarunscript.config.SecurityConfig;
import com.backstage.javarunscript.setting_enum.ExceptionConstant;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @Author: 711lxsky
 * @Date: 2024-03-17
 */

@Component
public class SecurityUtil {

    @Resource
    private SecurityConfig securityConfig;

    @Resource(name = "pwdAES")
    private AES pwdAES;

    @Resource(name = "saltAES")
    private SymmetricCrypto saltAES;

    public String getSalt(){
        return RandomUtil.randomString(securityConfig.getSaltLen());
    }

    public String AESEncryptForPwd(String rawPwd) throws HttpException{
        try {
            return pwdAES.encryptBase64(rawPwd);
        } catch (RuntimeException e){
            throw new HttpException(e.getMessage() + "  " + ExceptionConstant.EncryptError.getMessage_EN());
        }
    }

    public String AESDecryptForPwd(String encryptedPwd) throws HttpException{
        try {
            return pwdAES.decryptStr(encryptedPwd);
        }catch (RuntimeException e){
            throw new HttpException(e.getMessage() + "  " + ExceptionConstant.DecryptError.getMessage_EN());
        }

    }

    public String AESEncryptForSalt(String salt) throws HttpException{
        try {
            return saltAES.encryptBase64(salt);
        } catch (RuntimeException e){
            throw new HttpException(e.getMessage() + "  " + ExceptionConstant.EncryptError.getMessage_EN());
        }

    }

    public String AESDecryptForSalt(String encryptedSalt) throws HttpException{
        try {
            return saltAES.decryptStr(encryptedSalt);
        } catch (RuntimeException e){
            throw new HttpException(e.getMessage() + "  " + ExceptionConstant.DecryptError.getMessage_EN());
        }
    }

    public String addSaltIntoPassword(String salt, String password) throws HttpException {
        if( StringUtils.hasText(salt) && StringUtils.hasText(password)){
            int lenS = salt.length(), lenP = password.length();
            int sumLen = lenS + lenP;
            char [] desStr = new char[sumLen];
            for(int index = 0, ptrS = 0, ptrP = 0; index < sumLen; ){
                if(ptrS < lenS){
                    desStr[index++] = salt.charAt(ptrS++);
                }
                if(ptrP < lenP){
                    desStr[index++] = password.charAt(ptrP++);
                }
            }
            return new String(desStr);
        }
        throw new HttpException(ExceptionConstant.DateNull.getMessage_EN());
    }

    public void checkPassword(String encryptedPwd, String encryptedSalt, String rawPwd) throws HttpException{
        String decryptedPwd = this.AESDecryptForPwd(encryptedPwd);
        String decryptedSalt = this.AESDecryptForSalt(encryptedSalt);
        String realPwd = this.addSaltIntoPassword(decryptedSalt, rawPwd);
        if(! decryptedPwd.equals(realPwd)){
            throw new HttpException(ExceptionConstant.PasswordError.getMessage_EN());
        }
    }

}
