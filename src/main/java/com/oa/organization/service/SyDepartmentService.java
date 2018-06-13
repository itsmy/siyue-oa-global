package com.oa.organization.service;

import com.oa.organization.entity.SyDepartment;

import java.math.BigDecimal;
import java.util.List;

/**
 * 对于service层，应该和dao层的接口有所区别，这是直接面向用户设计的接口
 */
public interface SyDepartmentService {
    /**
     * 获取部门列表接口
     *
     * @return
     */
    List<SyDepartment> getDeptList();

    /*获取父部门下的所有子部门，含递归*/
    List<SyDepartment> getSubDeptListByFather(BigDecimal fatherDeptId);

    /*获取新增的部门列表*/
    List<SyDepartment> getNewDeptList();

    /*获取废弃的部门列表*/
    List<BigDecimal> getDiscardDeptIdList();

    /*获取属性更新的部门列表*/
    List<SyDepartment> getUpdateDeptList();

    /**
     * 根据父部门id查询部门，不包含递归
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
