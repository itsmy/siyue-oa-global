package com.oa.organization.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.oa.common.util.AuthUtil;
import com.oa.common.util.HttpUtil;
import com.oa.organization.entity.SyDepartment;
import com.oa.organization.exception.OApiException;
import com.oa.organization.service.ChatGroupService;
import com.oa.organization.service.DeptRecordService;
import com.oa.organization.service.SyDepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class ChatGroupServiceImpl implements ChatGroupService {
    @Autowired
    private SyDepartmentService departmentService;
    @Autowired
    private DeptRecordService deptRecordService;
    private final Logger logger = LoggerFactory.getLogger(ChatGroupServiceImpl.class);

    @Override
    public int createChatGroup(String accessToken, long departmentId, String orgDeptOwner) throws OApiException {
        /*        JSONObject jsonParam = new JSONObject();
         *//*部门id*//*
        jsonParam.put("id", "68640060");
        *//*开启部门群*//*
        jsonParam.put("createDeptGroup", true);
        *//*部门群是否包含子部门*//*
        jsonParam.put("groupContainSubDept", true);
        *//*部门群群主*//*
        jsonParam.put("orgDeptOwner", "203478");
        *//*调用更新部门的接口*//*
        JSONObject jsonObject = updateChatGroup(accessToken, jsonParam);
        if (jsonObject != null) {
            return 1;
        }
        return 0;*/
        return 0;
    }

    public JSONObject updateChatGroup(String accessToken, JSONObject jsonParam) throws OApiException {
        try {
            return HttpUtil.httpPost("https://oapi.dingtalk.com/department/update?access_token=" + accessToken, jsonParam);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public int createChatGroupMut() {
        return 0;
    }

    @Override
    public int updateChatGroup(String accessToken, long departmentId, String orgDeptOwner) throws OApiException {
        JSONObject jsonParam = new JSONObject();
        /*部门id*/
        jsonParam.put("id", "68640060");
        /*开启部门群*/
        jsonParam.put("createDeptGroup", true);
        /*部门群是否包含子部门*/
        jsonParam.put("groupContainSubDept", true);
        /*部门群群主*/
        jsonParam.put("orgDeptOwner", "203478");
        /*调用更新部门的接口*/
        JSONObject jsonObject = updateChatGroup(accessToken, jsonParam);
        if (jsonObject != null) {
            return 1;
        }
        return 0;
    }

    @Override
    public int updateChatGroupMut() throws OApiException {
        List<SyDepartment> departmentList = departmentService.getDeptList();
        int num = 0;
        for (int i = 0; i < departmentList.size(); i++) {
            SyDepartment department = departmentList.get(i);
            BigDecimal departmentId = department.getDepartmentId();
            String deptLeader = String.valueOf(department.getJobNumber());
            String deptDdId = deptRecordService.getDept(departmentId).getDdId();
            if (deptLeader != null && deptDdId != null && !"".equals(deptLeader) && !"".equals(deptDdId)) {
                num = num + updateChatGroup(AuthUtil.getAccessToken(), Long.parseLong(deptDdId), deptLeader);
            }
            continue;
        }
        return num;
    }
}
