package cn.eeepay.framework.dao.activity;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.activity.ActivityManagement;
import cn.eeepay.framework.model.activity.RangeName;
import cn.eeepay.framework.model.activity.ShowConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface ActivityManagementDao {

    /**
     * 活动模板查询
     */
    @Select(
            "select * from vip_activity where business_no='0' and is_delete='0' "
    )
    @ResultType(ActivityManagement.class)
    List<ActivityManagement> getActivityList();

    @Select(
            " select * from vip_activity where act_code=#{actCode} and business_no=#{businessNo} and is_delete='0' "
    )
    ActivityManagement getActivityInfo(@Param("actCode") String actCode, @Param("businessNo") String businessNo);

    @Select(
            " select * from vip_activity where id=#{id} and business_no=#{businessNo} and is_delete='0' "
    )
    ActivityManagement getActivityInfoById(@Param("id") Long id, @Param("businessNo") String businessNo);

    /**
     * 保存活动
     *
     * @param info
     * @return
     */
    @UpdateProvider(type = ActivityManagementDao.SqlProvider.class, method = "saveActivity")
    int saveActivity(@Param("info") ActivityManagement info);

    @SelectProvider(type = ActivityManagementDao.SqlProvider.class, method = "getActivityListBusNo")
    @ResultType(ActivityManagement.class)
    List<ActivityManagement> getActivityListBusNo(@Param("info") ActivityManagement info);

    @Insert(
            " INSERT INTO vip_activity " +
                    " (business_no,act_code,ori_act_code,act_name,act_desc,seq_no, " +
                    "  award_desc,act_rule,act_link,open_status,show_status,act_begin_time,act_end_time,act_range_type, " +
                    "  act_type,create_time,is_delete,act_img,withdrawal_time_end) " +
                    " VALUES " +
                    " (#{newInfo.businessNo},#{newInfo.actCode},#{newInfo.oriActCode},#{oldInfo.actName},#{oldInfo.actDesc},#{newInfo.seqNo}," +
                    "  #{oldInfo.awardDesc},#{oldInfo.actRule},#{newInfo.actLink},'0','0',#{oldInfo.actBeginTime},#{oldInfo.actEndTime},'0'," +
                    "  #{oldInfo.actType},now(),'0',#{oldInfo.actImg},#{oldInfo.withdrawalTimeEnd}) "
    )
    int insertCopyActivity(@Param("newInfo") ActivityManagement newInfo, @Param("oldInfo") ActivityManagement oldInfo);

    @Update(
            "update vip_activity set open_status=#{status} where id=#{id} and business_no=#{businessNo}"
    )
    int activitySwitch(@Param("id") Long id, @Param("status") int status, @Param("businessNo") String businessNo);

    @Update(
            "update vip_activity set show_status=#{status} where id=#{id} and business_no=#{businessNo}"
    )
    int homePageSwitch(@Param("id") Long id, @Param("status") int status, @Param("businessNo") String businessNo);

    @Update(
            "update vip_activity set is_delete='1' where id=#{info.id} and business_no=#{info.businessNo} "
    )
    int deleteActivity(@Param("info") ActivityManagement info);

    @Delete(
            " delete from vip_activity_range where id=#{info.id} "
    )
    int deleteRangeName(@Param("info") RangeName item);

    @Insert(
            " INSERT INTO vip_activity_range " +
                    " (act_code,leaguer_no,create_time,operator) " +
                    " VALUES " +
                    " (#{info.actCode},#{info.leaguerNo},now(),#{info.operator}) "
    )
    int insertRangeName(@Param("info") RangeName item);

    /**
     * 批量新增范围管理名单
     *
     * @param list
     * @return
     */
    @Insert(
            "<script> INSERT INTO vip_activity_range(act_code,leaguer_no,operator,create_time) VALUES " +
                    "<foreach  collection=\"list\" item=\"info\" index=\"index\" separator=\",\" > " +
                    "(#{info.actCode},#{info.leaguerNo},#{info.operator},now()) " +
                    " </foreach ></script>"
    )
    int insertRangeNameBatch(@Param("list") List<RangeName> list);

    @Delete(
            " delete from vip_activity_range where act_code=#{actCode}"
    )
    int deleteActRangeList(@Param("actCode") String actCode);

    @Update(
            " update vip_activity set act_range_type=#{actRangeType} where act_code=#{actCode} "
    )
    int updateActRangeType(@Param("actCode") String actCode, @Param("actRangeType") String actRangeType);


    @Select(
            "select * from vip_lottery_show_config where act_code=#{actCode} "
    )
    ShowConfig getShowConfig(String actCode);

    @Insert(
            "INSERT INTO vip_lottery_show_config (config_data,act_code,create_time,operater) " +
                    " VALUES (#{info.configData},#{info.actCode},now(),#{info.operater}) "
    )
    int addShowConfig(@Param("info") ShowConfig info);

    @Update(
            " update vip_lottery_show_config set config_data=#{info.configData} where act_code=#{info.actCode} "
    )
    int updateShowConfig(@Param("info") ShowConfig info);

    @SelectProvider(type = ActivityManagementDao.SqlProvider.class, method = "getRangeNameList")
    @ResultType(RangeName.class)
    List<RangeName> getRangeNameList(@Param("info") RangeName info, @Param("page") Page<RangeName> page);

    @Select(
            " select * from vip_activity_range where act_code=#{actCode} and leaguer_no=#{leaguerNo}"
    )
    @ResultType(RangeName.class)
    List<RangeName> getRangeNameInfo(@Param("leaguerNo") String leaguerNo, @Param("actCode") String actCode);

    @Select(
            " select * from vip_activity where act_code=#{actCode} and business_no=#{businessNo} and is_delete='0' "
    )
    @ResultType(ActivityManagement.class)
    ActivityManagement findActivityDetailByActCodeAndBusinessNo(@Param("actCode") String actCode, @Param("businessNo") String businessNo);

    class SqlProvider {

        public String saveActivity(final Map<String, Object> param) {
            ActivityManagement info = (ActivityManagement) param.get("info");
            StringBuffer sb = new StringBuffer();
            sb.append(" update vip_activity set ");
            sb.append("    act_name=#{info.actName},act_desc=#{info.actDesc},seq_no=#{info.seqNo},award_desc=#{info.awardDesc},withdrawal_time_end=#{info.withdrawalTimeEnd}, ");
            if (StringUtils.isNotBlank(info.getActImg())) {
                sb.append(" act_img=#{info.actImg}, ");
            }
            sb.append("    act_rule=#{info.actRule},act_begin_time=#{info.actBeginTime},act_end_time=#{info.actEndTime} ");
            sb.append("  where id=#{info.id} and business_no=#{info.businessNo} and is_delete='0' ");
            return sb.toString();
        }

        public String getActivityListBusNo(final Map<String, Object> param) {
            ActivityManagement info = (ActivityManagement) param.get("info");
            StringBuffer sb = new StringBuffer();
            sb.append(" select * ");
            sb.append("  from  vip_activity acti ");
            sb.append(" where 1=1 ");
            if (StringUtils.isNotBlank(info.getBusinessNo())) {
                sb.append(" and acti.business_no=#{info.businessNo} ");
            }
            if (StringUtils.isNotBlank(info.getBusinessNoFilter())) {
                if (!"-1".equals(info.getBusinessNoFilter())) {
                    sb.append(" and acti.business_no in (" + info.getBusinessNoFilter() + ") ");
                }
            } else {
                sb.append(" and 1=2 ");
            }
            sb.append(" and acti.is_delete = '0' ");
            sb.append(" ORDER BY acti.open_status desc,acti.create_time desc ");
            return sb.toString();
        }

        public String getRangeNameList(final Map<String, Object> param) {
            RangeName info = (RangeName) param.get("info");
            StringBuffer sb = new StringBuffer();
            sb.append(" select rang.*,lea.leaguer_name,lea.business_no,lea.mobile_phone,lea.origin_user_no,bus.business_name ");
            sb.append("    from vip_activity_range rang ");
            sb.append("      LEFT JOIN vip_leaguer_info lea ON lea.leaguer_no=rang.leaguer_no ");
            sb.append("      LEFT JOIN vip_business_info bus ON bus.business_no=lea.business_no ");

            sb.append(" where 1=1 ");
            if (StringUtils.isNotBlank(info.getActCode())) {
                sb.append(" and rang.act_code=#{info.actCode} ");
            }
            if (StringUtils.isNotBlank(info.getLeaguerNo())) {
                sb.append(" and rang.leaguer_no=#{info.leaguerNo} ");
            }
            if (StringUtils.isNotBlank(info.getOriginUserNo())) {
                sb.append(" and lea.origin_user_no=#{info.originUserNo} ");
            }
            if (StringUtils.isNotBlank(info.getMobilePhone())) {
                sb.append(" and lea.mobile_phone=#{info.mobilePhone} ");
            }
            sb.append(" ORDER BY rang.create_time desc ");
            return sb.toString();
        }
    }
}
