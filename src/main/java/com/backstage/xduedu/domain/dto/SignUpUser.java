package com.backstage.xduedu.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
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
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户注册信息")
public class SignUpUser implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 确认密码
     */
    @ApiModelProperty("确认密码")
    private String validPassword;

}
