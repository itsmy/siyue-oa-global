package com.oa.organization.dao;

import com.oa.organization.entity.SyExcludeUser;
import com.oa.organization.entity.SyUser;

import java.math.BigDecimal;
import java.util.List;

/*对排除人员列表的操作dao*/
public interface SyExcludeUserDao {
    List<SyExcludeUser> getExcludeUserList();

    List<String> getExcludeUserIdList();

    int updateExcludeUser(SyExcludeUser excludeUser);

    void deleteExcludeUser(BigDecimal excludeUserId);
}
