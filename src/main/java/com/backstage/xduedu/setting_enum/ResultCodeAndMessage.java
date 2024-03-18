package com.backstage.xduedu.setting_enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: 711lxsky
 * @Date: 2024-03-16
 */


@AllArgsConstructor
@Getter
public enum ResultCodeAndMessage {

    SUCCESS(200, "OK", "成功"),
    SignUpSuccess(200, "Sign up success!", "注册成功"),
    LoginSuccess(200, "Login success!", "登录成功"),
    AnalysisSuccess(200, "Analysis success!", "解析成功"),
    QuerySuccess(200, "Query success!", "查询成功"),
    UpdateSuccess(200, "Update success!", "更新成功");



    /**
     * 消息码
     */
    private final Integer code;

    /**
     * 描述
     */
    private final String description;

    /**
     * 中文描述
     */
    private final String zhDescription;
}
