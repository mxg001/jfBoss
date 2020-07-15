package cn.eeepay.framework.dao.activity;

import cn.eeepay.framework.model.activity.ActivityDetail;
import cn.eeepay.framework.model.activity.ActivityManagement;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ActivityDetailDao {

    @Select(
            "select * from vip_activity_detail where act_code=#{actCode} and active_type='4' and is_delete='0' "
    )
    ActivityDetail getLuckDrawpDetail(@Param("actCode") String actCode);

    @Select(
            "select * from vip_activity_detail where act_det_code=#{actDetCode} and is_delete='0' "
    )
    ActivityDetail getActivityDetailByCode(@Param("actDetCode") String actDetCode);

    @Select(
            "select * from vip_activity_detail where act_code=#{actCode} and act_det_code=#{actDetCode} and is_delete='0' "
    )
    ActivityDetail getActivityDetailByActCodeAndActDetCode(@Param("actCode") String actCode, @Param("actDetCode") String actDetCode);

    @Select(
            "select * from vip_activity_detail where act_code=#{actCode} and is_delete='0' "
    )
    @ResultType(ActivityDetail.class)
    List<ActivityDetail> getActivityDetailList(@Param("actCode") String actCode);

    @Select(
            "select * from vip_activity_detail where act_code=#{actCode} and active_type in (${activeType}) and is_delete='0' "
    )
    @ResultType(ActivityDetail.class)
    public List<ActivityDetail> getActivityDetailByActCodeAndActiveType(@Param("actCode") String actCode, @Param("activeType") String activeType);

    @Select(
            "select * from vip_activity_detail where id=#{id}"
    )
    @ResultType(ActivityDetail.class)
    public ActivityDetail findById(Long id);

    /**
     * 抽奖模板新增
     */
    @Insert(
            " INSERT INTO vip_activity_detail " +
                    " (act_det_code,act_code,expend_type,expend_num,active_type, " +
                    "  amount_range_min,notice,create_time,expend_number) " +
                    " VALUES " +
                    " (#{info.actDetCode},#{info.actCode},#{info.expendType},#{info.expendNum},#{info.activeType}, " +
                    "  #{info.amountRangeMin},#{info.notice},now(),#{info.expendNumber}) "
    )
    int insertLuckActTemplate(@Param("info") ActivityDetail addInfo);

    @Update(
            " update vip_activity_detail " +
                    " set expend_num=#{info.expendNum},notice=#{info.notice},amount_range_min=#{info.amountRangeMin}, " +
                    "  expend_type=#{info.expendType},expend_number=#{info.expendNumber} " +
                    " where act_code=#{info.actCode} and active_type='4' and is_delete='0' "
    )
    int updateLuckActTemplate(@Param("info") ActivityDetail luckDrawpDetail);

    /**
     * 用户激活新增内容
     *
     * @param info
     * @return
     */
    @Insert(
            " INSERT INTO vip_activity_detail " +
                    " (act_det_code,act_code,active_name,active_desc,expend_num,active_type, " +
                    "  award_method,create_time,expend_number,expiry_date_type,expiry_num,all_num,policy_type,section_begin,section_end" +
                    ",is_link_behind,standard_type,black_reward) " +
                    " VALUES " +
                    " (#{info.actDetCode},#{info.actCode},#{info.activeName},#{info.activeDesc},#{info.expendNum},#{info.activeType}, " +
                    "  #{info.awardMethod},now(),#{info.expendNumber},#{info.expiryDateType},#{info.expiryNum},#{info.allNum},#{info.policyType},#{info.sectionBegin}" +
                    ",#{info.sectionEnd},#{info.isLinkBehind},#{info.standardType},#{info.blackReward}) "
    )
    int insertActiInfo(@Param("info") ActivityDetail info);

    /**
     * 用户激活新增内容
     *
     * @param info
     * @return
     */
    @Update(
            " update vip_activity_detail set " +
                    "  active_name=#{info.activeName},active_desc=#{info.activeDesc},award_method=#{info.awardMethod} " +
                    " where act_det_code=#{info.actDetCode} "
    )
    int updateActiInfo(@Param("info") ActivityDetail info);

    @Update(
            "update vip_activity_detail set is_delete='1' where act_det_code=#{actDetCode} and is_delete='0' "
    )
    int deleteActiInfoByCode(@Param("actDetCode") String actDetCode);

    @Insert(
            " INSERT INTO vip_activity_detail " +
                    " (act_det_code,act_code,active_name,active_desc,expend_type, " +
                    "  expend_num,act_det_status,active_type,award_method,notice, " +
                    "  create_time,is_delete,amount_range_min) " +
                    " VALUES " +
                    " (#{newInfo.actDetCode},#{newInfo.actCode},#{oldInfo.activeName},#{oldInfo.activeDesc},#{oldInfo.expendType}," +
                    "  #{oldInfo.expendNum},#{oldInfo.actDetStatus},#{oldInfo.activeType},#{oldInfo.awardMethod},#{oldInfo.notice}," +
                    "  now(),'0',#{oldInfo.amountRangeMin})"
    )
    int insertCopyActiDeInfo(@Param("newInfo") ActivityDetail newInfo, @Param("oldInfo") ActivityDetail oldInfo);

    @Update(
            " update vip_activity_detail set " +
                    "act_det_code=#{info.actDetCode},act_code=#{info.actCode},active_name=#{info.activeName},active_desc=#{info.activeDesc},expend_type=#{info.expendType}," +
                    "expend_num=#{info.expendNum},act_det_status=#{info.actDetStatus},active_type=#{info.activeType}," +
                    "award_method=#{info.awardMethod},notice=#{info.notice},create_time=#{info.createTime},is_delete=#{info.isDelete},expend_number=#{info.expendNumber}," +
                    "expiry_date_type=#{info.expiryDateType},expiry_num=#{info.expiryNum},all_num=#{info.allNum},policy_type=#{info.policyType},section_begin=#{info.sectionBegin}," +
                    "section_end=#{info.sectionEnd},is_link_behind=#{info.isLinkBehind},standard_type=#{info.standardType},black_reward=#{info.blackReward}" +
                    " where id=#{info.id} "
    )
    int updateActivityDetail(@Param("info") ActivityDetail info);

    @Update(
            "update vip_activity_detail set is_delete='1' where act_code=#{actCode} and is_delete='0' "
    )
    int deleteActiInfoByActCode(String actCode);

    @Update("update vip_activity_detail set open_status = #{status} where act_code = #{actCode} and act_det_code = #{actDetCode}")
    int updateActivityDetailOpenStatus(@Param("actCode") String actCode, @Param("actDetCode") String actDetCode, @Param("status") Integer status);

    @Select("SELECT * FROM vip_activity WHERE business_no=#{businessNo} AND is_delete='0'")
    List<ActivityManagement> selectAllActivity(@Param("businessNo") String businessNo);
}
