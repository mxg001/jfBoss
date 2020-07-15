package cn.eeepay.framework.serviceImpl.schedule.job;

import cn.eeepay.framework.service.activity.PrizeInfoService;
import cn.eeepay.framework.service.integral.LeaguerService;
import cn.eeepay.framework.serviceImpl.schedule.abstractJob.ScheduleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Scope("prototype")
public class LeaguerImportJob extends ScheduleJob {

    private static final Logger log = LoggerFactory.getLogger(PrizeTimesResetJob.class);

    @Resource
    private LeaguerService leaguerService;
    @Override
    protected void runTask(String runNo) {
        //0 0/5 0 * * ?
        log.info("--------------  会员数据同步定时任务开始:--------------");
        //业务逻辑
        leaguerService.leaguerImportJob();
        log.info("--------------  会员数据同步定时任务结束--------------");
    }
}
