package com.backstage.xduedu.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户表
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 用户id，即账号
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 用户名
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 密码盐值
     */
    @TableField(value = "salt")
    private String salt;

    /**
     * 登录密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 用户创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 信息修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 登录ip
     */
    @TableField(value = "login_ip")
    private String loginIP;

    /**
     * 逻辑删除标识
     */
    @TableField(value = "deleted")
    private Integer deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}