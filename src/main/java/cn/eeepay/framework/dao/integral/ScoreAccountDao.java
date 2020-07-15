package cn.eeepay.framework.dao.integral;

import cn.eeepay.framework.model.integral.ScoreAccountCount;
import cn.eeepay.framework.model.integral.ScoreAccountInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/7/11/011.
 * @author liuks
 * 积分出入账 dao
 */
public interface ScoreAccountDao {

    /**
     * 会员详情-积分查询
     * @param info
     * @return
     */
    @SelectProvider(type=ScoreAccountDao.SqlProvider.class,method="getUserAccountList")
    @ResultType(ScoreAccountInfo.class)
    List<ScoreAccountInfo> getUserAccountList(@Param("info")ScoreAccountInfo info);

    /**
     * 会员详情-积分查询 统计条数
     * @param info
     * @return
     */
    @SelectProvider(type=ScoreAccountDao.SqlProvider.class,method="getUserAccountListCount")
    int getUserAccountListCount(@Param("info")ScoreAccountInfo info);

    /**
     * 积分查询
     * @param info
     * @return
     */
    @SelectProvider(type=ScoreAccountDao.SqlProvider.class,method="getSelectPageList")
    @ResultType(ScoreAccountInfo.class)
    List<ScoreAccountInfo> getSelectPageList(@Param("info")ScoreAccountInfo info);

    /**
     * 积分查询统计
     * @param info
     * @return
     */
    @SelectProvider(type=ScoreAccountDao.SqlProvider.class,method="getSelectPageListCount")
    @ResultType(ScoreAccountCount.class)
    ScoreAccountCount getSelectPageListCount(@Param("info")ScoreAccountInfo info);

    class SqlProvider{

        public String getSelectPageList(final Map<String, Object> param) {
            ScoreAccountInfo info = (ScoreAccountInfo) param.get("info");
            StringBuffer sb=new StringBuffer();
            sb.append(" select ");
            sb.append("   acc.*, ");
            sb.append("   lea.leaguer_name,lea.origin_user_no,lea.mobile_phone, ");
            sb.append("   (select bus.business_name from vip_business_info bus where bus.business_no=lea.business_no ) business_name, ");
            sb.append("   bat.effe_date as scoreBatchDate ");

            sb.append("  from  vip_score_account_serial acc ");
            sb.append("    LEFT JOIN  vip_leaguer_info lea ON lea.leaguer_no=acc.leaguer_no ");
            sb.append("    LEFT JOIN  vip_score_batch bat ON bat.batch_no=acc.score_batch_no ");

            sqlWhereStringPage(sb,info);
            sb.append(" ORDER BY acc.create_time desc,acc.id desc ");

            if(info.getPageFirst()!=null&&info.getPageSize()!=null){
                sb.append(" LIMIT "+info.getPageFirst()+","+info.getPageSize());
            }
            return sb.toString();
        }
        public String getSelectPageListCount(final Map<String, Object> param) {
            ScoreAccountInfo info = (ScoreAccountInfo) param.get("info");
            StringBuffer sb=new StringBuffer();
            sb.append(" select ");
            sb.append("   count(*) total, ");
            sb.append("   sum(IF(acc.acc_type='1',acc.acc_score,0)) addScoreNum, ");
            sb.append("   sum(IF(acc.acc_type='2' and acc.from_type!='4' and acc.from_type!='5',acc.acc_score,0)) usedScoreNum, ");//总使用积分，不包含过期，作废的
            sb.append("   sum(IF(acc.acc_type='2' and acc.from_type ='5',acc.acc_score,0)) expScoreNum ");
//            sb.append("   sum(IF(acc.acc_type='2' and acc.from_type ='4',acc.acc_score,0)) cancelScoreNum ");

            sb.append("  from  vip_score_account_serial acc ");
            sb.append("    LEFT JOIN  vip_leaguer_info lea ON lea.leaguer_no=acc.leaguer_no ");
            sb.append("    LEFT JOIN  vip_score_batch bat ON bat.batch_no=acc.score_batch_no ");

            sqlWhereStringPage(sb,info);

            return sb.toString();
        }


