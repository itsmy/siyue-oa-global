package com.oa.organization.dao;

import com.oa.organization.entity.SyncLog;


/*日志dao接口*/
public interface SyncLogDao {
    /*更新之前和之后都需要调用该接口设置状态*/
    void insertLog(SyncLog syncLog);

    /*只需要更新时间和状态*/
    void updateLog(SyncLog syncLog);

    /*查询最后一次成功的日期*/
    SyncLog getLatestSuccessLog();

    /*查询最后一次插入数据的日志*/
    SyncLog getLatestFalseLog();

}
