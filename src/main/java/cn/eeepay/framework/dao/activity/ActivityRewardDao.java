package cn.eeepay.framework.dao.activity;

import cn.eeepay.framework.model.activity.ActivityReward;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * 活动权益操作dao
 */
public interface ActivityRewardDao {

    @Insert(
            "INSERT INTO vip_activity_reward " +
                    " (act_reward_no,link_code,standard_amount,reward_type,calc_type,award_num," +
                    "  award_template_formula,expire_day,status,create_time,is_delete,reward_name) " +
                    " VALUES " +
                    " (#{info.actRewardNo},#{info.linkCode},#{info.standardAmount},#{info.rewardType},#{info.calcType},#{info.awardNum}," +
                    "  #{info.awardTemplateFormula},#{info.expireDay},'1',now(),'0',#{info.rewardName} )"
    )
    int addActRewardInfo(@Param("info") ActivityReward info);

    @Update(
            "update vip_activity_reward " +
                    " set standard_amount=#{info.standardAmount},reward_type=#{info.rewardType},calc_type=#{info.calcType}, " +
                    "     award_num=#{info.awardNum},award_template_formula=#{info.awardTemplateFormula},expire_day=#{info.expireDay}, " +
                    "     reward_name=#{info.rewardName} " +
                    " where id=#{info.id} "
    )
    int updateActRewardInfo(@Param("info") ActivityReward addInfo);

    //抽奖的只有当个实体
    @Select(
            "select * from vip_activity_reward where link_code=#{linkCode} and is_delete='0' "
    )
    ActivityReward getActRewardInfo(@Param("linkCode") String linkCode);

    //活动激活内容奖品
    @Select(
            "select * from vip_activity_reward where link_code=#{linkCode} and is_delete='0' ORDER BY standard_amount asc,create_time asc "
    )
    @ResultType(ActivityReward.class)
    List<ActivityReward> getActRewardList(@Param("linkCode") String linkCode);

    @Update(
            "update vip_activity_reward set is_delete='1' where act_reward_no=#{info.actRewardNo} and is_delete='0' "
    )
    int deleteActReward(@Param("info") ActivityReward prize);

    @Update(
            "update vip_activity_reward set is_delete='1' where link_code=#{linkCode} and is_delete='0' "
    )
    int deleteActRewardByLinkCode(@Param("linkCode") String linkCode);

    @UpdateProvider(type = ActivityRewardDao.SqlProvider.class, method = "updateActRewardList")
    @ResultType(Integer.class)
    int updateActRewardList(@Param("strs") String strs);


    @Insert(
            " INSERT INTO vip_activity_reward " +
                    " (act_reward_no,link_code,standard_amount,reward_type,calc_type, " +
                    "  award_num,award_template_formula,expire_day,status,create_time,is_delete,reward_name) " +
                    " VALUES " +
                    " (#{newInfo.actRewardNo},#{newInfo.linkCode},#{oldInfo.standardAmount},#{oldInfo.rewardType},#{oldInfo.calcType}," +
                    "  #{oldInfo.awardNum},#{oldInfo.awardTemplateFormula},#{oldInfo.expireDay},'1',now(),'0',#{oldInfo.rewardName})"
    )
    int insertCopyActiReInfo(@Param("newInfo") ActivityReward newRe, @Param("oldInfo") ActivityReward itemRe);

    @Select(
            " select * from vip_activity_reward where calc_type=#{goodsCode} and is_delete='0' limit 1 "
    )
    ActivityReward getRewardInfoByGoodCode(@Param("goodsCode") String goodsCode);

    class SqlProvider {

        public String updateActRewardList(final Map<String, Object> param) {
            final String strs = (String) param.get("strs");
            return "update vip_activity_reward  set is_delete='1' where id in (" + strs + ")";
        }
    }
}
