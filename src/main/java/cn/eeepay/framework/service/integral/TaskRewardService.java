package cn.eeepay.framework.service.integral;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.integral.TaskRewardConfig;
import cn.eeepay.framework.model.integral.TaskRewardInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/7/3/003.
 * @author  liuks
 * 任务配置
 */
public interface TaskRewardService {

    List<TaskRewardInfo> getSelectPageList(TaskRewardInfo info, Page<TaskRewardInfo> page);

    int addTaskRewardInfo(TaskRewardInfo info, Map<String, Object> msg);

    TaskRewardInfo getTaskRewardInfo(Long id);

    int editTaskRewardInfo(TaskRewardInfo info, Map<String, Object> msg);

    List<TaskRewardInfo> getTaskRewardInfoList();

    List<TaskRewardConfig> getTaskRewardEntry(String businessNo,String taskNo);

    int addTaskRewardEntry(TaskRewardConfig item);

    int updateTaskRewardEntry(TaskRewardConfig item);

    int deleteTaskRewardEntry(String strs);

    int deleteTaskReward(TaskRewardConfig info);

    TaskRewardInfo getTaskRewardInfoToCopy(Long valueOf);

}
