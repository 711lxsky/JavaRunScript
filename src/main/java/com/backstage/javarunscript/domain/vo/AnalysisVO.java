package com.backstage.javarunscript.domain.vo;

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
 * @Date: 2024-03-18
 */

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("分析记录")
public class AnalysisVO implements Serializable {

    /**
     * 分析记录id
     */
    @ApiModelProperty("分析记录id")
    private Long recordId;

    /**
     * 上传文件时间
     */
    @ApiModelProperty("上传文件时间")
    private Date uploadDate;

    /**
     * 解析状态
     */
    @ApiModelProperty("解析状态, 1=成功， 0=失败， 2=解析中")
    private Integer analysisStatus;

    /**
     * 记录解析时间
     */
    @ApiModelProperty("记录解析时间")
    private Date recordDate;

    /**
     * 上传的文件转储在oss后生成的url链接
     */
    @ApiModelProperty("上传的文件转储在oss后生成的url链接")
    private String uploadFileOssUrl;

    /**
     * 解析完成的文件转储OSS生成的url链接
     */
    @ApiModelProperty("解析完成的文件转储OSS生成的url链接")
    private String analysisFileOssUrl;

    /**
     * 解析备注
     */
    @ApiModelProperty("解析备注")
    private String analysisRemark;

    private static final long serialVersionUID = 1L;


}
