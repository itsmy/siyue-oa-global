package com.oa.organization.service.impl;

import com.oa.organization.service.DeptRecordService;
import com.oa.organization.dao.DeptRecordDao;
import com.oa.organization.entity.DeptRecord;
import com.oa.organization.service.DeptRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DeptRecordServiceImpl implements DeptRecordService {
    private final Logger logger = LoggerFactory.getLogger(DeptRecordServiceImpl.class);
    @Autowired
    private DeptRecordDao deptRecordDao;

    @Override
    public int createDept(DeptRecord deptRecord) {
        return deptRecordDao.createDept(deptRecord);
    }

    @Override
    public int deleteDept(BigDecimal deptErpId) {
        return deptRecordDao.deleteDept(deptErpId);
    }

    @Override
    public int updateDept(BigDecimal deptErpId, BigDecimal deptParentId, String deptErpName) {

        return deptRecordDao.updateDept(deptErpId, deptParentId, deptErpName);
    }

    @Override
    public DeptRecord getDept(BigDecimal deptErpId) {
        return deptRecordDao.getDept(deptErpId);
    }

    @Override
    public List<DeptRecord> getDeptList() {
        return deptRecordDao.getDeptList();
    }

    @Override
    public List<BigDecimal> getDeptIdList() {
        return deptRecordDao.getDeptIdList();
    }

    @Override
    public int insertDept(List<DeptRecord> deptList) {
        try {
            for (int i = 0; i < deptList.size(); i++) {
                DeptRecord deptRecord = deptList.get(i);
                deptRecordDao.insertDept(deptRecord);
                logger.info("insert deptRecord++++++++++++++++++++++++" + deptRecord.getErpName());
            }
            return 0;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return -1;
        }
    }

    @Override
    public int insertDept(DeptRecord deptRecord) {
        return deptRecordDao.insertDept(deptRecord);
    }
}
