package com.oa.organization.service.impl;

import com.dingtalk.open.client.api.model.corp.*;
import com.oa.organization.service.*;
import com.dingtalk.open.client.ServiceFactory;
import com.dingtalk.open.client.api.service.corp.CorpDepartmentService;
import com.dingtalk.open.client.common.SdkInitException;
import com.dingtalk.open.client.common.ServiceException;
import com.dingtalk.open.client.common.ServiceNotExistException;
import com.oa.organization.entity.DeptRecord;
import com.oa.organization.entity.SyDepartment;
import com.oa.common.util.AuthUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public CopyOnWriteArrayList<DeptRecord> targetDeptList = new CopyOnWriteArrayList();
    Map<BigDecimal, String> map = new HashMap<BigDecimal, String>();
    @Autowired
    private SyDepartmentService syDepartmentService;
    @Autowired
    private DeptRecordService deptRecordService;
    @Autowired
    private UserService userService;

    @Override
    public List<Department> listDepartments(String accessToken, String parentDeptId) throws ServiceNotExistException, SdkInitException, ServiceException {
        CorpDepartmentService corpDepartmentService = ServiceFactory.getInstance().getOpenService(CorpDepartmentService.class);
        List<Department> deptList = corpDepartmentService.getDeptList(accessToken, parentDeptId);
        return deptList;
    }

    @Override
    public DepartmentDetail getDeptDetail(String accessToken, String departmentId) throws SdkInitException, ServiceNotExistException, ServiceException {
        CorpDepartmentService corpDepartmentService = ServiceFactory.getInstance().getOpenService(CorpDepartmentService.class);
        return corpDepartmentService.getDeptDetail(accessToken, departmentId);
    }

    @Override
    public String createDepartment(String accessToken, String name, String parentId, String order, boolean createDeptGroup) throws Exception {
        CorpDepartmentService corpDepartmentService = ServiceFactory.getInstance().getOpenService(CorpDepartmentService.class);
        return corpDepartmentService.deptCreate(accessToken, name, parentId, order, createDeptGroup);
    }

    /**
     * 当需要删除的部门有子部门或者有成员的时候钉钉接口会抛出异常，这时候需要捕获异常，但是不抛出
     *
     * @param accessToken
     * @param id
     * @throws Exception
     */
    @Override
    public void deleteDepartment(String accessToken, Long id) throws Exception {
        try {
            CorpDepartmentService corpDepartmentService = ServiceFactory.getInstance().getOpenService(CorpDepartmentService.class);
            corpDepartmentService.deptDelete(accessToken, id);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void updateDepartment(String accessToken, long id, String name,
                                 String parentId, String order, Boolean createDeptGroup,
                                 boolean autoAddUser, String deptManagerUserIdList, boolean deptHiding, String deptPerimits,
                                 String userPerimits, Boolean outerDept, String outerPermitDepts,
                                 String outerPermitUsers, String orgDeptOwner) throws Exception {
        CorpDepartmentService corpDepartmentService = ServiceFactory.getInstance().getOpenService(CorpDepartmentService.class);
        corpDepartmentService.deptUpdate(accessToken, id, name, parentId, order, createDeptGroup,
                autoAddUser, deptManagerUserIdList, deptHiding, deptPerimits, userPerimits,
                outerDept, outerPermitDepts, outerPermitUsers, orgDeptOwner);
    }

    /**
     * 批量创建部门，创建完成之后需要将数据写入到erp中间表
     * sqlserver对语句的条数和插入对象的总数是有限制的分别为1000和2100，这
     *
     * @throws Exception
     */
    public void createDeptMut() throws Exception {
        /*GIS客户服务部*/
        createDept(AuthUtil.getAccessToken(), new BigDecimal("1"));
        /*里使用分批次插入的方法*/
        // List<List<DeptRecord>> resultList = averageAssign(targetDeptList, 500);
        deptRecordService.insertDept(targetDeptList);

    }

    /**
     * 调用更新部门的接口,更新部门的时候需要同时更新到钉钉和ERP中间表
     *
     * @return
     * @throws Exception
     */
    public void updateDeptMut() throws Exception {
        System.out.println("......Entering.....updateDeptMut().......");
        addDepartment(AuthUtil.getAccessToken());
        updateDepartment(AuthUtil.getAccessToken());
        delDepartment(AuthUtil.getAccessToken());
    }

    /**
     * 部门的批量增加，删除和更新，三个需要都执行，如果更新失败，会在下次更新的时候重新更新。
     *
     * @param accessToken
     * @return
     */
    private int addDepartment(String accessToken) throws Exception {
        /*增删改查是单独进行的*/
        //CopyOnWriteArrayList<DeptRecord> deptRecordList = new CopyOnWriteArrayList();
        /*获取新增的部门的集合*/
        int result = 0;
        List<SyDepartment> newDeptList = syDepartmentService.getNewDeptList();
        for (int i = 0; newDeptList.size() > 0 && i < newDeptList.size(); i++) {
            SyDepartment syDepartment = newDeptList.get(i);
            String syDepartmentName = syDepartment.getDepartmentName();
            BigDecimal deptParentId = syDepartment.getFatherDeptId();
            DeptRecord record = deptRecordService.getDept(deptParentId);
            /*如果查不到只有一种情况，即增加的部门属于其他公司*/
            if (record != null) {
                String dingTalkParentId = record.getDdId();
                /*父部门是否已存在*/
                int exist = isExist(accessToken, dingTalkParentId);
                if (exist > 0) {
                    /*这里调用的是钉钉的创建部门的接口,创建部门需要传入父部门id的*/
                    String deptBackId = createDepartment(accessToken, syDepartmentName, dingTalkParentId, "", false);
                    //Thread.sleep(60);
                    /*将新增加的部门同步到中间表*/
                    DeptRecord deptRecord = new DeptRecord();
                    deptRecord.setErpId(syDepartment.getDepartmentId());
                    deptRecord.setDdId(deptBackId);
                    deptRecord.setErpName(syDepartmentName);
                    deptRecord.setParentId(deptParentId);
                    deptRecord.setCompanyId(syDepartment.getCompanyId());
                    deptRecord.setCompanyName(syDepartment.getCompanyName());
                    result = result + deptRecordService.insertDept(deptRecord);
                }
                //deptRecordList.add(deptRecord);
                continue;
            }
        }
        return result;
    }

    /**
     * 删除部门
     * 钉钉接口规则:不能删除含有子部门、成员的部门
     * 如果要删除的部门下有子部门和成员，人员更新完成之后，第二天会自动更新部门，部门的删除会从叶子节点开始，直到把对应的部门全部删除
     *
     * @param accessToken
     * @throws Exception 异常时部门删除出错<p>
     **/
    private void delDepartment(String accessToken) throws Exception {
        List<BigDecimal> delDeptIdList = syDepartmentService.getDiscardDeptIdList();
        for (int i = 0; i < delDeptIdList.size(); i++) {
            BigDecimal syDepartmentId = delDeptIdList.get(i);
            DeptRecord deptRecord = deptRecordService.getDept(syDepartmentId);
            /*如果要删除的部门不在DeptRecord表中，则deptRecord为null*/
            if (deptRecord != null) {
                /*要删除的部门id*/
                long dingTalkId = Long.parseLong(deptRecord.getDdId());
                int exist = isExist(accessToken, String.valueOf(dingTalkId));
                if (exist > 0) {
                    /*4.删除部门d*/
                    deleteDepartment(accessToken, dingTalkId);
                    //Thread.sleep(60);
                }
                /*删除中间表中的记录表*/
                deptRecordService.deleteDept(syDepartmentId);
                continue;
            }
        }
    }

    /**
     * 部门更新,parentId虽然不是部门更新的必填字段，但是如果更新的是父部门，那么parentId就是必填字段
     *
     * @param accessToken
     * @return
     * @throws Exception
     */
    private void updateDepartment(String accessToken) throws Exception {
        /*获取更新部门的集合*/
        List<SyDepartment> updateDeptList = syDepartmentService.getUpdateDeptList();
        for (int i = 0; updateDeptList.size() > 0 && i < updateDeptList.size(); i++) {
            SyDepartment syDepartment = updateDeptList.get(i);
            String departmentName = syDepartment.getDepartmentName();
            BigDecimal syDepartmentId = syDepartment.getDepartmentId();
            BigDecimal syDeptParentId = syDepartment.getFatherDeptId();
            BigDecimal syParentId = syDepartment.getFatherDeptId();
            long dingTalkDeptId = Long.parseLong(deptRecordService.getDept(syDepartmentId).getDdId());
            String parentId = String.valueOf(deptRecordService.getDept(syParentId).getDdId());
            int exist = isExist(accessToken, String.valueOf(dingTalkDeptId));
            if (exist > 0) {
                updateDepartment(accessToken, dingTalkDeptId, departmentName, parentId, "",
                        false, false, "", false, "", "", false, "", "", "");
                //Thread.sleep(60);
                /*更新中间表中的部门*/
                deptRecordService.updateDept(syDepartmentId, syDeptParentId, departmentName);
            }
            continue;
        }
    }

    /*获取deptRecordId的集合*/
    private List<BigDecimal> getDeptRecordIdList() {
        return deptRecordService.getDeptIdList();
    }

    /**
     * id配对，所有子公司的父id都是1，即股份公司，股份公司是手动创建出来的，钉钉中它的id是1
     *
     * @param deptMap
     * @param fatherId
     * @return
     */
    private String matching(Map<BigDecimal, String> deptMap, BigDecimal fatherId) {
        if (deptMap.containsKey(fatherId)) {
            return deptMap.get(fatherId);
        }
        return "1";
    }

    /**
     * 按照层级关系创建部门,递归创建出所有部门
     */
    private void createDept(String accessToken, BigDecimal parentId) throws Exception {
        /*根据父部门查询子部门*/
        String departmentName = "";
        /*erp中部门父id*/
        try {
            List<SyDepartment> deptList = syDepartmentService.getDeptListByFatherId(parentId);
            for (int i = 0; (deptList != null && deptList.size() > 0) && i < deptList.size(); i++) {
                SyDepartment department = deptList.get(i);
                /*部门名称*/
                departmentName = department.getDepartmentName();
                /*erp中部门父id*/
                parentId = department.getFatherDeptId();
                /*部门id*/
                BigDecimal deptId = department.getDepartmentId();
                BigDecimal companyId = department.getCompanyId();
                String companyName = department.getCompanyName();
                /*获得钉钉中dept的父id*/
                String fatherId = matching(map, parentId);
                String deptBackId = createDepartment(accessToken, departmentName, fatherId, "", false);
                /*钉钉单个接口调用频率不能超过1000次/分钟*/
                Thread.sleep(60);
                /*将部门id和deptBackId以键值对的形式放进map*//**/
                map.put(deptId, deptBackId);
                /*创建部门成功之后需要将信息写到erp中间表*/
                DeptRecord deptRecord = new DeptRecord();
                deptRecord.setErpId(deptId);
                deptRecord.setDdId(deptBackId);
                deptRecord.setErpName(departmentName);
                deptRecord.setParentId(parentId);
                deptRecord.setCompanyId(companyId);
                deptRecord.setCompanyName(companyName);
                targetDeptList.add(deptRecord);
                /*递归*/
                createDept(accessToken, deptId);
            }
        } catch (Exception e) {
            logger.error("departmentName============" + departmentName + "++++++++++++++" + parentId);
        }
    }

    /*根据id查询部门是否存在，1，存在，0不存在，-1异常不存在*/
    private int isExist(String accessToken, String departmentId) {
        try {
            DepartmentDetail departmentDetail = getDeptDetail(accessToken, departmentId);
            if (departmentDetail != null) {
                return 1;
            }
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * sqlserver对语句的条数和插入对象的总数是有限制的分别为1000和2100，这里使用分批次插入的方法
     *
     * @param list
     * @return
     */
    /**
     * 将一个list均分成n个list,还可使用java8提供的Lambda表达式来实现
     *
     * @param source
     * @return
     */
/*    private <T> List<List<T>> averageAssign(List<T> source, int n) {
        List<List<T>> result = new ArrayList<List<T>>();
        int remaider = source.size() % n;  //(先计算出余数)
        int number = source.size() / n;  //然后是商
        int offset = 0;//偏移量
        for (int i = 0; i < n; i++) {
            List<T> value = null;
            if (remaider > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remaider--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }*/



    /*    *//**
     * 这里有三种情况:第一种新增，第二种删除，第三种更改
     * 判断两个部门集合是否相等，由于部门数目众多，所以直接从数据库中取出部门id进行比较
     *
     * @param accessToken
     * @param deptIdList
     * @param deptRecordIdList
     * @return
     *//*
    public boolean updateDetails(String accessToken, List<BigDecimal> deptIdList, List<BigDecimal> deptRecordIdList) throws Exception {
        boolean result = false;
        try {
            *//*只包含更新*//*
            List<SyDepartment> departmentList = syDepartmentService.getDeptList();
            List<DeptRecord> deptRecordList = deptRecordService.getDeptList();
            if (deptIdList.size() == deptRecordIdList.size() && deptIdList.containsAll(deptRecordIdList)) {
                result = updateDepartment(accessToken, departmentList, deptRecordList);
                *//*包含增加和删除*//*
            } else {
                deptIdList.removeAll(deptRecordIdList);
                deptRecordIdList.removeAll(deptIdList);
                //主数据库中有而中间表中没有的数据
                if (deptIdList.size() > 0) {
                    result = addDepartment(accessToken, deptIdList, departmentList);
                }
                //中间表中有而主数据库中没有，并且增量大于50%，主数据库中数据同步出错
                if (deptRecordIdList.size() > 0 && (deptRecordIdList.size() / deptIdList.size() < 0.5)) {
                    result = deleteDepartment(accessToken, deptRecordIdList, deptRecordList);
                }
            }
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }
    *//**
     * 更新部门之——新增部门
     *
     * @param accessToken
     * @param deptIdList
     * @param departmentList
     * @return
     *//*
    private boolean addDepartment(String accessToken, List<BigDecimal> deptIdList, List<SyDepartment> departmentList) {
        boolean result = false;
        try {
            for (int i = 0; i < deptIdList.size(); i++) {
                for (int j = 0; j < departmentList.size(); j++) {
                    BigDecimal departmentId = deptIdList.get(i);
                    SyDepartment syDepartment = departmentList.get(j);
                    //主数据库中的部门id
                    BigDecimal deptId = syDepartment.getDepartmentId();
                    if (departmentId.equals(deptId)) {
                        //主数据库中的父部门id
                        BigDecimal parentId = syDepartment.getFatherDeptId();
                        //主数据库中的部门名称
                        String departmentName = syDepartment.getDepartmentName();
                        //调用本地接口。查出钉钉返回的父部门的id
                        BigDecimal companyId = syDepartment.getCompanyId();
                        String companyName = syDepartment.getCompanyName();
                        DeptRecord deptRecord = deptRecordService.getDept(parentId);
                        if (deptRecord != null) {
                            String deptParentBackId = deptRecord.getDdId().toString();
                            //调用钉钉接口创建部门
                            String deptBackId = createDepartment(accessToken, departmentName, deptParentBackId, "", false);
                            Thread.sleep(1);
                            deptRecord = new DeptRecord();
                            deptRecord.setErpId(deptId);
                            deptRecord.setErpName(departmentName);
                            deptRecord.setParentId(parentId);
                            deptRecord.setDdId(deptBackId);
                            deptRecord.setCompanyId(companyId);
                            deptRecord.setCompanyName(companyName);
                            //在记录表中创建出这条数据
                            deptRecordService.createDept(deptRecord);
                        }
                    }
                }
                result = true;
            }
        } catch (OApiException e) {
            logger.error(e.getMessage(), e);
            result = false;
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
            result = false;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = false;
        } finally {
            return result;
        }
    }

    *//**
     * 更新部门之——删除部门
     *
     * @param accessToken
     * @param deptRecIdList
     * @param deptRecordList
     * @return
     *//*
    private boolean deleteDepartment(String accessToken, List<BigDecimal> deptRecIdList, List<DeptRecord> deptRecordList) {
        try {
            for (int i = 0; i < deptRecIdList.size(); i++) {
                for (int j = 0; j < deptRecordList.size(); j++) {
                    BigDecimal deptRecordId = deptRecIdList.get(i);
                    DeptRecord deptRecord = deptRecordList.get(j);
                    BigDecimal departmentId = deptRecord.getErpId();
                    if (deptRecordId.equals(departmentId)) {
                        //先查询
                        String deptDingTalkId = deptRecordService.getDept(departmentId).getDdId().toString().toString();
                        //调用删除的接口
                        deleteDepartment(accessToken, Long.parseLong(deptDingTalkId));
                        Thread.sleep(1);
                        //删除记录表中的数据
                        deptRecordService.deleteDept(departmentId);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    *//**
     * 更新部门之——update
     *
     * @param accessToken
     * @param departmentList
     * @param deptRecordList
     * @return
     *//*
    private boolean updateDepartment(String accessToken, List<SyDepartment> departmentList, List<DeptRecord> deptRecordList) {
        try {
            for (int i = 0; i < departmentList.size(); i++) {
                for (int j = 0; j < deptRecordList.size(); j++) {
                    SyDepartment syDepartment = departmentList.get(i);
                    DeptRecord deptRecord = deptRecordList.get(j);
                    BigDecimal departmentId = syDepartment.getDepartmentId();
                    String departmentName = syDepartment.getDepartmentName();
                    BigDecimal parentId = syDepartment.getFatherDeptId();
                    if (departmentId.equals(deptRecord.getErpId())) {
                        if (departmentName.equals(deptRecord.getErpName())
                                && parentId.equals(deptRecord.getParentId())) {
                            continue;
                        }
                        //先查出来父部门department
                        DeptRecord departParentRecord = deptRecordService.getDept(parentId);
                        //调用钉钉更新接口，单个更新
                        if (departParentRecord != null) {
                            //再查出来父部门对应的钉钉id
                            String dingTalkParentId = String.valueOf(departParentRecord.getDdId());
                            //需要更新的部门对应的钉钉id
                            long dingTalkDeptId = Long.parseLong(String.valueOf(deptRecord.getDdId()));
                            //更新部门
                            updateDepartment(accessToken, dingTalkDeptId, departmentName, dingTalkParentId, "",
                                    false, false, "", false, "", "", false, "", "", "");
                            Thread.sleep(1);
                            //调用本地更新接口，单个更新
                            deptRecordService.updateDept(departmentId, parentId, departmentName);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
        /**
     * 设值
     *
     * @param syDepartment
     * @param deptBackId
     * @return
     */
/*    private DeptRecord setAttribute(SyDepartment syDepartment, String deptBackId) {
        DeptRecord deptRecord = new DeptRecord();
        //部门id
        deptRecord.setErpId(syDepartment.getCompanyId());
        //从钉钉接口返回的部门id
        deptRecord.setDdId(deptBackId);
        //部门的父id
        deptRecord.setParentId(syDepartment.getFatherDeptId());
        //部门名称
        deptRecord.setErpName(syDepartment.getDepartmentName());
        deptRecord.setCompanyId(syDepartment.getCompanyId());
        deptRecord.setCompanyName(syDepartment.getCompanyName());
        return deptRecord;
    }*/
/*    public List<Department> getDepartments(String accessToken, String parentId) throws SdkInitException, ServiceException, ServiceNotExistException {
        List<Department> departments = listDepartments(accessToken, String.valueOf(parentId));
        if (departments != null && !departments.isEmpty()) {
            for (int i = 0; i < departments.size(); i++) {
                return getDepartments(accessToken, parentId);
            }
        }
        return null;
    }*/
    /*    *//*判断更新的部门是否在DEPR_RECORD表中*//*
    private boolean containsId(List<BigDecimal> deptIdList, List<SyDepartment> deptList) {
        for (int i = 0; i < deptList.size(); i++) {
            if (deptIdList.contains(deptList.get(i).getDepartmentId())) {
                return true;
            }
        }
        return false;
    }*/
}
