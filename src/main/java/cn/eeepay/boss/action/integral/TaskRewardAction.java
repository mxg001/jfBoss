package cn.eeepay.boss.action.integral;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.integral.BusinessInfo;
import cn.eeepay.framework.model.integral.TaskRewardInfo;
import cn.eeepay.framework.model.integral.TaskType;
import cn.eeepay.framework.service.integral.TaskRewardService;
import cn.eeepay.framework.service.integral.TaskTypeService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/7/3/003.
 * @author liuks
 * 基础任务配置
 */
@Controller
@RequestMapping(value = "/taskRewardAction")
public class TaskRewardAction {

    private static final Logger log = LoggerFactory.getLogger(TaskRewardAction.class);

    @Resource
    private TaskRewardService taskRewardService;

    @Resource
    private TaskTypeService taskTypeService;

    /**
     * 获取业务组列表
     */
    @RequestMapping(value = "/getSelectPageList")
    @ResponseBody
    public Map<String,Object> getSelectPageList(@ModelAttribute("page") Page<TaskRewardInfo> page,
                                                @RequestParam("info") String param){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            TaskRewardInfo info = JSON.parseObject(param, TaskRewardInfo.class);
            taskRewardService.getSelectPageList(info,page);
            msg.put("page", page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("获取业务组列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取业务组列表异常!");
        }
        return msg;
    }
    /**
     * 新增任务
     */
    @RequestMapping(value = "/addTaskRewardInfo")
    @ResponseBody
    @SystemLog(description = "新增任务",operCode="taskRewardAction.addTaskRewardInfo")
    public Map<String,Object> addTaskRewardInfo(@RequestParam("info") String param,@RequestParam("taskOverview")String taskOverview) {
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            TaskRewardInfo info = JSON.parseObject(param, TaskRewardInfo.class);
            info.setTaskOverview(taskOverview);
            taskRewardService.addTaskRewardInfo(info,msg);
        } catch (Exception e){
            log.error("新增任务异常!",e);
            msg.put("status", false);
            msg.put("msg", "新增任务异常!");
        }
        return msg;
    }

    /**
     * 获取任务
     */
    @RequestMapping(value = "/getTaskRewardInfo")
    @ResponseBody
    public Map<String,Object> getTaskRewardInfo(@RequestParam("id") Long id) {
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            TaskRewardInfo info=taskRewardService.getTaskRewardInfo(id);
            msg.put("info", info);
            msg.put("status", true);
        } catch (Exception e){
            log.error("新增任务异常!",e);
            msg.put("status", false);
            msg.put("msg", "新增任务异常!");
        }
        return msg;
    }
    /**
     * 修改任务
     */
    @RequestMapping(value = "/editTaskRewardInfo")
    @ResponseBody
    @SystemLog(description = "修改任务",operCode="taskRewardAction.editTaskRewardInfo")
    public Map<String,Object> editTaskRewardInfo(@RequestParam("info") String param,@RequestParam("taskOverview")String taskOverview) {
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            TaskRewardInfo info = JSON.parseObject(param, TaskRewardInfo.class);
            info.setTaskOverview(taskOverview);
            taskRewardService.editTaskRewardInfo(info,msg);
        } catch (Exception e){
            log.error("修改任务异常!",e);
            msg.put("status", false);
            msg.put("msg", "修改任务异常!");
        }
        return msg;
    }
    /**
     * 获取任务类型列表
     */
    @RequestMapping(value = "/getTaskTypeList")
    @ResponseBody
    public Map<String,Object> getTaskTypeList() {
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            List<TaskType> list=taskTypeService.getTaskTypeList();
            msg.put("list", list);
            msg.put("status", true);
        } catch (Exception e){
            log.error("获取任务类型列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取任务类型列表异常!");
        }
        return msg;
    }
    /**
     * 获取任务列表
     */
    @RequestMapping(value = "/getTaskRewardInfoList")
    @ResponseBody
    public Map<String,Object> getTaskRewardInfoList() {
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            List<TaskRewardInfo> list=taskRewardService.getTaskRewardInfoList();
            msg.put("list", list);
            msg.put("status", true);
        } catch (Exception e){
            log.error("获取任务类型列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取任务类型列表异常!");
        }
        return msg;
    }

}
