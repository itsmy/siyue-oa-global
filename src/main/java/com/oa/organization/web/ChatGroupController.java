package com.oa.organization.web;

import com.oa.common.util.AuthUtil;
import com.oa.organization.exception.OApiException;
import com.oa.organization.service.ChatGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/organization")
public class ChatGroupController {
    @Autowired
    private ChatGroupService chatGroupService;

    @GetMapping("/group/update")
    public String createChatGroup() {
        try {
            chatGroupService.updateChatGroupMut();
            return "success";
        } catch (OApiException e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/group/create")
    public String updateChatGroup() {
        return "success";
    }

}
