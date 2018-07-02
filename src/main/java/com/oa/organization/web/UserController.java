package com.oa.organization.web;

import com.alibaba.fastjson.JSONArray;
import com.dingtalk.open.client.api.model.corp.CorpUserDetail;
import com.oa.common.enums.ResultCode;
import com.oa.common.vo.Result;
import com.oa.organization.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/organization")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserServiceImpl userService;

    /**
     * 创建用户
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/user/create", method = RequestMethod.GET)
    public Result createUser() throws Exception {
        logger.info("Entering...createUser....................................");
        List<String> userIdList = userService.createUserMut();
        logger.info("Existing...createUser....................................");
        return new Result(ResultCode.SUCCESS, userIdList);
    }

    /**
     * 更新用户
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/user/update", method = RequestMethod.GET)
    public Result updateUser() throws Exception {
        logger.info("Entering...updateUser....................................");
        JSONArray updateUserArray = userService.updateUserMut(1);
        logger.info("Existing...updateUser......................................");
        return new Result(ResultCode.SUCCESS, updateUserArray);

    }

    @RequestMapping(value = "/user/compare", method = RequestMethod.GET)
    public Result compareUser() throws Exception {
        logger.info("Entering...compareUser........................................");
        List<CorpUserDetail> invalidUserList = userService.compareUser();
        logger.info("Existing...compareUser........................................");
        return new Result(ResultCode.SUCCESS, invalidUserList);
    }
    /*    */

    /**
     * 登录,成功后反回人员组织架构的信息
     *
     * @param
     * @param
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
/*    @GetMapping("/user/login")
    public Map<String, String> login() {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("msg", "登录成功");
        return hashMap;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public Result loginNp() {
        return new Result(ResultCode.SUCCESS);
    }*/
}
