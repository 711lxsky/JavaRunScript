package com.backstage.javarunscript.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * @Author: 711lxsky
 * @Date: 2024-03-18
 */

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户更新信息")
public class UpdateUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String userName;

    /**
     * 旧密码
     */
    @ApiModelProperty("旧密码")
    private String oldPassword;

    /**
     * 登录密码
     */
    @ApiModelProperty("新密码")
    private String password;

    /**
     * 确认密码
     */
    @ApiModelProperty("确认密码")
    private String validPassword;
}
