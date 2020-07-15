package cn.eeepay.framework.dao.activity;

import cn.eeepay.framework.model.activity.ConditionForStandard;
import org.apache.ibatis.annotations.*;

public interface ConditionForStandardDao {


    @Update("update vip_condition_for_standard set extend = #{extend} where act_det_code = #{actDetCode}")
    int updateCondtionByActDetCode(@Param("actDetCode") String actDetCode, @Param("extend") String extend);

    @Select("select * from vip_condition_for_standard where act_det_code = #{actDetCode}")
    @ResultType(ConditionForStandard.class)
    ConditionForStandard findConditionByActDetCode(String actDetCode);

    @Insert(
            " INSERT INTO vip_condition_for_standard(act_det_code,extend,create_time) " +
                    " VALUES " +
                    " (#{condition.actDetCode},#{condition.extend},#{condition.createTime}) "
    )
    int insertCondition(@Param("condition") ConditionForStandard conditionForStandard);

    @Delete("delete from vip_condition_for_standard where act_det_code = #{actDetCode}")
    int deleteConditionByActDetCode(String actDetCode);
}
