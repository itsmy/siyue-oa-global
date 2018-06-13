package com.oa.organization.service;

import com.oa.organization.entity.SyUser;

import java.math.BigDecimal;
import java.util.List;

public interface SyUserService {
    /**
     * 查询所有人员的列表
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
     * 新入职人员列表
     *
     * @return
     */
    List<SyUser> getNewUserList(int dateDiffer);

    /**
     * 离职人员列表
     *
     * @return
     */
    List<SyUser> getLeaveUserList(int dateDiffer);

    /**
     * 个人信息变更人员列表
     *
     * @return
     */
    List<SyUser> getUpdateUserList(int dateDiffer);

    List<BigDecimal> getAllUserIdList();

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
}
