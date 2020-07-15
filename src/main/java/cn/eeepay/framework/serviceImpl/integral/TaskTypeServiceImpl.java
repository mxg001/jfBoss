package cn.eeepay.framework.serviceImpl.integral;

import cn.eeepay.framework.dao.integral.TaskTypeDao;
import cn.eeepay.framework.model.integral.TaskType;
import cn.eeepay.framework.service.integral.TaskTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2019/7/4/004.
 * @author  liuks
 * 任务类型
 */
@Service("taskTypeService")
public class TaskTypeServiceImpl implements TaskTypeService {

    @Resource
    private TaskTypeDao taskTypeDao;

    @Override
    public List<TaskType> getTaskTypeList() {
        return taskTypeDao.getTaskTypeList();
    }
}
