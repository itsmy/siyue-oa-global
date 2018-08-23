package com.oa.chatgroup.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.open.client.api.model.corp.CorpUserDetail;
import com.dingtalk.open.client.api.model.corp.CorpUserDetailList;
import com.oa.common.util.AuthUtil;
import com.oa.common.util.HttpUtil;
import com.oa.organization.entity.DeptRecord;
import com.oa.organization.entity.SyDepartment;
import com.oa.organization.exception.OApiException;
import com.oa.chatgroup.service.ChatGroupService;
import com.oa.organization.service.DeptRecordService;
import com.oa.organization.service.SyDepartmentService;
import com.oa.organization.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ChatGroupServiceImpl implements ChatGroupService {
    @Autowired
    private SyDepartmentService departmentService;
    @Autowired
    private DeptRecordService deptRecordService;
    @Autowired
    private UserService userService;
    private final Logger logger = LoggerFactory.getLogger(ChatGroupServiceImpl.class);


    public JSONObject updateChatGroup(String accessToken, JSONObject jsonParam) throws OApiException {
        try {
            return HttpUtil.httpPost("https://oapi.dingtalk.com/department/update?access_token=" + accessToken, jsonParam);
        } catch (Exception e) {
            logger.error(jsonParam.getString("id"), e.getMessage(), e);
            return null;
        }
    }

    @Override
    public int updateChatGroup(String accessToken, long departmentId, String orgDeptOwner) throws OApiException {

        JSONObject jsonParam = new JSONObject();
        /*部门id*/
        jsonParam.put("id", departmentId);
        /*开启部门群*/
        jsonParam.put("createDeptGroup", true);
        /*部门群是否包含子部门*/
        //jsonParam.put("groupContainSubDept", true);
        /*设置群主*/
        jsonParam.put("orgDeptOwner", orgDeptOwner);
        /*调用更新部门的接口*/
        JSONObject jsonObject = updateChatGroup(accessToken, jsonParam);
        if (jsonObject != null) {
            return 1;
        }
        return 0;
    }

    @Override
    public int updateChatGroupMut() throws OApiException, InterruptedException {
        List<SyDepartment> departmentList = departmentService.getDeptList();
        int num = 0;
        for (int i = 0; i < departmentList.size(); i++) {
            SyDepartment department = departmentList.get(i);
            BigDecimal departmentId = department.getDepartmentId();
            String deptLeader = String.valueOf(department.getJobNumber());
            String orgDeptOwner = "";
            DeptRecord deptRecord = deptRecordService.getDept(departmentId);
            if (deptRecord != null) {
                long deptDdId = Long.parseLong(deptRecord.getDdId());
                if (deptLeader != null && !"".equals(deptLeader) && userService.isExist(AuthUtil.getAccessToken(), deptLeader) > 0) {
                    orgDeptOwner = deptLeader;
                }
                updateChatGroup(AuthUtil.getAccessToken(), deptDdId, orgDeptOwner);
                logger.info("updateChatGroup chatGroup+++++++++++++++++++++++++++++++" + department.getDepartmentName());
                num++;
                //Thread.sleep(60);
            }
            continue;
        }
        return num;
    }

    /*    *//*如果该领导不在该部门，则把这个领导加到这个部门中*//*
    private int isExist(String accessToken, long deptDdId, String deptLeader) throws Exception {
        CorpUserDetailList corpUserDetailList = userService.getUserDetails(accessToken, deptDdId, null, null, "");
        List<CorpUserDetail> userList = corpUserDetailList.getUserlist();
        for (int i = 0; i < userList.size(); i++) {
            CorpUserDetail userDetail = userList.get(i);
            String userId = userDetail.getUserid();
            if (deptLeader.equals(userId)) {
                return 1;
            }
            break;
        }
        return 0;
    }*/
}
