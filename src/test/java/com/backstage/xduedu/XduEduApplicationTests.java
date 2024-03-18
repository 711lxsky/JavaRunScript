package com.backstage.xduedu;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.backstage.xduedu.config.FileConfig;
import com.backstage.xduedu.config.SecurityConfig;
import com.backstage.xduedu.utils.FileUtil;
import com.backstage.xduedu.utils.ScriptUtil;
import com.backstage.xduedu.utils.SecurityUtil;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;


@SpringBootTest(classes = XduEduApplication.class)
@Log4j2
class XduEduApplicationTests {

    @Resource
    private FileConfig fileConfig;

    @Resource
    private FileUtil fileUtil;

    @Resource
    private ScriptUtil scriptUtil;

    @Resource
    private SecurityConfig securityConfig;

    @Test
    void contextLoads() {
       log.info(fileConfig.getBufferLength());
    }

    @Test
    void RandomStrTest() {
        log.info(RandomUtil.randomString(16));
        log.info(RandomUtil.randomString(16));

        log.info(Arrays.toString(RandomUtil.randomInts(16)));
        log.info(Arrays.toString(RandomUtil.randomInts(16)));
    }

    @Test
    void testWorkDir() {
        log.info(fileUtil.getCurWordDir());
        scriptUtil.runPyScript();
        byte[] key1 = SecureUtil.generateKey(SymmetricAlgorithm.DESede.getValue()).getEncoded();
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
        log.info(Arrays.toString(key));
        log.info(key1);
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, "fdsfswef".getBytes(StandardCharsets.UTF_8));
        log.info(aes);
        log.info(securityConfig.getPwdKey());
        log.info(securityConfig.getSaltKey());
    }

    @Resource
    private SecurityUtil securityUtil;
    @Test
    void securityPwdTest(){
        String rawPwd = "zyy123";
        String salt = securityUtil.getSalt();
        String realPwd = securityUtil.addSaltIntoPassword(salt, rawPwd);
        log.info("rawPwd :{} --- salt :{} ----- realPwd :{}", rawPwd, salt, realPwd);
        String encryptedPwd = securityUtil.AESEncryptForPwd(realPwd);
        log.info("encryptedPwd :{}", encryptedPwd);
        String decryptedPwd = securityUtil.AESDecryptForPwd(encryptedPwd);
        log.info("realPwd == decryptedPwd ? : {}, dec:{}", Objects.equals(realPwd, decryptedPwd), decryptedPwd);
        String encryptedSalt = securityUtil.AESEncryptForSalt(salt);
        log.info("encryptedSalt :{}", encryptedSalt);
        String decryptedSalt = securityUtil.AESDecryptForSalt(encryptedSalt);
        log.info("salt == decryptedSalt ? : {}, decS:{}", Objects.equals(salt, decryptedSalt), decryptedSalt);
    }
}
