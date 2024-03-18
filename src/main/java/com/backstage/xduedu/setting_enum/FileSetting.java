package com.backstage.xduedu.setting_enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: 711lxsky
 * @Date: 2024-03-16
 */

@Getter
@AllArgsConstructor
public enum FileSetting {


//    WorkDir("work/"),
    ZipType("zip"),
    PythonScriptCmd("python3");

    private final String value;
}
