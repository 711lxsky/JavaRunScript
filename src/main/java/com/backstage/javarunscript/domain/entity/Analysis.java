package com.backstage.javarunscript.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 分析记录表
 * @TableName analysis
 */
@TableName(value ="analysis")
@Data
public class Analysis implements Serializable {
    /**
     * 分析记录id
     */
    @TableId(value = "record_id", type = IdType.AUTO)
    private Long recordId;

    /**
     * 解析记录的用户id
     */
    @TableField(value = "record_user_id")
    private Long recordUserId;

    /**
     * 记录解析时间
     */
    @TableField(value = "record_date")
    private Date recordDate;

    /**
     * 上传的文件转储在oss后生成的url链接
     */
    @TableField(value = "upload_file_oss_url")
    private String uploadFileOssUrl;

    /**
     * 解析完成的文件转储OSS生成的url链接
     */
    @TableField(value = "analysis_file_oss_url")
    private String analysisFileOssUrl;

    /**
     * 逻辑删除标识
     */
    @TableField(value = "deleted")
    private Integer deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}