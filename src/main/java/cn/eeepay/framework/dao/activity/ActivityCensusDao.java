package cn.eeepay.framework.dao.activity;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.activity.ActivityCensus;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

public interface ActivityCensusDao {

    @SelectProvider(type= ActivityCensusDao.SqlProvider.class,method="getSelectPageList")
    @ResultType(ActivityCensus.class)
    List<ActivityCensus> getSelectPageList(@Param("info")ActivityCensus info, @Param("page")Page<ActivityCensus> page);

    @SelectProvider(type= ActivityCensusDao.SqlProvider.class,method="getSelectPageList")
    @ResultType(ActivityCensus.class)
    List<ActivityCensus> importDetail(@Param("info")ActivityCensus info);

    class SqlProvider{

        public String getSelectPageList(final Map<String, Object> param) {
            ActivityCensus info = (ActivityCensus) param.get("info");
            StringBuffer sb=new StringBuffer();
            sb.append(" select ");
            sb.append("   cen.*, ");
            sb.append("   acti.act_name,acti.business_no ");
            sb.append("  from  vip_activity_census cen ");
            sb.append("    INNER JOIN  vip_activity acti ON acti.act_code=cen.act_code ");
            sb.append(" where 1=1 ");

            if(info.getCountDateBegin()!=null){
                sb.append(" and cen.count_date>=#{info.countDateBegin}");
            }
            if(info.getCountDateEnd()!=null){
                sb.append(" and cen.count_date<=#{info.countDateEnd}");
            }
            if(StringUtils.isNotBlank(info.getBusinessNoFilter())){
                if(!"-1".equals(info.getBusinessNoFilter())){
                    sb.append(" and acti.business_no in ("+info.getBusinessNoFilter()+") ");
                }
            }else{
                sb.append(" and 1=2 ");
            }
            sb.append(" ORDER BY cen.create_time desc ");
            return  sb.toString();
        }
    }
}
