package com.oa.organization.service;

import com.dingtalk.open.client.api.model.corp.Department;
import com.dingtalk.open.client.api.model.corp.DepartmentDetail;
import com.dingtalk.open.client.common.SdkInitException;
import com.dingtalk.open.client.common.ServiceException;
import com.dingtalk.open.client.common.ServiceNotExistException;

import java.util.List;

public interface DepartmentService {
    /**
     * 获取部门列表
     *
     * @param accessToken
     * @param parentDeptId
     * @return
     */
    List<Department> listDepartments(String accessToken, String parentDeptId) throws ServiceNotExistException, SdkInitException, ServiceException;


    DepartmentDetail getDeptDetail(String accessToken, String departmentId) throws SdkInitException, ServiceNotExistException, ServiceException;

    /**
     * 创建部门
     *
     * @param accessToken
     * @param name
     * @param parentId
     * @param order
     * @param createDeptGroup
     * @return
     * @throws Exception
     */
    String createDepartment(String accessToken, String name,
                            String parentId, String order, boolean createDeptGroup) throws Exception;

    /**
     * 删除部门
     *
     * @param accessToken
     * @param id
     * @return
     */
    void deleteDepartment(String accessToken, Long id) throws Exception;

    /**
     * 更新部门
     *
     * @param accessToken
     * @param id
     * @param name
     * @param parentId
     * @return
     */
    void updateDepartment(String accessToken, long id, String name,
                          String parentId, String order, Boolean createDeptGroup,
                          boolean autoAddUser, String deptManagerUserIdList, boolean deptHiding, String deptPerimits,
                          String userPerimits, Boolean outerDept, String outerPermitDepts,
                          String outerPermitUsers, String orgDeptOwner) throws Exception;

    /**
     * 批量创建
     *
     * @throws Exception
     */
    void createDeptMut() throws Exception;

    /**
     * 批量更新
     *
     * @return
     * @throws Exception
     */
    void updateDeptMut() throws Exception;
}
