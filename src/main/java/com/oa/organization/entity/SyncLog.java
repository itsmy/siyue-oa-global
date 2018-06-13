package com.oa.organization.entity;

import lombok.Data;

import java.util.Date;

/*日志对象*/
@Data
public class SyncLog {
    private String modelName;
    private String tableName;
    private Date syncDate;
    private String syncStatu;

}
