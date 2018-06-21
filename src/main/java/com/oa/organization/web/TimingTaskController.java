package com.oa.organization.web;

import com.oa.common.util.DateUtil;
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
@Component
@Lazy(false)
public class TimingTaskController {
    private final Logger logger = LoggerFactory.getLogger(TimingTaskController.class);
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private UserService userService;
    @Autowired
    private SyncLogService syncLogService;

    /**
     * spring自带的timing定时任务功能实现定时更新部门和人员
     *
     * @throws Exception
     */
    @Scheduled(cron = "0 0 12 * * *")
    public void timingUpdate() throws Exception {
        logger.info("定时任务启动+++++++++++++++++++++A方案+++++++++++++++++++++++++++++++++");
        try {
            /*开始之前，标记状态为F*/
            syncLogService.insertLog();
            departmentService.updateDeptMut();
            userService.updateUserMut(1);
            syncLogService.updateLog("S");
        } catch (Exception e) {
            logger.info("定时任务启动+++++++++++++++++++++B方案+++++++++++++++++++++++");
            e.getMessage();
            syncLogService.insertLog();
            /*异常情况需要获取最后一次更新成功的时间*/
            Date recordDate = syncLogService.getLatestSuccessLog();
            if (recordDate != null) {
                Date nowDate = new Date();
                int differ = DateUtil.dateDiffer(recordDate, nowDate);
                userService.updateUserMut(differ);
                syncLogService.updateLog("S");
            } else {
                syncLogService.updateLog("F");
            }
        }
    }
}
