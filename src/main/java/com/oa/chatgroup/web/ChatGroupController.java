package com.oa.chatgroup.web;

import com.oa.common.enums.ResultCode;
import com.oa.common.util.AuthUtil;
import com.oa.common.vo.Result;
import com.oa.chatgroup.service.ChatGroupService;
import com.oa.organization.exception.OApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chatGroup")
public class ChatGroupController {
    @Autowired
    private ChatGroupService chatGroupService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/group/update")
    public Result updateChatGroup() throws OApiException, InterruptedException {
        logger.info("Entering...updateChatGroup................................");
        chatGroupService.updateChatGroupMut();
        logger.info("Existing...updateChatGroup................................");
        return new Result(ResultCode.SUCCESS);
    }
}
