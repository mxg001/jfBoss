package cn.eeepay.framework.serviceImpl.schedule.job;

import cn.eeepay.framework.service.activity.PrizeInfoService;
import cn.eeepay.framework.serviceImpl.schedule.abstractJob.ScheduleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Scope("prototype")
public class PrizeTimesResetJob extends ScheduleJob {

    private static final Logger log = LoggerFactory.getLogger(PrizeTimesResetJob.class);

    @Resource
    private PrizeInfoService prizeInfoService;
    @Override
    protected void runTask(String runNo) {
        //0 0 0 * * ?
        log.info("--------------  执行抽奖奖项次数归0定时任务开始:--------------");
        //业务逻辑
        prizeInfoService.prizeTimesReset();
        log.info("--------------  执行抽奖奖项次数归0定时任务结束--------------");
    }
}
