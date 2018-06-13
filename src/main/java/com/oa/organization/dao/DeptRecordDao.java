package com.oa.organization.dao;

import com.oa.organization.entity.DeptRecord;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface DeptRecordDao {
    /**
     * 创建一个部门记录
     *
     * @param deptRecord
     * @return
     */
    int createDept(DeptRecord deptRecord);

    /**
     * 删除一条部门记录
     *
     * @param deptErpId
     * @return
     */
    int deleteDept(BigDecimal deptErpId);

    /**
     * 更新
     *
     * @param deptErpId
     * @param deptParentId
     * @param deptErpName
     * @return
     */
    int updateDept(@Param("deptErpId") BigDecimal deptErpId, @Param("deptParentId") BigDecimal deptParentId, @Param("deptErpName") String deptErpName);

    /**
     * 获取一个DeptRecord
     *
     * @param deptErpId
     * @return
     */
    DeptRecord getDept(BigDecimal deptErpId);

    /**
     * 获取recordList集合
     *
     * @return
     */
    List<DeptRecord> getDeptList();

    /*查询所有部门的id*/
    List<BigDecimal> getSubDeptIdByParent();

    /**
     * 查询所有部门的id
     *
     * @return
     */
    List<BigDecimal> getDeptIdList();

    /**
     * 批量插入部门列表
     *
     * @param deptRecord
     * @return
     */
    int insertDept(DeptRecord deptRecord);
}
