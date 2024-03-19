package com.backstage.javarunscript.setting_enum;

import lombok.Getter;

/**
 * @Author: 711lxsky
 * @Date: 2024-03-18
 */

public class RecordSetting {

    @Getter
    private static final Long minUserId = 1000L;

    @Getter
    private static final Long minPageNum = 0L;

    @Getter
    private static final Long minPageSize = 1L;

    @Getter
    private static final String totalCount = "totalCount";

    @Getter
    private static final String recordName = "record";

    @Getter
    private static final String curPage = "curPage";

    @Getter
    private static final String pageSize = "pageSize";

}
