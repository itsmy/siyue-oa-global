package com.oa.organization.task;

import com.oa.common.util.DateUtil;
import com.oa.organization.exception.OApiException;
import com.oa.chatroup.service.ChatGroupService;
import com.oa.organization.service.DepartmentService;
import com.oa.organization.service.SyncLogService;
import com.oa.organization.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 定时任务控制类
 */
@Component("taskJob")
@Lazy(false)
public class OrganizationTimingTask {
    private final Logger logger = LoggerFactory.getLogger(OrganizationTimingTask.class);
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private UserService userService;
    @Autowired
    private SyncLogService syncLogService;

    /**
     * spring自带的timing定时任务功能实现定时更新部门和人员，每天凌晨两点进行
     *
     * @throws Exception
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void organizationUpdate() throws Exception {
        departmentService.updateDeptMut();
        logger.info(new Date() + "定时任务启动+++++++++++++++++++++A方案+++++++++++++++++++++++++++++++++");
        try {
            /*开始之前，标记状态为F*/
            syncLogService.insertLog();
            userService.updateUserMut(1);
            syncLogService.updateLog("S");
            logger.info(new Date() + "定时任务完成+++++++++++++++++++++A方案+++++++++++++++++++++++++++++++++");
        } catch (Exception e) {
            logger.info(new Date() + "定时任务启动+++++++++++++++++++++B方案+++++++++++++++++++++++");
            e.getMessage();
            syncLogService.insertLog();
            /*异常情况需要获取最后一次更新成功的时间*/
            Date recordDate = syncLogService.getLatestSuccessLog();
            if (recordDate != null) {
                Date nowDate = new Date();
                int differ = DateUtil.dateDiffer(recordDate, nowDate);
                userService.updateUserMut(differ);
                syncLogService.updateLog("S");
                logger.info(new Date() + "定时任务完成+++++++++++++++++++++B方案+++++++++++++++++++++++");
            } else {
                syncLogService.updateLog("F");
                logger.error(new Date() + "定时任务失败+++++++++++++++++++++ALL+++++++++++++++++++++++", e.getMessage(), e);
            }
        }
    }
}
