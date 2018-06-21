package com.oa.organization.service;

import com.oa.organization.exception.OApiException;

public interface ChatGroupService {
    int createChatGroup(String accessToken,long departmentId,String orgDeptOwner) throws OApiException;

    int createChatGroupMut();

    /**
     *
     * @param accessToken
     * @param departmentId 部门id
     * @param orgDeptOwner 企业群群主
     */
    int updateChatGroup(String accessToken,long departmentId,String orgDeptOwner) throws OApiException;

    int updateChatGroupMut() throws OApiException;
}
