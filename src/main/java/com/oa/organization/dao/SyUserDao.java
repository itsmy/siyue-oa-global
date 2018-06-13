package com.oa.organization.dao;

import com.oa.organization.entity.SyUser;

import java.math.BigDecimal;
import java.util.List;

public interface SyUserDao {
    /**
     * 获取成员列表，所有人
     *
     * @return
     */
    List<SyUser> getUserList();

    /**
     * 根据部门id查询部门下的人
     *
     * @param deptId
     * @return
     */
    List<SyUser> getUserListByDeptId(BigDecimal deptId);

    /**
     * 获取新入职员工列表
     *
     * @return
     */
    List<SyUser> getNewUserList(int dateDiffer);

    /**
     * 获取离职人员列表
     *
     * @return
     */
    List<SyUser> getLeaveUserList(int dateDiffer);

    /**
     * 获取基本信息变更的人员列表
     *
     * @return
     */
    List<SyUser> getUpdateUserList(int dateDiffer);

    /**
     * 更新某个人的信息
     *
     * @param user
     * @return
     */
    int updateUser(SyUser user);

    /**
     * 根据userId删除成员
     *
     * @param userId
     */
    void deleteUser(BigDecimal userId);

    List<BigDecimal> getAllUserIdList();
}
