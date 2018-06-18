package com.oa.organization.web;

import com.oa.common.enums.ResultCode;
import com.oa.common.vo.Result;
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
    public Result createDepartment() throws Exception {
        departmentService.createDeptMut();
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/department/update", method = RequestMethod.GET)
    public Result updateDepartment() throws Exception {
        departmentService.updateDeptMut();
        return new Result(ResultCode.SUCCESS);
    }
}
