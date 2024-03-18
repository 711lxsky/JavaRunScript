package com.backstage.xduedu.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: 711lxsky
 * @Date: 2024-03-17
 */

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户信息")
public class UserVO implements Serializable {

    /**
     * 用户id，即账号
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String userName;

    private static final long serialVersionUID = 1L;

}
