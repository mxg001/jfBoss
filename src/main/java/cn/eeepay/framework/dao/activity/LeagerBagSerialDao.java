package cn.eeepay.framework.dao.activity;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.activity.LeagerBagSerial;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface LeagerBagSerialDao {

    @SelectProvider(type = LeagerBagSerialDao.SqlProvider.class, method = "findListByPage")
    @ResultType(LeagerBagSerial.class)
    public List<LeagerBagSerial> findListByPage(@Param("info") LeagerBagSerial info, @Param("page") Page<LeagerBagSerial> page);

    @SelectProvider(type = LeagerBagSerialDao.SqlProvider.class, method = "findListByPage")
    @ResultType(LeagerBagSerial.class)
    public List<LeagerBagSerial> findList(@Param("info") LeagerBagSerial info);


    class SqlProvider {
        public String findListByPage(final Map<String, Object> param) {
            LeagerBagSerial info = (LeagerBagSerial) param.get("info");
            return new SQL() {{
                SELECT("lbs.*,vli.leaguer_name,vli.mobile_phone,va.act_name,vli.origin_user_no");
                FROM("vip_leager_bag_serial lbs left join vip_leaguer_info vli " +
                        "on vli.leaguer_no = lbs.leaguer_no left join vip_activity va  on va.act_code = lbs.act_code");
                if(StringUtils.isNotBlank(info.getAccType()) && !info.getAccType().equals("0")){
                    WHERE("lbs.acc_type = #{info.accType}");
                }
                if (StringUtils.isNotBlank(info.getLeaguerNo())) {
                    WHERE("lbs.leaguer_no = #{info.leaguerNo}");
                }
                if (StringUtils.isNotBlank(info.getOriginUserNo())) {
                    WHERE("vli.origin_user_no = #{info.originUserNo}");
                }
                if (StringUtils.isNotBlank(info.getCreateTimeStart())) {
                    WHERE("lbs.create_time >= #{info.createTimeStart}");
                }
                if (StringUtils.isNotBlank(info.getCreateTimeEnd())) {
                    WHERE("lbs.create_time <= #{info.createTimeEnd}");
                }
                if (StringUtils.isNotBlank(info.getMobilePhone())) {
                    WHERE("vli.mobile_phone = #{info.mobilePhone}");
                }
                ORDER_BY("lbs.create_time desc ");
            }}.toString();
        }
    }

}
