package com.backstage.xduedu.mapper;

import com.backstage.xduedu.domain.entity.Analysis;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author zyy
* @description 针对表【analysis(分析记录表)】的数据库操作Mapper
* @createDate 2024-03-17 13:37:42
* @Entity com.backstage.xduedu.domain.entity.Analysis
*/
@Mapper
public interface AnalysisMapper extends BaseMapper<Analysis> {

}




