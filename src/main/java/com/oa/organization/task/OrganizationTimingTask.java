package com.oa.organization.task;

import com.dingtalk.open.client.api.model.corp.CorpUserDetail;
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
import java.util.List;

/**
 * 定时任务控制类
 */
@Component
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
    @Scheduled(cron = "0 0 1 * * *")
    public void departmentUpdate() {
        logger.info(new Date() + "departmentTimingUpdate+++++++++++++++++++++START+++++++++++++++++++++++++++");
        try {
            departmentService.updateDeptMut();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            logger.info(new Date() + "departmentTimingUpdate+++++++++++++++++++++END+++++++++++++++++++++++++++");
        }
    }

    @Scheduled(cron = "0 30 1 * * *")
    public void userUpdate() throws Exception {

        logger.info(new Date() + "userTimingUpdate+++++++++++++++++++++START-A+++++++++++++++++++++++++++");
        try {
            /*开始之前，标记状态为F*/
            syncLogService.insertLog();
            userService.updateUserMut(1);
            syncLogService.updateLog("S");
            logger.info(new Date() + "userTimingUpdate++++++++++++++++END-A+++++++++++++++++++++++++++++++++");
        } catch (Exception e) {
            logger.info(new Date() + "userTimingUpdate+++++++++++++++++++++START-B+++++++++++++++++++++++");
            e.getMessage();
            syncLogService.insertLog();
            /*异常情况需要获取最后一次更新成功的时间*/
            Date recordDate = syncLogService.getLatestSuccessLog();
            if (recordDate != null) {
                Date nowDate = new Date();
                int differ = DateUtil.dateDiffer(recordDate, nowDate);
                userService.updateUserMut(differ);
                syncLogService.updateLog("S");
                logger.info(new Date() + "userTimingUpdate+++++++++++END-B+++++++++++++");
            } else {
                syncLogService.updateLog("F");
                logger.error(new Date() + "userTimingUpdate+++++++++FAILED+++++++++", e.getMessage(), e);
            }
        }
    }

    @Scheduled(cron = "0 0 2 * * *")
    public void compareUser() throws Exception {
        logger.info("compareUser......................START.....................");
        try {
            List<CorpUserDetail> invalidUserList = userService.compareUser();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            logger.info("compareUser...................END.....................");
        }
    }
/*    *//*每周执行一次全量同步防止漏掉的人*//*
    @Scheduled(cron = "0 0 3 * * WED")
    public void createUser(){
        logger.info("creteUser......................START.....................");
        try{
            userService.createUserMut();
        }catch(Exception e){
            logger.error(e.getMessage(),e);
        }finally {
            logger.info("createUser..........................END.....................");
        }
    }*/
}
