package com.oa.organization.service;

import com.oa.organization.entity.UserRecord;

import java.math.BigDecimal;
import java.util.List;

public interface UserRecordService {
    int createUser(UserRecord userRecord);

    void deleteUser(String userId);

    /*更新*/
    int updateUser(UserRecord userRecord);

    /*查询所有user*/
    List<UserRecord> getAllUserList();
	
    List<BigDecimal> getAllUserIdList();

    /*根据部门获取部门下的成员，不包含子部门*/
    List<UserRecord> getUserListByDeptId(String ddDeptId);

    /*批量插入*/
    int insertUserList(List<UserRecord> userRecordList);
}
