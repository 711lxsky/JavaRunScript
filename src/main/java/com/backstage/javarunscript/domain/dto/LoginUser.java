package com.backstage.javarunscript.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * @Author: 711lxsky
 * @Date: 2024-03-17
 */


@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户登录信息")
public class LoginUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String userName;

    /**
     * 登录密码
     */
    @ApiModelProperty("登录密码")
    private String password;

    /**
     * 登录ip
     */
    @ApiModelProperty("登录ip")
    private String loginIP;
}
