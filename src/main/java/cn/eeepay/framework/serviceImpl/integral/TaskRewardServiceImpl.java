package cn.eeepay.framework.serviceImpl.integral;

import cn.eeepay.framework.dao.integral.TaskRewardDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.integral.TaskRewardConfig;
import cn.eeepay.framework.model.integral.TaskRewardInfo;
import cn.eeepay.framework.model.sys.UserLoginInfo;
import cn.eeepay.framework.service.integral.TaskRewardService;
import cn.eeepay.framework.service.sys.RandomNumService;
import cn.eeepay.framework.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/7/3/003.
 * @author liuks
 * 任务配置
 */
@Service("taskReward")
public class TaskRewardServiceImpl implements TaskRewardService {

    @Resource
    private TaskRewardDao taskRewardDao;
    @Resource
    private RandomNumService randomNumService;

    @Override
    public List<TaskRewardInfo> getSelectPageList(TaskRewardInfo info, Page<TaskRewardInfo> page) {
        return taskRewardDao.getSelectPageList(info,page);
    }

    @Override
    @Transactional(value = "transactionManager")
    public int addTaskRewardInfo(TaskRewardInfo info, Map<String, Object> msg) {
        if(checkTaskInfo(info,msg)){
            return 0;
        }
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setCreater(principal.getUsername());
        info.setTaskNo(randomNumService.getRandomNumberByData("TN","vipScore",5));
        int num=taskRewardDao.addTaskRewardInfo(info);
        if(num>0){
            maintainEntry(info);
            msg.put("status", true);
            msg.put("msg", "新增任务成功!");
        }else{
            msg.put("status", false);
            msg.put("msg", "新增任务失败!");
        }
        return num;
    }
    private boolean checkTaskInfo(TaskRewardInfo info,Map<String, Object> msg){
        if("TYPE002".equals(info.getTaskType())){
            if(info.getRestrictNum()!=null&&info.getRestrictNum().intValue()!=1){
                msg.put("status", false);
                msg.put("msg", "新手任务,可调用次数不为1");
                return true;
            }
        }
        if("1".equals(info.getGrantType())){
            if(info.getRestrictFrequency()!=null&&info.getRestrictFrequency().intValue()!=1){
                msg.put("status", false);
                msg.put("msg", "新手任务,当天调用次数不为1");
                return true;
            }
        }
        if(!"trans".equals(info.getTaskParams())){
            if(info.getTaskRewardConfigList()!=null&&info.getTaskRewardConfigList().size()>0){
                for (TaskRewardConfig taskRewardConfig : info.getTaskRewardConfigList()) {
                    if("2".equals(taskRewardConfig.getCalcType())){
                        msg.put("status",false);
                        msg.put("msg","当前触发类型不能含有公式类型的业务配置!");
                        return true;
                    }
                }
            }
        }
        return  false;
    }

    @Override
    @Transactional(value = "transactionManager")
    public int editTaskRewardInfo(TaskRewardInfo info, Map<String, Object> msg) {
        if(checkTaskInfo(info,msg)){
            return 0;
        }
        int num=taskRewardDao.editTaskRewardInfo(info);
        if(num>0){
            maintainEntry(info);
            msg.put("status", true);
            msg.put("msg", "修改任务成功!");
        }else{
            msg.put("status", false);
            msg.put("msg", "修改任务失败!");
        }
        return num;
    }

    @Override
    public List<TaskRewardInfo> getTaskRewardInfoList() {
        return taskRewardDao.getTaskRewardInfoList();
    }

    @Override
    public List<TaskRewardConfig> getTaskRewardEntry(String businessNo, String taskNo) {
        return taskRewardDao.getTaskRewardEntry(businessNo,taskNo);
    }

    @Override
    public int addTaskRewardEntry(TaskRewardConfig item) {
        return taskRewardDao.addTaskRewardEntry(item);
    }

    @Override
    public int updateTaskRewardEntry(TaskRewardConfig item) {
        return taskRewardDao.updateTaskRewardEntry(item);
    }

    @Override
    public int deleteTaskRewardEntry(String strs) {
        return taskRewardDao.deleteTaskRewardEntry(strs);
    }

    @Override
    public int deleteTaskReward(TaskRewardConfig info) {
        return taskRewardDao.deleteTaskReward(info);
    }

    @Override
    public TaskRewardInfo getTaskRewardInfo(Long id) {
        TaskRewardInfo info=taskRewardDao.getTaskRewardInfo(id);
        if(info!=null){
            info.setImgUrl(CommonUtil.getImgUrlAgent(info.getImgUrl()));
            info.setTaskRewardConfigList( taskRewardDao.getTaskRewardEntry("0",info.getTaskNo()));
        }
        return info;
    }
    @Override
    public TaskRewardInfo getTaskRewardInfoToCopy(Long id) {
        TaskRewardInfo info=taskRewardDao.getTaskRewardInfo(id);
        if(info!=null){
            info.setTaskRewardConfigList( taskRewardDao.getTaskRewardEntry("0",info.getTaskNo()));
        }
        return info;
    }
    private int maintainEntry(TaskRewardInfo info){
        List<TaskRewardConfig> list=info.getTaskRewardConfigList();
        int num=0;
        List<TaskRewardConfig> oldList=taskRewardDao.getTaskRewardEntry("0",info.getTaskNo());
        String strs=handleList(oldList);
        if(list!=null&&list.size()>0){
            for(TaskRewardConfig item:list){
                if(item.getId()==null){//新增
                    item.setRewardNo(randomNumService.getRandomNumberByData("TR","vipScore",5));
                    item.setCreater(info.getCreater());
                    item.setBusinessNo("0");
                    item.setTaskNo(info.getTaskNo());
                    item.setRewardNoBack(null);
                    num= num + taskRewardDao.addTaskRewardEntry(item);
                }else{
                    strs=handleString(strs,item.getId().toString());
                    //更新
                    num= num +taskRewardDao.updateTaskRewardEntry(item);
                }
            }
        }
        //清除删除过的数据
        if(strs!=null&&!",".equals(strs)){
            strs= strs.substring(1,strs.length()-1);
            taskRewardDao.deleteTaskRewardEntry(strs);
        }
        return num;
    }
    private String handleList(List<TaskRewardConfig> list){
        StringBuffer sb=new StringBuffer();
        sb.append(",");
        if(list!=null&&list.size()>0){
            for(TaskRewardConfig item:list){
                sb.append(item.getId()+",");
            }
        }
        return sb.toString();
    }

    /**
     * @param strs ,1,2,3,4,5
     * @param oldStr 4
     */
    private String handleString(String strs,String oldStr){
        String str=","+oldStr+",";
        if(strs.indexOf(str)>-1){
            return strs.replace(str,",");
        }
        return strs;
    }
}
