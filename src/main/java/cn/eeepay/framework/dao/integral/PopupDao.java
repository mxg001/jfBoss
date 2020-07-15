package cn.eeepay.framework.dao.integral;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.integral.PopupConfig;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface PopupDao {

    @Insert("INSERT INTO vip_popup_config(service_no,business_no,`status`,pop_content,pop_times," +
            "pop_start_time,pop_end_time,pop_img,pop_url,pop_position,`order`,create_time) " +
            "VALUES(#{pop.serviceNo},#{pop.businessNo},#{pop.status},#{pop.popContent},#{pop.popTimes}," +
            "#{pop.popStartTime},#{pop.popEndTime},#{pop.popImg},#{pop.popUrl},#{pop.popPosition}," +
            "#{pop.order},NOW())")
    int add(@Param("pop") PopupConfig popupConfig);

    @Update("UPDATE vip_popup_config SET " +
            "service_no=#{pop.serviceNo},business_no=#{pop.businessNo},`status`=#{pop.status},pop_times=#{pop.popTimes}," +
            "pop_start_time=#{pop.popStartTime},pop_end_time=#{pop.popEndTime},pop_img=#{pop.popImg},pop_url=#{pop.popUrl},pop_position=#{pop.popPosition}," +
            "`order`=#{pop.order} " +
            "WHERE id = #{pop.id}")
    int edit(@Param("pop") PopupConfig popupConfig);

    @SelectProvider(type=PopupDao.SqlProvider.class,method="selectWithPage")
    @ResultType(PopupConfig.class)
    List<PopupConfig> selectWithPage(@Param("page")Page<PopupConfig> page);

    @Delete("DELETE FROM vip_popup_config WHERE id = #{id}")
    int delete(@Param("id") String id);

    @Select("SELECT vpc.*,va.act_name,vbi.business_name " +
            "FROM vip_popup_config vpc " +
            "LEFT JOIN vip_business_info vbi ON vbi.business_no=vpc.business_no " +
            "LEFT JOIN vip_activity va ON vpc.service_no=va.act_code " +
            "WHERE vpc.id = #{id}")
    @ResultType(PopupConfig.class)
    PopupConfig selectById(@Param("id") int id);


    class SqlProvider{

        public String selectWithPage(final Map<String, Object> param){
            String sql = new SQL(){{
                SELECT("vpc.*,va.act_name");
                FROM("vip_popup_config vpc");
                LEFT_OUTER_JOIN("vip_activity va ON vpc.service_no=va.act_code");
                ORDER_BY("vpc.id");
            }}.toString();
            return sql;
        }

    }
}
