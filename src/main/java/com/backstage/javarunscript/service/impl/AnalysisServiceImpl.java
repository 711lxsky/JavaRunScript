package com.backstage.javarunscript.service.impl;

import com.backstage.javarunscript.Exception.HttpException;
import com.backstage.javarunscript.domain.Result;
import com.backstage.javarunscript.domain.entity.Analysis;
import com.backstage.javarunscript.domain.vo.AnalysisVO;
import com.backstage.javarunscript.mapper.AnalysisMapper;
import com.backstage.javarunscript.service.AnalysisService;
import com.backstage.javarunscript.setting_enum.ExceptionConstant;
import com.backstage.javarunscript.setting_enum.RecordSetting;
import com.backstage.javarunscript.setting_enum.ResultCodeAndMessage;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.*;

/**
* @author zyy
* @description 针对表【analysis(分析记录表)】的数据库操作Service实现
* @createDate 2024-03-17 13:37:42
*/
@Service
public class AnalysisServiceImpl extends ServiceImpl<AnalysisMapper, Analysis>
    implements AnalysisService{

    @Override
    public boolean saveRecord(Analysis newRecord) {
        return this.save(newRecord);
    }

    @Override
    public AnalysisVO parseAnalysisToAnalysisVO(Analysis analysis) {
        AnalysisVO analysisVO = new AnalysisVO();
        analysisVO.setRecordId(analysis.getRecordId());
        analysisVO.setRecordDate(analysis.getRecordDate());
        analysisVO.setUploadFileOssUrl(analysis.getUploadFileOssUrl());
        analysisVO.setAnalysisFileOssUrl(analysis.getAnalysisFileOssUrl());
        return analysisVO;
    }

    @Override
    public Result<?> getAnalysisRecordByUserId(Long userId) throws HttpException{
        if(Objects.isNull(userId) || userId < RecordSetting.getMinUserId()){
            throw new HttpException(ExceptionConstant.DataError.getMessage_EN());
        }
        LambdaQueryWrapper<Analysis> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Analysis::getRecordUserId, userId);
        List<Analysis> record = this.list(wrapper);
        if(record.isEmpty()){
            throw new HttpException(ExceptionConstant.DataNotFound.getMessage_EN());
        }
        List<AnalysisVO> analysisVOList = new ArrayList<>();
        record.forEach(
                analysis -> {
                    AnalysisVO analysisVO = parseAnalysisToAnalysisVO(analysis);
                    analysisVOList.add(analysisVO);
                }
        );
        Map<String, Object> recordMap = new HashMap<>();
        recordMap.put(RecordSetting.getTotalCount(), analysisVOList.size());
        recordMap.put(RecordSetting.getRecordName(), analysisVOList);
        return Result.success(ResultCodeAndMessage.QuerySuccess.getDescription(), recordMap);
    }

    @Override
    public Result<?> getAllRecords(Long pageNum, Long pageSize) throws HttpException{
        if(Objects.isNull(pageNum) || Objects.isNull(pageSize)
        || pageNum < RecordSetting.getMinPageNum() || pageSize < RecordSetting.getMinPageSize()){
            throw new HttpException(ExceptionConstant.DataError.getMessage_EN());
        }
        Page<Analysis> pageData = new Page<>(pageNum, pageSize);
        this.page(pageData);
        List<AnalysisVO> record = new ArrayList<>();
        pageData.getRecords().forEach(
                analysis -> {
                    AnalysisVO analysisVO = parseAnalysisToAnalysisVO(analysis);
                    record.add(analysisVO);
                }
        );
        Map<String, Object> data = new HashMap<>();
        data.put(RecordSetting.getTotalCount(), record.size());
        data.put(RecordSetting.getCurPage(), pageNum);
        data.put(RecordSetting.getPageSize(), pageSize);
        data.put(RecordSetting.getRecordName(), record);
        return Result.success(ResultCodeAndMessage.QuerySuccess.getDescription(), data);
    }

}




