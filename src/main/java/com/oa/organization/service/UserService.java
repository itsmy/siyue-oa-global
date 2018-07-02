package com.oa.organization.service;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.open.client.api.model.corp.CorpUserBaseInfo;
import com.dingtalk.open.client.api.model.corp.CorpUserDetail;
import com.dingtalk.open.client.api.model.corp.CorpUserDetailList;
import com.dingtalk.open.client.api.model.corp.CorpUserList;
import com.oa.organization.exception.OApiException;

import java.util.Date;
import java.util.List;

public interface UserService {
    /**
     * 根据免登陆授权码查询免登陆用户id
     *
     * @param accessToken
     * @param code
     * @return
     * @throws Exception
     */
    CorpUserBaseInfo getUserInfo(String accessToken, String code) throws Exception;

    /**
     * 创建企业成员
     *
     * @param accessToken
     * @param userDetail
     * @return
     * @throws Exception
     */
    String createUser(String accessToken, CorpUserDetail userDetail) throws Exception;

    /**
     * 更新成员
     *
     * @param accessToken
     * @param userDetail
     * @throws Exception
     */
    void updateUser(String accessToken, CorpUserDetail userDetail) throws Exception;

    /**
     * 删除成员
     *
     * @param accessToken
     * @param userId
     * @throws Exception
     */
    void deleteUser(String accessToken, String userId) throws Exception;

    /**
     * 获取成员
     *
     * @param accessToken
     * @param userId
     * @return
     * @throws Exception
     */
    CorpUserDetail getUser(String accessToken, String userId) throws Exception;

    /**
     * 批量删除成员
     *
     * @param accessToken
     * @param userIdList
     * @throws Exception
     */
    void batchDeleteUser(String accessToken, List<String> userIdList) throws Exception;

    /**
     * 获取某个部门下的成员
     *
     * @param accessToken
     * @param departmentId
     * @param offset
     * @param size
     * @param order
     * @return
     * @throws Exception
     */
    CorpUserList getDepartmentUser(String accessToken, long departmentId, Long offset, Integer size, String order) throws Exception;

    /**
     * 获取部门成员详情
     *
     * @param accessToken
     * @param departmentId
     * @param offset
     * @param size
     * @param order
     * @return
     * @throws Exception
     */
    CorpUserDetailList getUserDetails(String accessToken, long departmentId, Long offset, Integer size, String order) throws Exception;

    /**
     * 管理后台免登时通过CODE换取微应用管理员的身份信息
     *
     * @param ssoToken
     * @param code
     * @return
     * @throws OApiException
     */
    JSONObject getAgentUserInfo(String ssoToken, String code) throws OApiException;

    List<String> createUserMut() throws Exception;

    Object updateUserMut(int dateDiffer) throws Exception;

    /*不属于我们公司的人员需要清理掉*/
    List<CorpUserDetail> compareUser() throws Exception;

    // boolean reUpdateUsers(String accessToken) throws Exception;
    int isExist(String accessToken, String userId);
}
