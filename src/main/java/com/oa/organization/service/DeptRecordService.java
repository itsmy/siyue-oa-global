package com.oa.organization.service;

import com.oa.organization.entity.DeptRecord;

import java.math.BigDecimal;
import java.util.List;

public interface DeptRecordService {
    /**
     * 创建一个DeptRecord
     *
     * @param deptRecord
     * @return
     */
    int createDept(DeptRecord deptRecord);

    /**
     * 根据deptId删除一个DeptRecord
     *
     * @param deptErpId
     * @return
     */
    int deleteDept(BigDecimal deptErpId);

    /**
     * 更新DeptRecord
     *
     * @param deptErpId
     * @param deptParentId
     * @param deptErpName
     * @return
     */
    int updateDept(BigDecimal deptErpId, BigDecimal deptParentId, String deptErpName);

    /**
     * 根据deptErpId获得DeptRecord
     *
     * @param deptErpId
     * @return
     */
    DeptRecord getDept(BigDecimal deptErpId);

    /**
     * 获取DeptRecord集合
     *
     * @return
     */
    List<DeptRecord> getDeptList();

    /**
     * 查询所有的deptId
     *
     * @return
     */
    List<BigDecimal> getDeptIdList();

    /**
     * 批量插入部门列表
     *
     * @param deptList
     * @return
     */
    int insertDept(List<DeptRecord> deptList);

    int insertDept(DeptRecord deptRecord);
}
