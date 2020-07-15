package cn.eeepay.framework.dao.integral;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.integral.TaskRewardConfig;
import cn.eeepay.framework.model.integral.TaskRewardInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/7/3/003.
 * @author liuks
 * 任务配置
 */
public interface TaskRewardDao {

    @SelectProvider(type=TaskRewardDao.SqlProvider.class,method="getSelectPageList")
    @ResultType(TaskRewardInfo.class)
    List<TaskRewardInfo> getSelectPageList(@Param("info") TaskRewardInfo info, @Param("page")Page<TaskRewardInfo> page);

    /**
     * 新增任务
     */
    @Insert(
            " INSERT INTO vip_task_info " +
                    " (task_type,task_no,task_name,task_desc,task_params," +
                    "  restrict_num,restrict_frequency,frequency_type,status,create_time,creater,task_overview ,grant_type,img_url) " +
                    " VALUES " +
                    " (#{info.taskType},#{info.taskNo},#{info.taskName},#{info.taskDesc},#{info.taskParams}," +
                    "  #{info.restrictNum},#{info.restrictFrequency},#{info.frequencyType},1,NOW(),#{info.creater},#{info.taskOverview},#{info.grantType},#{info.imgUrl})"
    )
    int addTaskRewardInfo(@Param("info")TaskRewardInfo info);

    /**
     *修改任务
     */
    @UpdateProvider(type=TaskRewardDao.SqlProvider.class,method="editTaskRewardInfo")
    int editTaskRewardInfo(@Param("info")TaskRewardInfo info);
    /**
     * 新增权益列表
     */
    @Insert(
            " INSERT INTO vip_task_reward_config " +
                    " (business_no,task_no,reward_type,reward_no,reward_name,calc_type," +
                    "  reward_template,reward_template_formula,reward_effe_day,create_time,creater,reward_no_back ) " +
                    " VALUES " +
                    " (#{info.businessNo},#{info.taskNo},#{info.rewardType},#{info.rewardNo},#{info.rewardName},#{info.calcType}, " +
                    "  #{info.rewardTemplate},#{info.rewardTemplateFormula},#{info.rewardEffeDay},NOW(),#{info.creater},#{info.rewardNoBack} )"
    )
    int addTaskRewardEntry(@Param("info")TaskRewardConfig info);

    /**
     * 更新权益列表
     */
    @Update(
            " UPDATE vip_task_reward_config set " +
                    " reward_type=#{info.rewardType},calc_type=#{info.calcType},reward_template=#{info.rewardTemplate}, " +
                    " reward_effe_day=#{info.rewardEffeDay},reward_name=#{info.rewardName}," +
                    " reward_template_formula=#{info.rewardTemplateFormula} " +
                    " where id=#{info.id}"
    )
    int updateTaskRewardEntry(@Param("info")TaskRewardConfig info);


    @Select(
            "select * from vip_task_info where id=#{id} "
    )
    TaskRewardInfo getTaskRewardInfo(@Param("id")Long id);

    @Select(
            "select * from vip_task_reward_config " +
                    " where business_no=#{businessNo} and task_no=#{taskNo} "
    )
    List<TaskRewardConfig> getTaskRewardEntry(@Param("businessNo")String businessNo,@Param("taskNo")String taskNo);

    @DeleteProvider(type=TaskRewardDao.SqlProvider.class,method="deleteTaskRewardEntry")
    @ResultType(Integer.class)
    int deleteTaskRewardEntry(@Param("strs")String strs);

    @Select(
            " select tas.*,tt.task_type_name taskTypeName " +
                    " from vip_task_info tas " +
                    " LEFT JOIN vip_task_type_config tt ON tt.task_type_no=tas.task_type order by tas.create_time"
    )
    @ResultType(TaskRewardInfo.class)
    List<TaskRewardInfo> getTaskRewardInfoList();

    @Delete(
            "delete from vip_task_reward_config where id=#{info.id}"
    )
    int deleteTaskReward(@Param("info")TaskRewardConfig info);

    @Select(
            " select * from vip_task_reward_config where calc_type=#{info} limit 1 "
    )
    TaskRewardConfig getTaskRewardEntryByGoodCode(@Param("info")String goodsCode);


    class SqlProvider{

        public String editTaskRewardInfo(final Map<String, Object> param){
            final TaskRewardInfo info = (TaskRewardInfo) param.get("info");
            String sql = "UPDATE vip_task_info set " +
                    " task_type=#{info.taskType},task_name=#{info.taskName},task_desc=#{info.taskDesc}" +
                    ",task_overview=#{info.taskOverview} ,grant_type=#{info.grantType} ,restrict_frequency=#{info.restrictFrequency},frequency_type=#{info.frequencyType}," +
                    " task_params=#{info.taskParams}," +
                    " restrict_num=#{info.restrictNum},task_params=#{info.taskParams}";
            if(StringUtils.isNotBlank(info.getImgUrl())){
                sql +=",img_url=#{info.imgUrl} " ;
            }
            sql +=" where id=#{info.id} ";

            return sql;
        }

        public String getSelectPageList(final Map<String, Object> param) {
            final TaskRewardInfo info = (TaskRewardInfo) param.get("info");
            return new SQL(){{
                SELECT(" tas.*,tt.task_type_name taskTypeName ");
                FROM(" vip_task_info tas ");
                LEFT_OUTER_JOIN(" vip_task_type_config tt ON tt.task_type_no=tas.task_type ");
                if(StringUtils.isNotBlank(info.getTaskNo())){
                    WHERE(" tas.task_no = #{info.taskNo} ");
                }
                if(StringUtils.isNotBlank(info.getTaskName())){
                    WHERE(" tas.task_name like concat(#{info.taskName},'%') ");
                }
                ORDER_BY(" tas.create_time ");
            }}.toString();
        }

        public String deleteTaskRewardEntry(final Map<String, Object> param) {
            final String strs = (String) param.get("strs");
            return "delete from vip_task_reward_config where id in ("+strs+")";
        }
    }
}
