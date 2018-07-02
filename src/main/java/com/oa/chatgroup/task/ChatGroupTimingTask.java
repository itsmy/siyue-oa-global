/*
package com.oa.chatgroup.task;

import com.oa.chatgroup.service.ChatGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Lazy(false)
public class ChatGroupTimingTask {
    @Autowired
    private ChatGroupService chatGroupService;
    private final Logger logger = LoggerFactory.getLogger(ChatGroupTimingTask.class);

    @Scheduled(cron = "0 0 17 * * ?")
    public void chatGroupUpdate() throws Exception {
        logger.info(new Date() + "ChatGroupTimingTask++++++++++++++++++chatGroupUpdate()++++++++++START++++++++++++++");
        chatGroupService.updateChatGroupMut();
        logger.info(new Date() + "ChatGroupTimingTask++++++++++++++++++chatGroupUpdate()+++++++++++++END++++++++++");
    }
}
*/
