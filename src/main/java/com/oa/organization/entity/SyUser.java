package com.oa.organization.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
@Data
public class SyUser implements Serializable {
    private static final long serialVersionUID = -3043326591262930087L;
    private BigDecimal jobNumber;
    private String chName;
    private String status;
    private String sex;
    private String personalMobile;
    private String officeMobile;
    private String officeTelephone;
    private String officeAddress;
    private String personalEmail;
    private String officeEmail;
    private BigDecimal departmentId;
    private String departmentName;
    private String position;


}
