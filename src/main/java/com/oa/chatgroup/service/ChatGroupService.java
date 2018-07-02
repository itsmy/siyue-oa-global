package com.oa.chatgroup.service;

import com.oa.organization.exception.OApiException;
import org.springframework.stereotype.Service;

@Service
public interface ChatGroupService {

    int updateChatGroup(String accessToken, long departmentId,String orgDeptOwner) throws OApiException;

    int updateChatGroupMut() throws OApiException, InterruptedException;
}
