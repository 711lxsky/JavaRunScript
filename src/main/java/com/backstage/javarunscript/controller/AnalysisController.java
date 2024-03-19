package com.backstage.javarunscript.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.backstage.javarunscript.Exception.HttpException;
import com.backstage.javarunscript.domain.Result;
import com.backstage.javarunscript.service.AnalysisService;
import com.backstage.javarunscript.service.ScriptAnalysisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @Author: 711lxsky
 * @Date: 2024-03-16
 */
@Api(tags = "分析模块")
@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    @Resource
    private ScriptAnalysisService scriptAnalysisService;

    @Resource
    private AnalysisService analysisService;

    @ApiOperation(value = "分析单个文件")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "file", value = ".zip文件", required = true, dataType = "MultipartFile")
    )
    @SaCheckLogin
    @PostMapping("/for-zip")
    public Result<?> analysis(@RequestParam(name = "file") MultipartFile file){
        try {
            return scriptAnalysisService.analysisForZip(file);
        }
        catch (HttpException e){
            e.printStackTrace();
            return Result.fail(e);
        }
    }

    @ApiOperation(value = "分页获取所有分析记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = true, dataType = "Long")
    })
    @SaCheckLogin
    @GetMapping("/get-record-all")
    public Result<?> getAllRecords(@RequestParam(name = "pageNum") Long pageNum,
                                   @RequestParam(name = "pageSize") Long pageSize){
        try {
            return analysisService.getAllRecords(pageNum,pageSize);
        }
        catch (HttpException e){
            e.printStackTrace();
            return Result.fail(e);
        }
    }

    @ApiOperation(value = "根据用户id获取分析记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "Long")
    })
    @SaCheckLogin
    @GetMapping("/get-record-user")
    public Result<?> getAnalysisRecordByUserId(@RequestParam(name = "userId") Long userId){
        try {
            return analysisService.getAnalysisRecordByUserId(userId);
        }
        catch (HttpException e){
            e.printStackTrace();
            return Result.fail(e);
        }
    }

}
