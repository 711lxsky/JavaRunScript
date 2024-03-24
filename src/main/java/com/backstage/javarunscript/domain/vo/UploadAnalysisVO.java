package com.backstage.javarunscript.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;

/**
 * @Author: 711lxsky
 * @Description: 上传记录的视图类
 */

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("上传记录")
public class UploadAnalysisVO {

    @ApiModelProperty("分析记录id")
    private Long recordId;

    @ApiModelProperty("上传文件时间")
    private Date uploadDate;

    @ApiModelProperty("上传的文件转储在oss后生成的url链接")
    private String uploadFileOssUrl;

    @ApiModelProperty("解析状态, 1=成功， 0=失败， 2=解析中")
    private Integer analysisStatus;
}