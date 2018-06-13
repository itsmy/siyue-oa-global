package com.oa.organization.web;

import com.oa.common.util.AuthUtil;
import com.oa.common.util.DateUtil;
import com.oa.organization.service.SyDepartmentService;
import com.oa.organization.service.SyUserService;
import com.oa.organization.service.SyncLogService;
import com.oa.organization.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/organization")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SyDepartmentService syDepartmentService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private SyncLogService syncLogService;

    /**
     * 创建用户
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/user/create", method = RequestMethod.GET)
    public String createUser() throws Exception {
        try {
            userService.createUserMut();
            return "success";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "error";
        }
    }

    /**
     * 更新用户
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/user/update", method = RequestMethod.GET)
    public String updateUser() throws Exception {
        try {
            syncLogService.insertLog();
            userService.updateUserMut(1);
            syncLogService.updateLog("S");
            return "success";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            /*异常情况需要获取最后一次更新成功的时间*/
            syncLogService.insertLog();
            Date recordDate = syncLogService.getLatestSuccessLog();
            if (recordDate != null) {
                Date nowDate = new Date();
                int differ = DateUtil.dateDiffer(recordDate, nowDate);
                userService.updateUserMut(differ);
                syncLogService.updateLog("S");
                return "success";
            }
            return "error";
        }
    }

    /*    *//**
     * 登录,成功后反回人员组织架构的信息
     * @param jsonObject
     * @param httpServletRequest
     * @return
     *//*
    @RequestMapping(value = "/user/login",method = RequestMethod.POST)
    public JSONObject login(@RequestBody JSONObject jsonObject, HttpServletRequest httpServletRequest) throws Exception {
        JSONObject json = null;
        try {
            String token = userService.login(jsonObject);
            //写session，免登录在后面看，需要实现Spring的AOP功能。
            if (!"".equals(token)) {
                HttpSession session = httpServletRequest.getSession();
                //写session,并设置最大有效时间为30分钟
                session.setAttribute("token", token);
                session.setMaxInactiveInterval(30 * 60);
                List<SyDepartment> departmentList = syDepartmentService.getDeptList();
                List<SyUser> userList = syUserService.getUserList();
                json.put("departmentList",departmentList);
                json.put("userList",userList);
            }
        }catch( Exception e){
            e.getMessage();
        }
        return json;
    }*/
}
