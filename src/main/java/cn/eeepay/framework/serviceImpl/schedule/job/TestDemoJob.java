package cn.eeepay.framework.serviceImpl.schedule.job;

import cn.eeepay.framework.serviceImpl.schedule.abstractJob.ScheduleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 将所有交易成功且记账不成功的记录，循环调用账户接口
 * @author MXG
 * create 2018/11/06
 */
@Component
@Scope("prototype")
public class TestDemoJob extends ScheduleJob {

    private static final Logger log = LoggerFactory.getLogger(TestDemoJob.class);


    @Override
    protected void runTask(String runNo) {
        log.info("--------------  调用账户的接口，定时任务开始:--------------");
        //业务逻辑
        log.info("--------------  调用账户的接口，定时任务结束--------------");
    }
}
