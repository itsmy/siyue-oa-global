package com.oa.organization.service.impl;

import com.oa.organization.dao.UserRecordDao;
import com.oa.organization.entity.UserRecord;
import com.oa.organization.service.UserRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserRecordServiceImpl implements UserRecordService {
    @Autowired
    private UserRecordDao userRecordDao;

    @Override
    public int createUser(UserRecord userRecord) {
        return userRecordDao.createUser(userRecord);
    }

    @Override
    public void deleteUser(String userId) {
        userRecordDao.deleteUser(userId);
    }

    @Override
    public int updateUser(UserRecord userRecord) {
        return userRecordDao.updateUser(userRecord);
    }

    @Override
    public List<UserRecord> getAllUserList() {
        return userRecordDao.getAllUserList();
    }

    @Override
    public List<BigDecimal> getAllUserIdList() {
        return userRecordDao.getAllUserIdList();
    }

    @Override
    public List<UserRecord> getUserListByDeptId(String ddDeptId) {
        return userRecordDao.getUserListByDeptId(ddDeptId);
    }

    @Override
    public int insertUserList(List<UserRecord> userRecordList) {
        return userRecordDao.insertUserList(userRecordList);
    }
}
