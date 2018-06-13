package com.oa.organization.dao;

import com.oa.organization.entity.UserRecord;

import java.math.BigDecimal;
import java.util.List;

public interface UserRecordDao {

    int createUser(UserRecord userRecord);

    void deleteUser(String userId);

    int updateUser(UserRecord userRecord);

    List<UserRecord> getAllUserList();

    List<BigDecimal> getAllUserIdList();

    List<UserRecord> getUserListByDeptId(String ddDeptId);

    int insertUserList(List<UserRecord> userRecordList);
}
