package com.oa.organization.web;

import com.oa.organization.service.impl.DepartmentServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/organization")
public class DepartmentController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private DepartmentServiceImpl departmentService;

    //为了方便测试先写全部写成get接口
    @RequestMapping(value = "/department/create", method = RequestMethod.GET)
    public String createDepartment() throws Exception {
        try {
            departmentService.createDeptMut();
            return "ok";
        } catch (Exception e) {
            return "sorry";
        }
    }

    @RequestMapping(value = "/department/update", method = RequestMethod.GET)
    public String updateDepartment() throws Exception {
        try {
            departmentService.updateDeptMut();
            return "ok";
        } catch (Exception e) {
            return "sorry";
        }
    }
}
