package cn.eeepay.framework.dao.integral;

import cn.eeepay.framework.model.integral.BusinessFlow;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/7/15/015.
 * @author  liuks
 * 业务流水dao
 */
public interface BusinessFlowDao {

    /**
     * 业务流水查询
     * @param info
     * @return
     */
    @SelectProvider(type=BusinessFlowDao.SqlProvider.class,method="getSelectPageList")
    @ResultType(BusinessFlow.class)
    List<BusinessFlow> getSelectPageList(@Param("info") BusinessFlow info);

    /**
     * 业务流水查询统计
     * @param info
     * @return
     */
    @SelectProvider(type=BusinessFlowDao.SqlProvider.class,method="getSelectPageListCount")
    int getSelectPageListCount(@Param("info")BusinessFlow info);

    @Select(
            "select rew.reward_order_no,rew.reward_status,lea.business_no,bus.digest_key " +
                    " from vip_task_reward_info rew " +
                    "    INNER JOIN  vip_leaguer_info lea ON lea.leaguer_no=rew.leaguer_no " +
                    "    LEFT JOIN  vip_business_info bus ON bus.business_no=lea.business_no " +
                    " where " +
                    "  rew.reward_order_no=#{rewardOrderNo} and lea.business_no=#{businessNo} "
    )
    BusinessFlow getLowerHair(@Param("rewardOrderNo")String rewardOrderNo,@Param("businessNo") String businessNo);

    class SqlProvider{

        public String getSelectPageList(final Map<String, Object> param) {
            BusinessFlow info = (BusinessFlow) param.get("info");
            StringBuffer sb=new StringBuffer();
            sb.append(" select ");
            sb.append("   rew.id ,rew.service_order_no task_order_no,rew.leaguer_no leaguer_no,lea.business_no business_no,rew.create_time, ");
            sb.append("   tas.last_update_time,tas.origin_order_no,rew.service_no task_no,rew.service_name task_name, ");
            sb.append("   rew.reward_order_no,rew.reward_no,rew.reward_type,rew.reward_status,rew.calc_type, ");
            sb.append("   rew.origin_num,rew.reward_num,rew.reward_template,rew.from_type,rew.from_system, ");
            sb.append("   rew.from_desc,rew.notify_sure,rew.notify_status,rew.notify_time,rew.notify_data, ");
            sb.append("   rew.notify_num,rew.reward_name, ");
            sb.append("   (select goods.goods_name from vip_goods_config goods where goods.goods_code=rew.calc_type ) calcTypeName, ");
            sb.append("   (select bus.business_name from vip_business_info bus where bus.business_no=lea.business_no ) business_name, ");
            sb.append("   lea.leaguer_name,lea.origin_user_no,lea.mobile_phone ");

            sb.append("  from vip_task_reward_info rew ");
            sb.append("    LEFT JOIN  vip_leaguer_task_order  tas ON tas.task_order_no=rew.service_order_no ");
            sb.append("    LEFT JOIN  vip_leaguer_info lea ON lea.leaguer_no=rew.leaguer_no ");

            sqlWhereStringPage(sb,info);

            sb.append(" ORDER BY rew.create_time desc ");

            if(info.getPageFirst()!=null&&info.getPageSize()!=null){
                sb.append(" LIMIT "+info.getPageFirst()+","+info.getPageSize());
            }
            return sb.toString();
        }
        public String getSelectPageListCount(final Map<String, Object> param) {
            BusinessFlow info = (BusinessFlow) param.get("info");
            StringBuffer sb=new StringBuffer();
            sb.append(" select ");
            sb.append("   count(*) ");
            sb.append("  from vip_task_reward_info rew ");
            sb.append("    LEFT JOIN  vip_leaguer_task_order  tas ON tas.task_order_no=rew.service_order_no ");
            sb.append("    LEFT JOIN  vip_leaguer_info lea ON lea.leaguer_no=rew.leaguer_no ");
            sqlWhereStringPage(sb,info);
            return sb.toString();
        }

        private void sqlWhereStringPage(StringBuffer sb,BusinessFlow info){
            sb.append("  where 1=1 ");
            if(StringUtils.isNotBlank(info.getRewardType())){
                sb.append(" and rew.reward_type = #{info.rewardType} ");
            }
            if(StringUtils.isNotBlank(info.getRewardName())){
                sb.append(" and rew.reward_name like concat(#{info.rewardName},'%') ");
            }
            if(StringUtils.isNotBlank(info.getTaskOrderNo())){
                sb.append(" and rew.service_order_no = #{info.taskOrderNo} ");
            }
            if(StringUtils.isNotBlank(info.getBusinessNo())){
                sb.append(" and lea.business_no = #{info.businessNo} ");
            }
            if(StringUtils.isNotBlank(info.getLeaguerNo())){
                sb.append(" and rew.leaguer_no = #{info.leaguerNo} ");
            }
            if(StringUtils.isNotBlank(info.getLeaguerName())){
                sb.append(" and lea.leaguer_name like concat(#{info.leaguerName},'%') ");
            }
            if(StringUtils.isNotBlank(info.getMobilePhone())){
                sb.append(" and lea.mobile_phone = #{info.mobilePhone} ");
            }
            if(StringUtils.isNotBlank(info.getRewardStatus())){
                sb.append(" and rew.reward_status = #{info.rewardStatus} ");
            }
            if(StringUtils.isNotBlank(info.getFromType())){
                sb.append(" and rew.from_type = #{info.fromType} ");
            }
            if(StringUtils.isNotBlank(info.getFromSystem())){
                sb.append(" and rew.from_system = #{info.fromSystem} ");
            }
            if(StringUtils.isNotBlank(info.getOriginOrderNo())){
                sb.append(" and tas.origin_order_no = #{info.originOrderNo} ");
            }
            if(info.getCreateTimeBegin()!=null){
                sb.append(" and rew.create_time>=#{info.createTimeBegin}");
            }
            if(info.getCreateTimeEnd()!=null){
                sb.append(" and rew.create_time<=#{info.createTimeEnd}");
            }
            if(StringUtils.isNotBlank(info.getBusinessNoFilter())){
                if(!"-1".equals(info.getBusinessNoFilter())){
                    sb.append(" and lea.business_no in ("+info.getBusinessNoFilter()+") ");
                }
            }else{
                sb.append(" and 1=2 ");
            }
        }
    }
}
