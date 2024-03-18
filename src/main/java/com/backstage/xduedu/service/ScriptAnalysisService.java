package com.backstage.xduedu.service;

import com.backstage.xduedu.domain.Result;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: 711lxsky
 * @Date: 2024-03-16
 */

public interface ScriptAnalysisService {

    Result<?> analysisForZip(MultipartFile file);
}
