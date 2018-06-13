package com.oa.organization.dao;

import com.oa.organization.entity.SyDepartment;

import java.math.BigDecimal;
import java.util.List;

public interface SyDepartmentDao {
    /**
     * 获取部门列表
     *
     * @return
     */
    List<SyDepartment> getDepartmentList();

    /*获取父部门下的所有子部门，含递归*/
    List<SyDepartment> getSubDeptListByFather(BigDecimal fatherDeptId);

    /**
     * 获取所有部门的id
     *
     * @return
     */
    List<BigDecimal> getDeptIdList();

    /**
     * 根据父部门查询部门id
     *
     * @param fatherDeptId
     * @return
     */
    List<SyDepartment> getDeptListByFatherId(BigDecimal fatherDeptId);

    /**
     * 根据id获取部门详情
     *
     * @param departmentId
     * @return
     */
    SyDepartment getDeptById(BigDecimal departmentId);

    /**
     * 更新部门
     *
     * @param dept
     * @return
     */
    int updateDeptById(SyDepartment dept);

    /**
     * 删除部门
     *
     * @param deptId
     */
    void deleteDeptById(BigDecimal deptId);
}
