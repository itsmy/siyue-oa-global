package com.oa.organization.service.impl;

import com.oa.organization.entity.DeptRecord;
import com.oa.organization.entity.UserRecord;
import com.oa.organization.service.DeptRecordService;
import com.oa.organization.service.SyUserService;
import com.oa.organization.dao.SyUserDao;
import com.oa.organization.entity.SyUser;
import com.oa.organization.service.SyUserService;
import com.oa.organization.service.UserRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SyUserServiceImpl implements SyUserService {
    @Autowired
    private SyUserDao syUserDao;
    @Autowired
    private DeptRecordService deptRecordService;

    @Override
    public List<SyUser> getUserList() {
        return syUserDao.getUserList();
    }

    @Override
    public List<SyUser> getUserListByDeptId(BigDecimal deptId) {
        return syUserDao.getUserListByDeptId(deptId);
    }

    /**
     * 过滤人员，这里需要判断新入职的人员是否在记录表中的部门范围内
     *
     * @return
     */
    @Override
    public List<SyUser> getNewUserList(int dateDiffer) {
        List<SyUser> newUserList = syUserDao.getNewUserList(dateDiffer);
        return filterUser(newUserList);
    }

    /**
     * 离职人员是否在记录表中的部门范围内
     *
     * @return
     */
    @Override
    public List<SyUser> getLeaveUserList(int dateDiffer) {
        List<SyUser> leaveUserList = syUserDao.getLeaveUserList(dateDiffer);
        return filterUser(leaveUserList);
    }

    /**
     * 属性更新的人员是否在记录表中的部门范围内
     *
     * @return
     */
    @Override
    public List<SyUser> getUpdateUserList(int dateDiffer) {
        List<SyUser> updateUserList = syUserDao.getUpdateUserList(dateDiffer);
        return filterUser(updateUserList);
    }

    @Override
    public List<BigDecimal> getAllUserIdList() {
        return syUserDao.getAllUserIdList();
    }

    private List<SyUser> filterUser(List<SyUser> userList) {
        List<BigDecimal> deptRecordIdList = deptRecordService.getDeptIdList();
        List<SyUser> resultUserList = new ArrayList<SyUser>();
        for (int i = 0; i < userList.size(); i++) {
            SyUser user = userList.get(i);
            BigDecimal departmentId = user.getDepartmentId();
            if (deptRecordIdList.contains(departmentId)) {
                resultUserList.add(user);
            }
        }
        return resultUserList;
    }

    @Override
    public int updateUser(SyUser user) {
        return syUserDao.updateUser(user);
    }

    @Override
    public void deleteUser(BigDecimal userId) {
        syUserDao.deleteUser(userId);
    }
}
