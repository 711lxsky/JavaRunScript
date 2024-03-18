package com.backstage.xduedu.utils;

import com.backstage.xduedu.Exception.HttpException;
import com.backstage.xduedu.setting_enum.ExceptionConstant;
import org.springframework.util.StringUtils;

/**
 * @Author: 711lxsky
 * @Date: 2024-03-16
 */

public class StrUtil {

    public static String judStrIsBlank(String str) throws HttpException{
        if(StringUtils.hasText(str)){
            return str;
        }
        throw new HttpException(ExceptionConstant.FileNameError.getMessage_EN());
    }
}
