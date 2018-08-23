package com.oa.organization.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * job_number nvarchar ( 8 ),
 * ch_name nvarchar ( 40 ),
 * sex nvarchar(2),
 * personal_mobile nvarchar ( 30 ),
 * office_mobile nvarchar ( 30 )
 */
@Data
public class SyExcludeUser implements Serializable {
    private static final long serialVersionUID = 7199553447366470322L;
    private String jobNumber;
    private String chName;
    private String sex;
    private String personalMobile;
    private String officeMobile;
}
