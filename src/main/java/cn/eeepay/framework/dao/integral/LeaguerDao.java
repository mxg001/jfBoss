package cn.eeepay.framework.dao.integral;

import cn.eeepay.framework.model.activity.LeagerBagSerial;
import cn.eeepay.framework.model.integral.BusinessInfo;
import cn.eeepay.framework.model.integral.LeaguerGrow;
import cn.eeepay.framework.model.integral.LeaguerInfo;
import cn.eeepay.framework.model.integral.LeaguerScore;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/7/10/010.
 *
 * @author liuks
 * 会员查询
 */
public interface LeaguerDao {


    @Select(
            "select * from vip_leaguer_info where origin_user_no=#{originUserNo} AND business_no=#{businessNo} limit 1 "
    )
    LeaguerInfo getLeaguerByOriNo(@Param("originUserNo") String originUserNo, @Param("businessNo") String businessNo);

    @Select(
            "select * from vip_leaguer_info where mobile_phone=#{mobilePhone} AND business_no=#{businessNo} limit 1 "
    )
    LeaguerInfo getLeaguerByPhone(@Param("mobilePhone") String mobilePhone, @Param("businessNo") String businessNo);

    /**
     * 会员列表查询
     *
     * @param info
     * @return
     */
    @SelectProvider(type = LeaguerDao.SqlProvider.class, method = "getSelectPageList")
    @ResultType(LeaguerInfo.class)
    List<LeaguerInfo> getSelectPageList(@Param("info") LeaguerInfo info);

    /**
     * 会员列表查询-统计
     *
     * @param info
     * @return
     */
    @SelectProvider(type = LeaguerDao.SqlProvider.class, method = "getSelectPageListCount")
    int getSelectPageListCount(@Param("info") LeaguerInfo info);

    @Select(
            "select " +
                    " lea.*,sco.score_balance," +
                    " (select bus.business_name from vip_business_info bus where bus.business_no=lea.business_no ) business_name, " +
                    " m.m_level_name as mLevelName,g.vip_level_name as vipLevelName " +
                    " from vip_leaguer_info lea " +
                    "  LEFT JOIN vip_business_info bus ON bus.business_no=lea.business_no " +
                    "  LEFT JOIN vip_m_level_config m ON (m.m_level=lea.m_level and m.business_no=lea.business_no) " +
                    "  LEFT JOIN vip_grow_level_config g ON (g.vip_level=lea.grow_up_level and g.business_no=lea.business_no) " +
                    "  LEFT JOIN vip_leaguer_score sco ON sco.leaguer_no=lea.leaguer_no " +
                    " where lea.id=#{id} and lea.business_no=#{businessNo} "
    )
    LeaguerInfo getLeaguerInfo(@Param("id") Long id, @Param("businessNo") String businessNo);


    @Select(
            "select t1.*,t2.vip_level_name as beforeLevelName,t3.vip_level_name as afterLevelName  " +
                    " from vip_leaguer_grow_info t1 " +
                    "  LEFT JOIN vip_grow_level_config t2 ON (t2.vip_level=t1.before_level and t2.business_no=t1.business_no) " +
                    "  LEFT JOIN vip_grow_level_config t3 ON (t3.vip_level=t1.after_level  and t3.business_no=t1.business_no) " +
                    " where t1.leaguer_no=#{leaguerNo}  and t1.grow_up_genre='1' and t1.before_level!=t1.after_level " +
                    " ORDER BY t1.create_time desc limit 20 "
    )
    List<LeaguerGrow> getLeaguerGrowList(@Param("leaguerNo") String leaguerNo);


    @Select(
            "select * from vip_leaguer_score where leaguer_no=#{leaguerNo} "
    )
    List<LeaguerScore> getLeaguerScoreList(@Param("leaguerNo") String leaguerNo);

    @Select(
            "select batch_balance from vip_score_batch where effe_date=#{effeDate} and leaguer_no=#{leaguerNo} and batch_status='1' "
    )
    BigDecimal getScoreBatch(@Param("effeDate") Date effeDate, @Param("leaguerNo") String leaguerNo);

    @SelectProvider(type = LeaguerDao.SqlProvider.class, method = "getCheckLeaguerInfo")
    @ResultType(LeaguerInfo.class)
    List<LeaguerInfo> getCheckLeaguerInfo(@Param("strNo") String strNo, @Param("businessNos") String businessNos, @Param("businessNo") String businessNo);

    /**
     * 根据编码查询业务组
     */
    @Select(
            "select business_no,digest_key from vip_business_info where business_no=#{businessNo}"
    )
    BusinessInfo getBusinessInfoByNo(@Param("businessNo") String businessNo);

    /**
     * 查询是否有权限
     *
     * @param businessNo
     * @return
     */
    @SelectProvider(type = LeaguerDao.SqlProvider.class, method = "getBusinessInfoByJurisdiction")
    @ResultType(LeaguerInfo.class)
    BusinessInfo getBusinessInfoByJurisdiction(@Param("businessNo") String businessNo, @Param("businessNos") String businessNos);

    @SelectProvider(type = SqlProvider.class, method = "findLeaguerInfoByUserNo")
    @ResultType(LeaguerInfo.class)
    List<LeaguerInfo> findLeaguerInfoByUserNo(@Param("key") String key, @Param("limit") int limit);

    class SqlProvider {

        public String findLeaguerInfoByUserNo(final Map<String, Object> param) {
            String key = (String) param.get("key");
            Integer limit = (Integer) param.get("limit");
            return new SQL() {{
                SELECT("*");
                FROM("vip_leaguer_info");
//                WHERE(" origin_user_no like  \"%\"#{key}\"%\" or leaguer_name like  \"%\"#{key}\"%\" ");
                WHERE(" origin_user_no = #{key} ");
                ORDER_BY("origin_user_no limit "+limit);
            }}.toString();
        }

