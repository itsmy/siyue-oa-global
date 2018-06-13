package com.oa.organization.service;

import com.oa.organization.entity.SyncLog;

import java.util.Date;

public interface SyncLogService {
    /*更新之前和之后都需要调用该接口设置状态*/
    void insertLog();

    /*只需要更新时间和状态*/
    void updateLog(String status);

    /*查询最后一次成功的日期*/
    Date getLatestSuccessLog();

    /*查询最后一次插入数据的日期*/
    SyncLog getLatestFalseLog();
}
