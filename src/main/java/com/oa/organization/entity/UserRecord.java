package com.oa.organization.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * user记录表的实体类
 */
@Data
public class UserRecord implements Serializable {
    private static final long serialVersionUID = 943321573969219294L;
    private BigDecimal jobNumber;
    private String chName;
    private String personalMobile;
    private String officeMobile;
    private String officeTelephone;
    private String officeAddress;
    private String personalEmail;
    private String officeEmail;
    private BigDecimal erDeptId;
    private String departmentName;
    private String position;
}
