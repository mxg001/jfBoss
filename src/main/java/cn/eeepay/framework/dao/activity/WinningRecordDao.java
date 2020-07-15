package cn.eeepay.framework.dao.activity;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.activity.WinningRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

public interface WinningRecordDao {

    @SelectProvider(type= WinningRecordDao.SqlProvider.class,method="getSelectPageList")
    @ResultType(WinningRecord.class)
    List<WinningRecord> getSelectPageList(@Param("info")WinningRecord info,@Param("page") Page<WinningRecord> page);

    @SelectProvider(type= WinningRecordDao.SqlProvider.class,method="getWinningRecordCount")
    @ResultType(Integer.class)
    int getWinningRecordCount(@Param("info")WinningRecord info);

    @SelectProvider(type= WinningRecordDao.SqlProvider.class,method="getSelectPageList")
    @ResultType(WinningRecord.class)
    List<WinningRecord> importDetail(@Param("info")WinningRecord info);



    class SqlProvider{

        public String getSelectPageList(final Map<String, Object> param) {
            WinningRecord info = (WinningRecord) param.get("info");
            StringBuffer sb=new StringBuffer();
            sb.append(" select ");
            sb.append("   lot.*, act.act_name, ");
            sb.append("   lea.leaguer_name,lea.origin_user_no,lea.mobile_phone ");
            sb.append("  from  vip_lottery_record lot ");
            sb.append("    INNER JOIN  vip_leaguer_info lea ON lea.leaguer_no=lot.leaguer_no ");
            sb.append("    LEFT JOIN  vip_activity act ON act.act_code=lot.act_code ");

            sqlWhereString(sb,info);
            sb.append(" ORDER BY lot.create_time desc ");
            return  sb.toString();
        }
        public String getWinningRecordCount(final Map<String, Object> param) {
            WinningRecord info = (WinningRecord) param.get("info");
            StringBuffer sb=new StringBuffer();
            sb.append(" select ");
            sb.append("   count(*) ");
            sb.append("  from  vip_lottery_record lot ");
            sb.append("    INNER JOIN  vip_leaguer_info lea ON lea.leaguer_no=lot.leaguer_no ");
            sqlWhereString(sb,info);
            return  sb.toString();
        }

        private void sqlWhereString(StringBuffer sb,WinningRecord info){
            sb.append(" where 1=1 ");
            if(StringUtils.isNotBlank(info.getLeaguerNo())){
                sb.append(" and lot.leaguer_no = #{info.leaguerNo} ");
            }
            if(StringUtils.isNotBlank(info.getOriginUserNo())){
                sb.append(" and lea.origin_user_no = #{info.originUserNo} ");
            }
            if(StringUtils.isNotBlank(info.getMobilePhone())){
                sb.append(" and lea.mobile_phone = #{info.mobilePhone} ");
            }
            if(info.getCreateTimeBegin()!=null){
                sb.append(" and lot.create_time>=#{info.createTimeBegin}");
            }
            if(info.getCreateTimeEnd()!=null){
                sb.append(" and lot.create_time<=#{info.createTimeEnd}");
            }
            if(StringUtils.isNotBlank(info.getLotteryStatus())){
                sb.append(" and lot.lottery_status = #{info.lotteryStatus} ");
            }
            if(StringUtils.isNotBlank(info.getAwardDesc())){
                sb.append(" and lot.award_desc like concat('%',#{info.awardDesc},'%') ");
            }
            if(StringUtils.isNotBlank(info.getAwardName())){
                sb.append(" and lot.award_name like concat('%',#{info.awardName},'%') ");
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
