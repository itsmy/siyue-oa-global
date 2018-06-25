package com.oa.organization.service.impl;

import com.dingtalk.open.client.api.model.corp.*;
import com.oa.organization.entity.UserRecord;
import com.oa.organization.exception.OApiException;
import com.oa.organization.service.UserRecordService;
import com.oa.organization.service.UserService;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.open.client.ServiceFactory;
import com.dingtalk.open.client.api.service.corp.CorpUserService;
import com.oa.organization.entity.DeptRecord;
import com.oa.organization.entity.SyUser;
import com.oa.organization.service.DeptRecordService;
import com.oa.organization.service.SyUserService;
import com.oa.common.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SyUserService syUserService;
    @Autowired
    private DeptRecordService deptRecordService;
    @Autowired
    private UserRecordService userRecordService;

    @Override
    public CorpUserBaseInfo getUserInfo(String accessToken, String code) throws Exception {
        CorpUserService corpUserService = ServiceFactory.getInstance().getOpenService(CorpUserService.class);
        return corpUserService.getUserinfo(accessToken, code);
    }

    @Override
    public String createUser(String accessToken, CorpUserDetail userDetail) throws Exception {
        CorpUserService corpUserService = ServiceFactory.getInstance().getOpenService(CorpUserService.class);
        JSONObject js = (JSONObject) JSONObject.parse(userDetail.getOrderInDepts());
        Map<Long, Long> orderInDepts = FileUtils.toHashMap(js);

        String userId = corpUserService.createCorpUser(accessToken, userDetail.getUserid(), userDetail.getName(), orderInDepts,
                userDetail.getDepartment(), userDetail.getPosition(), userDetail.getMobile(), userDetail.getTel(), userDetail.getWorkPlace(),
                userDetail.getRemark(), userDetail.getEmail(), userDetail.getJobnumber(),
                userDetail.getIsHide(), userDetail.getSenior(), userDetail.getExtattr());

        // 员工唯一标识ID
        return userId;
    }

    @Override
    public void updateUser(String accessToken, CorpUserDetail userDetail) throws Exception {
        CorpUserService corpUserService = ServiceFactory.getInstance().getOpenService(CorpUserService.class);
        JSONObject js = (JSONObject) JSONObject.parse(userDetail.getOrderInDepts());
        Map<Long, Long> orderInDepts = FileUtils.toHashMap(js);

        corpUserService.updateCorpUser(accessToken, userDetail.getUserid(), userDetail.getName(), orderInDepts,
                userDetail.getDepartment(), userDetail.getPosition(), userDetail.getMobile(), userDetail.getTel(), userDetail.getWorkPlace(),
                userDetail.getRemark(), userDetail.getEmail(), userDetail.getJobnumber(),
                userDetail.getIsHide(), userDetail.getSenior(), userDetail.getExtattr());
    }

    @Override
    public void deleteUser(String accessToken, String userId) throws Exception {
        CorpUserService corpUserService = ServiceFactory.getInstance().getOpenService(CorpUserService.class);
        corpUserService.deleteCorpUser(accessToken, userId);
    }

    @Override
    public CorpUserDetail getUser(String accessToken, String userId) throws Exception {
        CorpUserService corpUserService = ServiceFactory.getInstance().getOpenService(CorpUserService.class);
        return corpUserService.getCorpUser(accessToken, userId);
    }

    @Override
    public void batchDeleteUser(String accessToken, List<String> userIdList) throws Exception {
        CorpUserService corpUserService = ServiceFactory.getInstance().getOpenService(CorpUserService.class);
        corpUserService.batchdeleteCorpUserListByUserids(accessToken, userIdList);
    }

    @Override
    public CorpUserList getDepartmentUser(String accessToken, long departmentId, Long offset, Integer size, String order) throws Exception {
        CorpUserService corpUserService = ServiceFactory.getInstance().getOpenService(CorpUserService.class);
        return corpUserService.getCorpUserSimpleList(accessToken, departmentId,
                offset, size, order);
    }

    @Override
    public CorpUserDetailList getUserDetails(String accessToken, long departmentId, Long offset, Integer size, String order) throws Exception {
        CorpUserService corpUserService = ServiceFactory.getInstance().getOpenService(CorpUserService.class);
        return corpUserService.getCorpUserList(accessToken, departmentId,
                offset, size, order);
    }

    @Override
    public JSONObject getAgentUserInfo(String ssoToken, String code) throws OApiException {
        String url = Env.OAPI_HOST + "/sso/getuserinfo?" + "access_token=" + ssoToken + "&code=" + code;
        JSONObject response = HttpUtil.httpGet(url);
        return response;
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public List<SyUser> getUserList() {
        return syUserService.getUserList();
    }

    /**
     * 批量创建user
     *
     * @throws Exception
     */
    public void createUserMut() throws Exception {
        String accessToken = AuthUtil.getAccessToken();
        List<SyUser> userList = getUserList();
        List<DeptRecord> deptList = deptRecordService.getDeptList();
        createUser(accessToken, userList, deptList);

    }

    /**
     * 批量更新。包括增删改
     *
     * @return
     */
    public void updateUserMut(int dateDiffer) throws Exception {
        List<DeptRecord> deptRecordList = deptRecordService.getDeptList();
        addUser(AuthUtil.getAccessToken(), dateDiffer, deptRecordList);
        updateUser(AuthUtil.getAccessToken(), dateDiffer, deptRecordList);
        deleteUser(AuthUtil.getAccessToken(), dateDiffer, deptRecordList);
    }

    /**
     * 批量创建成员
     *
     * @param accessToken
     * @param userList
     * @param deptRecordList
     */
    private void createUser(String accessToken, List<SyUser> userList, List<DeptRecord> deptRecordList) throws Exception {
        for (int i = 0; i < deptRecordList.size(); i++) {
            DeptRecord deptRecord = deptRecordList.get(i);
            BigDecimal erpId = deptRecord.getErpId();
            String departmentId = deptRecord.getDdId();
            for (int j = 0; j < userList.size(); j++) {
                SyUser user = userList.get(j);
                String userId = String.valueOf(user.getJobNumber());
                BigDecimal userDeptId = user.getDepartmentId();
                if (userDeptId.equals(erpId)) {
                    /*为user设置属性，并批量创建user*/
                    int exist = isExist(accessToken, userId);
                    /*userId是否在钉钉中已存在*/
                    if (exist <= 0) {
                        CorpUserDetail userDetail = setUserDetail(user, departmentId);
                        /*erp中人员的手机号码是否为空*/
                        if (userDetail != null) {
                            createUser(accessToken, userDetail);
                            Thread.sleep(60);
                        }
                    }
                }
            }
        }
    }


    /**
     * 创建user
     *
     * @throws Exception
     */
    private void addUser(String accessToken, int dateDiffer, List<DeptRecord> deptRecordList) throws Exception {
        List<SyUser> newUserList = syUserService.getNewUserList(dateDiffer);
        for (int i = 0; newUserList.size() > 0 && i < newUserList.size(); i++) {
            SyUser user = newUserList.get(i);
            BigDecimal departmentId = user.getDepartmentId();
            String userId = String.valueOf(user.getJobNumber());
            /*如果钉钉端已经存在了此id，则不创建该用户*/
            int exist = isExist(accessToken, userId);
            if (exist <= 0) {
                for (int j = 0; j < deptRecordList.size(); j++) {
                    DeptRecord deptRecord = deptRecordList.get(j);
                    BigDecimal deptErpId = deptRecord.getErpId();
                    if (deptErpId.equals(departmentId)) {
                        String deptDdId = deptRecord.getDdId();
                        CorpUserDetail userDetail = setUserDetail(user, deptDdId);
                        /*erp中人员的手机号码是否为空*/
                        if (userDetail != null) {
                            createUser(accessToken, userDetail);
                            Thread.sleep(60);
                            logger.info("create users++++++++++++++++++++++++++++++++++++++" + user.getChName());
                        }
                    }
                }
            }
            continue;
        }
    }

    /**
     * 批量删除user,离职人员某天的数量超过1000个被认为是数据异常,正常情况是100个到300个之间
     *
     * @param accessToken
     * @param deptRecordList
     * @throws Exception
     */
    private void deleteUser(String accessToken, int dateDiffer, List<DeptRecord> deptRecordList) throws Exception {
        List<SyUser> leaveUserList = syUserService.getLeaveUserList(dateDiffer);
        for (int j = 0; leaveUserList.size() < 1000 && j < leaveUserList.size(); j++) {
            SyUser user = leaveUserList.get(j);
            /*删除的人员必须存在于钉钉组织架构中，并且需要在记录表中*/
            int exist = isExist(accessToken, String.valueOf(user.getJobNumber()));
            if (exist > 0) {
                for (int i = 0; i < deptRecordList.size(); i++) {
                    DeptRecord deptRecord = deptRecordList.get(i);
                    BigDecimal deptErpId = deptRecord.getErpId();
                    if (deptErpId.equals(user.getDepartmentId())) {
                        String userId = user.getJobNumber().toString();
                        deleteUser(accessToken, userId);
                        Thread.sleep(60);
                        logger.info("delete users ++++++++++++++++++" + user.getChName());
                    }
                }
            }
            continue;
        }
    }

    /**
     * 批量修改,人员信息发生了变化,如果该员工不在钉钉中且手机号码非空真实，则调用创建员工的接口，如果该员工在钉钉中，则更新
     *
     * @param accessToken
     * @param deptRecordList
     */
    private void updateUser(String accessToken, int dateDiffer, List<DeptRecord> deptRecordList) throws Exception {
        List<SyUser> updateUserList = syUserService.getUpdateUserList(dateDiffer);
        for (int j = 0; updateUserList.size() > 0 && j < updateUserList.size(); j++) {
            SyUser user = updateUserList.get(j);
            for (int i = 0; i < deptRecordList.size(); i++) {
                DeptRecord deptRecord = deptRecordList.get(i);
                BigDecimal deptErpId = deptRecord.getErpId();
                if (deptErpId.equals(user.getDepartmentId())) {
                    long deptDdId = Long.parseLong(deptRecord.getDdId());
                    int exist = isExist(accessToken, String.valueOf(user.getJobNumber()));
                    if (exist > 0) {
                        List<Long> departmentIdList = new ArrayList<Long>();
                        departmentIdList.add(deptDdId);
                        CorpUserDetail dingTalkUser = getUser(accessToken, user.getJobNumber().toString());
                        dingTalkUser.setName(user.getChName());
                        dingTalkUser.setEmail(user.getOfficeEmail());
                        dingTalkUser.setDepartment(departmentIdList);
                        dingTalkUser.setPosition(user.getPosition());
                        //调用钉钉员工接口
                        updateUser(accessToken, dingTalkUser);
                        Thread.sleep(60);
                        logger.info("update user++++++++++++++++++++++++" + user.getChName());
                    } else if (exist < 0 && !user.getOfficeMobile().equals("")) {
                        CorpUserDetail userDetail = setUserDetail(user, String.valueOf(deptDdId));
                        createUser(accessToken, userDetail);
                        logger.info("add user+++++++++++++++++++++++++++++++++++" + user.getChName());
                    }
                }
                continue;
            }
        }
    }

    /**
     * 设置属性
     *
     * @param user
     */
    private CorpUserDetail setUserDetail(SyUser user, String deptBackId) {
        //如果人员所属的部门和记录表中的部门id相同才创建，没有就返回null
        CorpUserDetail userDetail = new CorpUserDetail();
        //主部门只有一个id,如果有多个则需要放开长度
        List<Long> deptList = new ArrayList<Long>(1);
        deptList.add(Long.parseLong(deptBackId));
        //为钉钉用户设置姓名
        userDetail.setName(user.getChName());
        //ERP系统的人员id可以直接当作userId
        userDetail.setUserid(user.getJobNumber().toString());
        //设置部门
        userDetail.setDepartment(deptList);
        /*办公手机*/
        String mobile = user.getOfficeMobile();
        //获取个人手机
        String personalPhone = user.getPersonalMobile();
        //办公室电话
        String personalCall = user.getOfficeTelephone();
        //有一部分人的手机号码是不规范的，有汉字，有特殊符号

        /*手机号码是否隐藏*/
        userDetail.setIsHide(false);
        //工号
        userDetail.setJobnumber(user.getJobNumber().toString());
        //邮箱
        userDetail.setEmail(user.getOfficeEmail().toString());
        /*职位*/
        userDetail.setPosition(user.getPosition());
        /*只识别办公手机号码*/
/*        if (mobile.equals("") || !isMobile(mobile)) {
            mobile = personalPhone;
        }*/
        if (isMobile(mobile)) {
            userDetail.setMobile(mobile);
            return userDetail;
        }
        return null;
    }

    public boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断手机号码是否有效
     *
     * @param str
     * @return
     */
    public boolean isMobile(final String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8,9][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /*userId是否已经存在，如果是前台加入的用户，1,已存在，0不存在，-1不存在并报异常*/
    private int isExist(String accessToken, String userId) {
        try {
            CorpUserDetail corpUserDetail = getUser(accessToken, userId);
            if (corpUserDetail != null) {
                return 1;
            }
            return 0;
        } catch (Exception e) {
            //logger.error("error", e);
            return -1;
        }
    }

    /**
     * 登录，已经经过前端和Interceptor验证，这里直接进行登录的信息。
     *
     * @param jsonObject
     * @return
     */
    public String login(JSONObject jsonObject) {
        String token = "";
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");

        //加密并且写session，本应该查询数据库验证，这里为了方便，写死用户名和密码
        if ("202754".equals(username) && "qq565600".equals(password)) {
            //使用spring自带的加密工具进行加密
            token = DigestUtils.md5DigestAsHex((username + password).getBytes());
        }
        return token;
    }
    /*
     *//*成员更新异常时调用次接口*//*
    public boolean reUpdateUsers(String accessToken) throws Exception {
        try {
            List<SyUser> userList = syUserService.getUserList();
            List<BigDecimal> userRecordIdList = userRecordService.getAllUserIdList();
            for (int i = 0; i < userList.size(); i++) {
                SyUser user = userList.get(i);
                BigDecimal userId = user.getJobNumber();
                if (!userRecordIdList.contains(userId)) {
                    *//*创建成员*//*
                    BigDecimal departmentId = user.getDepartmentId();
                    DeptRecord deptRecord = deptRecordService.getDept(departmentId);
                    String ddId = deptRecord.getDdId();
                    CorpUserDetail corpUserDetail = setUserDetail(user, ddId);
                    createUser(accessToken, corpUserDetail);
                    UserRecord userRecord = new UserRecord();
                    userRecord.setJobNumber(user.getJobNumber());
                    userRecord.setChName(user.getChName());
                    userRecord.setPersonalMobile(user.getPersonalMobile());
                    userRecord.setOfficeMobile(user.getOfficeMobile());
                    userRecord.setOfficeTelephone(user.getOfficeTelephone());
                    userRecord.setOfficeAddress(user.getOfficeAddress());
                    userRecord.setPersonalEmail(user.getPersonalEmail());
                    userRecord.setOfficeEmail(user.getOfficeEmail());
                    userRecord.setErDeptId(user.getDepartmentId());
                    userRecord.setDepartmentName(user.getDepartmentName());
                    userRecord.setPosition(user.getPosition());
                    *//*更新记录表*//*
                    userRecordService.createUser(userRecord);
                }
            }
            *//*删除成员*//*
            List<BigDecimal> userIdList = syUserService.getAllUserIdList();
            for (int i = 0; i < userRecordIdList.size(); i++) {
                BigDecimal recordId = userRecordIdList.get(i);
                if (!userIdList.contains(recordId)) {
                    deleteUser(accessToken, String.valueOf(recordId));
                    userRecordService.deleteUser(String.valueOf(recordId));
                }
            }
            *//*更改成员*//*
            List<UserRecord> userRecordList = userRecordService.getAllUserList();
            for (int i = 0; i < userRecordList.size(); i++) {
                UserRecord userRecord = userRecordList.get(i);
                BigDecimal userIdRecord = userRecord.getJobNumber();
                for (int j = 0; j < userList.size(); j++) {
                    SyUser user = userList.get(j);
                    BigDecimal userId = user.getJobNumber();
                    if (userIdRecord.equals(userId) && (!userRecord.getChName().equals(user.getChName()) || !userRecord.getOfficeEmail()
                            .equalsIgnoreCase(user.getOfficeEmail()) || !userRecord.getErDeptId().equals(user.getDepartmentId()))) {
                        CorpUserDetail dingTalkUser = getUser(accessToken, user.getJobNumber().toString());
                        dingTalkUser.setName(user.getChName());
                        dingTalkUser.setEmail(user.getOfficeEmail());
                        List<Long> departmentIdList = new ArrayList<Long>();
                        long departmentId = Long.parseLong(user.getDepartmentId().toString());
                        departmentIdList.add(departmentId);
                        dingTalkUser.setDepartment(departmentIdList);
                        //调用钉钉员工接口
                        updateUser(accessToken, dingTalkUser);
                        userRecord.setChName(user.getChName());
                        userRecord.setErDeptId(user.getDepartmentId());
                        userRecord.setOfficeEmail(user.getOfficeEmail());
                        userRecordService.updateUser(userRecord);
                    }
                }

            }
            return true;
        } catch (Exception e) {
            *//*这里需要返回特定的异常*//*
            throw new Exception("please contacts administrator !");
        }
    }*/
}
