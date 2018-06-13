package com.oa.organization.service.impl;

import com.oa.organization.dao.SyncLogDao;
import com.oa.organization.entity.SyncLog;
import com.oa.organization.service.SyncLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SyncLogServiceImpl implements SyncLogService {
    @Autowired
    private SyncLogDao syncLogDao;

    @Override
    public void insertLog(){
        SyncLog syncLog = new SyncLog();
        syncLog.setModelName("DingTalk");
        syncLog.setTableName("T011");
        syncLog.setSyncDate(new Date());
        syncLog.setSyncStatu("F");
        syncLogDao.insertLog(syncLog);
    }

    @Override
    public void updateLog(String status) {
        SyncLog syncLog = getLatestFalseLog();
        if (syncLog != null && !syncLog.equals("")) {
            syncLog.setSyncStatu(status);
            syncLogDao.updateLog(syncLog);
        } else {
            insertLog();
        }
    }

    @Override
    public Date getLatestSuccessLog() {
        SyncLog syncLog = syncLogDao.getLatestSuccessLog();
        return syncLog.getSyncDate();
    }

    @Override
    public SyncLog getLatestFalseLog() {
        return syncLogDao.getLatestFalseLog();
    }
}
