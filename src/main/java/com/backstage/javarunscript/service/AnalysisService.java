package com.backstage.javarunscript.service;

import com.backstage.javarunscript.domain.Result;
import com.backstage.javarunscript.domain.entity.Analysis;
import com.backstage.javarunscript.domain.vo.AnalysisVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author zyy
* @description 针对表【analysis(分析记录表)】的数据库操作Service
* @createDate 2024-03-17 13:37:42
*/
public interface AnalysisService extends IService<Analysis> {

    boolean saveRecord(Analysis newRecord);

    AnalysisVO parseAnalysisToAnalysisVO(Analysis analysis);

    Result<?> getAnalysisRecordByUserId(Long userId);

    Result<?> getAllRecords(Long pageNum, Long pageSize);
}
