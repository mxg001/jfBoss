package cn.eeepay.framework.dao.integral;

import cn.eeepay.framework.model.integral.TaskType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Administrator on 2019/7/4/004.
 * @author liuks
 * 任务类型dao
 */
public interface TaskTypeDao {

    @Select(
            "select * from vip_task_type_config ORDER BY create_time"
    )
    List<TaskType> getTaskTypeList();
}
