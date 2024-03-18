package com.backstage.xduedu.setting_enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;

/**
 * @Author: 711lxsky
 * @Date: 2024-03-16
 */

@AllArgsConstructor
@Getter
public enum StringConstant {

    Separator(File.separator),
    Dot(".");

    private final String value;

}