        private void sqlWhereStringPage(StringBuffer sb,ScoreAccountInfo info){
            sb.append("  where 1=1 ");

            if(StringUtils.isNotBlank(info.getLeaguerNo())){
                sb.append(" and acc.leaguer_no = #{info.leaguerNo} ");
            }
            if(StringUtils.isNotBlank(info.getLeaguerName())){
                sb.append(" and lea.leaguer_name like concat(#{info.leaguerName},'%') ");
            }
            if(StringUtils.isNotBlank(info.getMobilePhone())){
                sb.append(" and lea.mobile_phone = #{info.mobilePhone} ");
            }
            if(StringUtils.isNotBlank(info.getOriginUserNo())){
                sb.append(" and lea.origin_user_no = #{info.originUserNo} ");
            }
            if(StringUtils.isNotBlank(info.getScoreBatchNo())){
                sb.append(" and acc.score_batch_no = #{info.scoreBatchNo} ");
            }
            if(StringUtils.isNotBlank(info.getAccType())){
                sb.append(" and acc.acc_type = #{info.accType} ");
            }
            if(StringUtils.isNotBlank(info.getFromType())){
                sb.append(" and acc.from_type = #{info.fromType} ");
            }
            if(StringUtils.isNotBlank(info.getFromSystem())){
                sb.append(" and acc.from_system = #{info.fromSystem} ");
            }
            if(StringUtils.isNotBlank(info.getServiceOrderNo())){
                sb.append(" and acc.service_order_no = #{info.serviceOrderNo} ");
            }
            if(info.getCreateTimeBegin()!=null){
                sb.append(" and acc.create_time>=#{info.createTimeBegin}");
            }
            if(info.getCreateTimeEnd()!=null){
                sb.append(" and acc.create_time<=#{info.createTimeEnd}");
            }
            if(StringUtils.isNotBlank(info.getBusinessNo())){
                sb.append(" and acc.business_no = #{info.businessNo} ");
            }
            if(info.getScoreBatchDate()!=null){
                sb.append(" and bat.effe_date = #{info.scoreBatchDate} ");
            }
            sb.append(" and acc.acc_status = '2' ");

            if(StringUtils.isNotBlank(info.getBusinessNoFilter())){
                if(!"-1".equals(info.getBusinessNoFilter())){
                    sb.append(" and acc.business_no in ("+info.getBusinessNoFilter()+") ");
                }
            }else{
                sb.append(" and 1=2 ");
            }
        }


        public String getUserAccountList(final Map<String, Object> param) {
            ScoreAccountInfo info = (ScoreAccountInfo) param.get("info");

            StringBuffer sb=new StringBuffer();
            sb.append(" select ");
            sb.append("   acc.* ");
            sb.append("  from  vip_score_account_serial acc ");

            sqlWhereString(sb,info);

            sb.append(" ORDER BY acc.create_time desc,acc.id desc ");
            if(info.getPageFirst()!=null&&info.getPageSize()!=null){
                sb.append(" LIMIT "+info.getPageFirst()+","+info.getPageSize());
            }
            return sb.toString();
        }
        public String getUserAccountListCount(final Map<String, Object> param) {
            ScoreAccountInfo info = (ScoreAccountInfo) param.get("info");
            StringBuffer sb=new StringBuffer();
            sb.append(" select ");
            sb.append("   count(*) ");
            sb.append("  from  vip_score_account_serial acc ");
            sqlWhereString(sb,info);
            return sb.toString();
        }

        private void sqlWhereString(StringBuffer sb,ScoreAccountInfo info){
            sb.append("  where 1=1 ");
            if(StringUtils.isNotBlank(info.getLeaguerNo())){
                sb.append(" and acc.leaguer_no = #{info.leaguerNo} ");
            }
            if(StringUtils.isNotBlank(info.getAccType())){
                sb.append(" and acc.acc_type = #{info.accType} ");
            }
            if(StringUtils.isNotBlank(info.getFromType())){
                sb.append(" and acc.from_type = #{info.fromType} ");
            }
            if(info.getCreateTimeBegin()!=null){
                sb.append(" and acc.create_time>=#{info.createTimeBegin}");
            }
            if(info.getCreateTimeEnd()!=null){
                sb.append(" and acc.create_time<=#{info.createTimeEnd}");
            }
            if(StringUtils.isNotBlank(info.getBusinessNo())){
                sb.append(" and acc.business_no = #{info.businessNo} ");
            }
            sb.append(" and acc.acc_status = '2' ");
        }
    }
}
