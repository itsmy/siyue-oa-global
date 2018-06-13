package com.oa.organization.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 部门记录表的实体类
 */
@Data
public class DeptRecord implements Serializable {

    private static final long serialVersionUID = -2916628672957305793L;
    private BigDecimal erpId;
    private String ddId;
    private BigDecimal parentId;
    private String erpName;
    private BigDecimal companyId;
    private String companyName;
}
