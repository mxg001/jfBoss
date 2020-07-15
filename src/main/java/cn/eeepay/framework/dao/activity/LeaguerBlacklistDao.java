package cn.eeepay.framework.dao.activity;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.activity.AwardBlack;
import cn.eeepay.framework.model.activity.LeaguerBlacklist;
import cn.eeepay.framework.model.activity.PrizeInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface LeaguerBlacklistDao {

    @Insert(
            " INSERT INTO vip_leaguer_blacklist(leaguer_no,service_no,create_time,operater) " +
                    " VALUES " +
                    " (#{info.leaguerNo},#{info.serviceNo},#{info.createTime},#{info.operater}) "
    )
    public int insertBlackList(@Param("info") LeaguerBlacklist info);


    @SelectProvider(type = SqlProvider.class, method = "findListByActDetCode")
    @ResultType(LeaguerBlacklist.class)
    public List<LeaguerBlacklist> findListByActDetCode(@Param("actDetCode") String actDetCode, @Param("leaguerNo") String leaguerNo, @Param("page") Page page);

    @Select("select * from vip_leaguer_blacklist where leaguer_no = #{leaguerNo} and service_no = #{actDetCode}")
    @ResultType(LeaguerBlacklist.class)
    public LeaguerBlacklist findByActDetCode(@Param("actDetCode") String actDetCode, @Param("leaguerNo") String leaguerNo);


    @Delete("delete from vip_leaguer_blacklist where leaguer_no = #{leaguerNo} and service_no = #{serviceNo}")
    int deleteActivityBlack(@Param("serviceNo") String serviceNo, @Param("leaguerNo") String leaguerNo);

    class SqlProvider {
        public String findListByActDetCode(final Map<String, Object> param) {
            String actDetCode = (String) param.get("actDetCode");
            String leaguerNo = (String) param.get("leaguerNo");
            return new SQL() {{
                SELECT("vlb.*,vli.leaguer_name,vli.origin_user_no");
                FROM("vip_leaguer_blacklist vlb left join vip_leaguer_info vli " +
                        "on vli.leaguer_no = vlb.leaguer_no");
                WHERE("vlb.service_no = #{actDetCode}");
                if (StringUtils.isNotBlank(leaguerNo)) {
                    WHERE("vlb.leaguer_no = #{leaguerNo}");
                }
                ORDER_BY("vlb.create_time desc ");
            }}.toString();
        }
    }

}
