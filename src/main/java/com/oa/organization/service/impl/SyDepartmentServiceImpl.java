package com.oa.organization.service.impl;

import com.oa.organization.dao.SyDepartmentDao;
import com.oa.organization.entity.DeptRecord;
import com.oa.organization.service.DeptRecordService;
import com.oa.organization.service.SyDepartmentService;
import com.oa.organization.entity.SyDepartment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SyDepartmentServiceImpl implements SyDepartmentService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SyDepartmentDao syDepartmentDao;
    @Autowired
    private DeptRecordService deptRecordService;

    @Override
    public List<SyDepartment> getDeptList() {

        return syDepartmentDao.getDepartmentList();
    }

    @Override
    public List<SyDepartment> getSubDeptListByFather(BigDecimal fatherDeptId) {
        return syDepartmentDao.getSubDeptListByFather(fatherDeptId);
    }

    /*部门的新增、修改，删除是小概率事件*/
    @Override
    public List<SyDepartment> getNewDeptList() {
        List<SyDepartment> newDeptList = new ArrayList<SyDepartment>();
        List<BigDecimal> deptIdList = getDeptIdList();
        List<BigDecimal> deptRecordIdList = deptRecordService.getDeptIdList();
        for (int i = 0; i < deptIdList.size(); i++) {
            /*主数据库中间表中有，记录表中没有的部门属于新增部门*/
            BigDecimal deptId = deptIdList.get(i);
            if (!deptRecordIdList.contains(deptId)) {
                SyDepartment department = getDeptById(deptId);
                newDeptList.add(department);
            }
        }
        return newDeptList;
    }

    /*部门的删除需要考虑主数据库更新是否出错,少于原来数据的一半，属于更新出错*/
    public List<BigDecimal> getDiscardDeptIdList() {
        List<BigDecimal> discardDeptList = new ArrayList<BigDecimal>();
        List<BigDecimal> deptIdList = getDeptIdList();
        List<BigDecimal> deptRecordIdList = deptRecordService.getDeptIdList();
        for (int i = 0; i < deptRecordIdList.size(); i++) {
            /*主数据库中中间表中有，记录表中没有的部门属于新增部门*/
            BigDecimal deptRecordId = deptRecordIdList.get(i);
            if ((deptRecordIdList.size() - deptIdList.size()) / deptRecordIdList.size() < 0.5 && !deptIdList.contains(deptRecordId)) {
                discardDeptList.add(deptRecordId);
            }
        }
        return discardDeptList;
    }

    /*获取属性更新,注意这个方法只包含部门的更新，和上述两个方法均属于独立事件，的部门集合，包括部门名称，部门的父部门*/
    public List<SyDepartment> getUpdateDeptList() {
        List<SyDepartment> updateDeptList = new ArrayList<SyDepartment>();
        List<SyDepartment> departmentList = getDeptList();
        List<DeptRecord> deptRecordList = deptRecordService.getDeptList();
        for (int i = 0; i < departmentList.size(); i++) {
            SyDepartment department = departmentList.get(i);
            BigDecimal departmentId = department.getDepartmentId();
            String departmentName = department.getDepartmentName();
            BigDecimal departmentParentId = department.getFatherDeptId();
            for (int j = 0; j < deptRecordList.size(); j++) {
                DeptRecord deptRecord = deptRecordList.get(j);
                BigDecimal deptRecordId = deptRecord.getErpId();
                String deptRecordName = deptRecord.getErpName();
                BigDecimal deptParentId = deptRecord.getParentId();
                /*部门id相等并且(部门名字不相等或部门的父id不相等)属于部门的更新*/
                if (departmentId.equals(deptRecordId) && (!departmentName.equals(deptRecordName) ||
                        !departmentParentId.equals(deptParentId))) {
                    updateDeptList.add(department);
                }
            }
        }
        return updateDeptList;
    }

    public List<BigDecimal> getDeptIdList() {
        return syDepartmentDao.getDeptIdList();
    }

    @Override
    public List<SyDepartment> getDeptListByFatherId(BigDecimal fatherDeptId) {
        return syDepartmentDao.getDeptListByFatherId(fatherDeptId);
    }

    @Override
    public SyDepartment getDeptById(BigDecimal departmentId) {
        return syDepartmentDao.getDeptById(departmentId);
    }

    @Override
    public int updateDeptById(SyDepartment dept) {
        return syDepartmentDao.updateDeptById(dept);
    }

    @Override
    public void deleteDeptById(BigDecimal deptId) {
        syDepartmentDao.deleteDeptById(deptId);
    }
}
