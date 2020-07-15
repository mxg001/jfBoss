package cn.eeepay.framework.serviceImpl.sys;

import cn.eeepay.framework.dao.sys.ScheduleDao;
import cn.eeepay.framework.service.sys.ScheduleService;
import cn.eeepay.framework.serviceImpl.schedule.abstractJob.ScheduleJob;
import cn.eeepay.framework.serviceImpl.schedule.job.LeaguerImportJob;
import cn.eeepay.framework.serviceImpl.schedule.job.PrizeTimesResetJob;
import cn.eeepay.framework.serviceImpl.schedule.job.TestDemoJob;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service("scheduleService")
public class ScheduleServiceImpl implements ScheduleService {
    @Resource
    private ScheduleDao scheduleDao;

    @Resource
    private TaskExecutor taskExecutor;

    @Resource
    private BeanFactory beanFactory;

    @Override
    public int insertTask(String runNo,String interfaceName) {
        return scheduleDao.insert(runNo,interfaceName);
    }

    @Override
    public int updateTask(String runNo, String status) {
        return scheduleDao.updateStatus(runNo, status);
    }

    @Override
    public Map<String, Object> queryTask(String runNo) {
        return scheduleDao.query(runNo);
    }

    public Map<String, Object> process(String runNo, String interfaceType) {
        Map<String, Object> msg = new HashMap<>();
        msg.put("runningNo", runNo);
        Map<String, Object> task = queryTask(runNo);
        if (task != null) {
            //针对定时系统 只支持传值running,complete类型
            if("init".equals(task.get("running_status"))){
                msg.put("runningStatus","running");
            }else{
                msg.put("runningStatus", task.get("running_status"));
            }
        } else {
            insertTask(runNo,interfaceType);
            //添加线程池
            ScheduleJob runnable = null;
            if ("testDemo".equals(interfaceType)) {
                runnable = getSrpingBean(TestDemoJob.class);

            }else if("prizeTimesReset".equals(interfaceType)){//抽奖奖项次数归0
                runnable = getSrpingBean(PrizeTimesResetJob.class);

            }else if("leaguerRegister".equals(interfaceType)){//会员注册
                runnable = getSrpingBean(LeaguerImportJob.class);
            }

            if (runnable != null){
                scheduleDao.updateStatus(runNo,"running");
                runnable.setRunNo(runNo);
                taskExecutor.execute(runnable);
            }
            msg.put("runningStatus", "running");
        }
        return msg;
    }

    /**
     *加载多列bean
     */
    private ScheduleJob getSrpingBean(Class name){
        Object obj=beanFactory.getBean(name);
        if(obj instanceof ScheduleJob){
            return (ScheduleJob)obj;
        }
        return null;
    }

}