        public String getSelectPageList(final Map<String, Object> param) {
            LeaguerInfo info = (LeaguerInfo) param.get("info");

            StringBuffer sb = new StringBuffer();
            sb.append(" select ");
            sb.append("   lea.*,sco.score_balance,");
            sb.append("   (select bus.business_name from vip_business_info bus where bus.business_no=lea.business_no ) business_name, ");
            sb.append("   m.m_level_name as mLevelName,g.vip_level_name as vipLevelName ");

            sb.append("  from  vip_leaguer_info lea ");
            sb.append("    LEFT JOIN vip_m_level_config m ON (m.m_level=lea.m_level and m.business_no=lea.business_no) ");
            sb.append("    LEFT JOIN vip_grow_level_config g ON (g.vip_level=lea.grow_up_level and g.business_no=lea.business_no) ");
            sb.append("    LEFT JOIN vip_leaguer_score sco ON sco.leaguer_no=lea.leaguer_no ");

            sqlWhereString(sb, info);
            sb.append(" ORDER BY lea.create_time desc ");

            if (info.getPageFirst() != null && info.getPageSize() != null) {
                sb.append(" LIMIT " + info.getPageFirst() + "," + info.getPageSize());
            }

            return sb.toString();
        }

        public String getSelectPageListCount(final Map<String, Object> param) {
            LeaguerInfo info = (LeaguerInfo) param.get("info");
            StringBuffer sb = new StringBuffer();
            sb.append(" select ");
            sb.append("   count(*) ");
            sb.append("  from  vip_leaguer_info lea ");
            sqlWhereString(sb, info);
            return sb.toString();
        }

        private void sqlWhereString(StringBuffer sb, LeaguerInfo info) {
            sb.append("  where 1=1 ");
            sb.append("     and status !='3' ");
            if (StringUtils.isNotBlank(info.getLeaguerNo())) {
                sb.append(" and lea.leaguer_no = #{info.leaguerNo} ");
            }
            if (StringUtils.isNotBlank(info.getBusinessNo())) {
                sb.append(" and lea.business_no = #{info.businessNo} ");
            }
            if (StringUtils.isNotBlank(info.getMobilePhone())) {
                sb.append(" and lea.mobile_phone = #{info.mobilePhone} ");
            }
            if (StringUtils.isNotBlank(info.getLeaguerName())) {
                sb.append(" and lea.leaguer_name like concat(#{info.leaguerName},'%') ");
            }
            if (StringUtils.isNotBlank(info.getOriginUserNo())) {
                sb.append(" and lea.origin_user_no = #{info.originUserNo} ");
            }
            if (info.getmLevel() != null) {
                sb.append(" and lea.m_level = #{info.mLevel} ");
            }
            if (info.getGrowUpLevel() != null) {
                sb.append(" and lea.grow_up_level = #{info.growUpLevel} ");
            }
            if (info.getCreateTimeBegin() != null) {
                sb.append(" and lea.create_time>=#{info.createTimeBegin}");
            }
            if (info.getCreateTimeEnd() != null) {
                sb.append(" and lea.create_time<=#{info.createTimeEnd}");
            }
            if (info.getGrowDateBegin() != null) {
                sb.append(" and lea.grow_date>=#{info.growDateBegin}");
            }
            if (info.getGrowDateEnd() != null) {
                sb.append(" and lea.grow_date<=#{info.growDateEnd}");
            }
            if (StringUtils.isNotBlank(info.getBusinessNoFilter())) {
                if (!"-1".equals(info.getBusinessNoFilter())) {
                    sb.append(" and lea.business_no in (" + info.getBusinessNoFilter() + ") ");
                }
            } else {
                sb.append(" and 1=2 ");
            }
        }

        public String getCheckLeaguerInfo(final Map<String, Object> param) {
            String strNo = (String) param.get("strNo");
            String businessNos = (String) param.get("businessNos");
            String businessNo = (String) param.get("businessNo");

            StringBuffer sb = new StringBuffer();
            sb.append(" select ");
            sb.append("   lea.*,bus.digest_key as digestKey ");
            sb.append("  from  vip_leaguer_info lea ");
            sb.append("   LEFT JOIN vip_business_info bus ON bus.business_no=lea.business_no ");
            sb.append("  where 1=1 ");
            if (StringUtils.isNotBlank(strNo)) {
                sb.append(" and (lea.leaguer_no = #{strNo} or lea.origin_user_no = #{strNo} or lea.mobile_phone = #{strNo} )");
            }
            if (StringUtils.isNotBlank(businessNo)) {
                sb.append(" and lea.business_no = #{businessNo} ");
            }
            if (StringUtils.isNotBlank(businessNos)) {
                if (!"-1".equals(businessNos)) {
                    sb.append(" and lea.business_no in (" + businessNos + ") ");
                }
            } else {
                sb.append(" and 1=2 ");
            }
            return sb.toString();
        }

        public String getBusinessInfoByJurisdiction(final Map<String, Object> param) {
            String businessNo = (String) param.get("businessNo");
            String businessNos = (String) param.get("businessNos");
            StringBuffer sb = new StringBuffer();
            sb.append(" select business_no,digest_key  ");
            sb.append("  from  vip_business_info ");
            sb.append(" where business_no = #{businessNo} ");
            if (StringUtils.isNotBlank(businessNos)) {
                if (!"-1".equals(businessNos)) {
                    sb.append(" and business_no in (" + businessNos + ") ");
                }
            } else {
                sb.append(" and 1=2 ");
            }
            sb.append(" limit 1 ");
            return sb.toString();
        }
    }
}
