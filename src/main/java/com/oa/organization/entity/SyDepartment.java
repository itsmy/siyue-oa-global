package com.oa.organization.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class SyDepartment implements Serializable {
    private static final long serialVersionUID = 8603364068574542859L;
    private BigDecimal departmentId;
    private String departmentName;
    private BigDecimal fatherDeptId;
    private String fatherDeptName;
    private BigDecimal companyId;
    private String companyName;
    private BigDecimal jobNumber;
    private String chName;
    private String deptLevel;
    private String primeCentId;
    private String primeCentName;
    private Date beginDate;
    private Date endDate;
}
