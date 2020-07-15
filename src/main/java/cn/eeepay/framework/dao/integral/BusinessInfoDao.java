package cn.eeepay.framework.dao.integral;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.integral.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/7/2/002.
 * @author lius
 */
public interface BusinessInfoDao {

    /**
     * 获取所有业务编号，名称
     * @return
     */
    @Select("SELECT id,business_no,business_name FROM vip_business_info ")
    @ResultType(BusinessInfo.class)
    List<BusinessInfo> getBusinessInfoListAll();

    @SelectProvider(type=BusinessInfoDao.SqlProvider.class,method="getSelectPageList")
    @ResultType(BusinessInfo.class)
    List<BusinessInfo> getSelectPageList(@Param("info") BusinessInfo info,
                                         @Param("page")Page<BusinessInfo> page,
                                         @Param("businessNos")String businessNos);

    /**
     * 新增业务组
     */
    @Insert(
            " INSERT INTO vip_business_info " +
                    " (business_no,business_name,digest_key,ip_white_list, " +
                    "  ip_check_switch,status,create_time) " +
                    " VALUES " +
                    " ( #{info.businessNo},#{info.businessName},#{info.digestKey},#{info.ipWhiteList}," +
                    "   0,1,NOW() )"
    )
    int addBusinessInfo(@Param("info")BusinessInfo info);

    /**
     *根据编码查询业务组
     */
    @Select(
            "select * from vip_business_info where business_no=#{businessNo}"
    )
    List<BusinessInfo> checkDataNo(@Param("businessNo")String businessNo);

    /**
     *根据名称查询业务组
     */
    @Select(
            "select * from vip_business_info where business_name=#{businessName}"
    )
    List<BusinessInfo> checkDataName(@Param("businessName")String businessName);

    /**
     *根据id查询业务组
     */
    @Select(
            "select * from vip_business_info where business_no=#{businessNo}"
    )
    BusinessInfo BusinessInfo(@Param("businessNo")String businessNo);

    /**
     *根据id查询业务组
     */
    @Select(
            "select * from vip_business_info where business_name=#{businessName} limit 1 "
    )
    BusinessInfo BusinessInfoByName(@Param("businessName")String businessName);

    /**
     * 获取会员等级列表
     */
    @Select(
            "select id as id,business_no as businessNo,vip_level as level,vip_level_name as levelName, " +
                    " vip_min_num as minNum,vip_max_num as maxNum,section_remark as sectionRemark " +
                    " from vip_grow_level_config " +
                    " where business_no=#{businessNo} ORDER BY vip_level "
    )
    List<LevelInfo> getGrowLevelInfoList(@Param("businessNo")String businessNo);

    /**
     * 获取M等级列表
     */
    @Select(
            "select id as id,business_no as businessNo,m_level as level,m_level_name as levelName, " +
                    " m_min_num as minNum,m_max_num as maxNum,section_remark as sectionRemark " +
                    " from vip_m_level_config where business_no=#{businessNo} ORDER BY m_level "
    )
    List<LevelInfo> getMLevelInfoList(@Param("businessNo")String businessNo);

    /**
     *更新基础信息
     */
    @Update(
            " UPDATE vip_business_info set " +
                    " level_desc=#{info.levelDesc},score_desc=#{info.scoreDesc}, " +
                    " level_effe_date=#{info.levelEffeDate},level_num=#{info.levelNum}, " +
                    " level_rate=#{info.levelRate} " +
                    " where business_no=#{info.businessNo} "
    )
    int updateBusinessBasic(@Param("info")BusinessInfo info);

    /**
     * 新增会员等级
     */
    @Insert(
            " INSERT INTO vip_grow_level_config " +
                    " (business_no,vip_level,vip_level_name,vip_min_num, " +
                    "  vip_max_num,section_remark,create_time) " +
                    " VALUES " +
                    " (#{info.businessNo},#{info.level},#{info.levelName},#{info.minNum}, " +
                    "  #{info.maxNum},#{info.sectionRemark},NOW())"
    )
    int addGrowLevelInfo(@Param("info")LevelInfo info);

    /**
     * 更新会员等级
     */
    @Update(
            " UPDATE vip_grow_level_config set " +
                    " business_no=#{info.businessNo},vip_level=#{info.level},vip_level_name=#{info.levelName}, " +
                    " vip_min_num=#{info.minNum},vip_max_num=#{info.maxNum},section_remark=#{info.sectionRemark} " +
                    " where id=#{info.id} "
    )
    int updateGrowLevelInfo(@Param("info")LevelInfo info);

    /**
     * 新增M等级
     */
    @Insert(
            " INSERT INTO vip_m_level_config " +
                    " (business_no,m_level,m_level_name,m_min_num, " +
                    "  m_max_num,section_remark,create_time) " +
                    " VALUES " +
                    " (#{info.businessNo},#{info.level},#{info.levelName},#{info.minNum}, " +
                    "  #{info.maxNum},#{info.sectionRemark},NOW())"
    )
    int addMLevelInfo(@Param("info")LevelInfo info);

    /**
     * 更新M等级
     */
    @Update(
            " UPDATE vip_m_level_config set " +
                    " business_no=#{info.businessNo},m_level=#{info.level},m_level_name=#{info.levelName}, " +
                    " m_min_num=#{info.minNum},m_max_num=#{info.maxNum},section_remark=#{info.sectionRemark} " +
                    " where id=#{info.id} "
    )
    int updateMLevelInfo(@Param("info")LevelInfo info);

    /**
     * 删除会员等级
     */
    @DeleteProvider(type=BusinessInfoDao.SqlProvider.class,method="deleteGrowLevelInfo")
    @ResultType(Integer.class)
    int deleteGrowLevelInfo(@Param("strs")String strs);
    /**
     * 删除M等级
     */
    @DeleteProvider(type=BusinessInfoDao.SqlProvider.class,method="deleteMLevelInfo")
    @ResultType(Integer.class)
    int deleteMLevelInfo(@Param("strs")String strs);

    /**
     *查询业务组下的任务列表
     */
    @SelectProvider(type=BusinessInfoDao.SqlProvider.class,method="getBusinessTaskList")
    @ResultType(BusinessTask.class)
    List<BusinessTask> getBusinessTaskList(@Param("businessNo")String businessNo);

    @Select(
            "select * from vip_business_task_config " +
                    " where business_no=#{businessNo} and task_no=#{taskNo}"
    )
    @ResultType(BusinessTask.class)
    BusinessTask checkBusinessTask(@Param("businessNo")String businessNo,@Param("taskNo")String taskNo);

    /**
     * 新增业务组-任务
     */
    @Insert(
            " INSERT INTO vip_business_task_config " +
                    " (task_type,task_no,task_name,task_desc,task_params," +
                    "  restrict_num,restrict_frequency,frequency_type,status,create_time,creater,business_no,img_url,task_overview,grant_type) " +
                    " VALUES " +
                    " (#{info.taskType},#{info.taskNo},#{info.taskName},#{info.taskDesc},#{info.taskParams}," +
                    "  #{info.restrictNum},#{info.restrictFrequency},#{info.frequencyType},0,NOW(),#{info.creater},#{info.businessNo},#{info.imgUrl},#{info.taskOverview},#{info.grantType})"
    )
    int addBusinessTask(@Param("info")BusinessTask info);

    /**
     * 查询业务组-任务
     */
    @Select(
            " select * from vip_business_task_config where id=#{id} and business_no=#{businessNo}"
    )
    BusinessTask getBusinessTask(@Param("id")Long id,@Param("businessNo")String business_no);


    /***
     * 修改任务
     * @param info
     * @return
     */
    @UpdateProvider(type=BusinessInfoDao.SqlProvider.class,method="editBusinessTask")
    int editBusinessTask(@Param("info")BusinessTask info);

    @Delete(
            " delete from vip_business_task_config where id=#{id} and business_no=#{businessNo} "
    )
    int deleteBusinessTask(@Param("id")Long id,@Param("businessNo")String businessNo);

    @SelectProvider(type=BusinessInfoDao.SqlProvider.class,method="getBusinessInfoListByUser")
    @ResultType(BusinessInfo.class)
    List<BusinessInfo> getBusinessInfoListByUser(@Param("businessNos")String businessNos);

    @Update(
            "update vip_business_task_config set " +
                    " status=#{sta} " +
                    " where id=#{id} and business_no=#{businessNo} "
    )
    int closeBusinessTask(@Param("id")Long id,@Param("businessNo") String businessNo, @Param("sta")int sta);


    @SelectProvider(type=BusinessInfoDao.SqlProvider.class,method="businessTaskPreview")
    List<Map<Object,Object>> businessTaskPreview(@Param("info") BusinessVipLevelInfo info,@Param("allVipLevels") List<Map<String,Object>> allVipLevels);

    @SelectProvider(type = BusinessInfoDao.SqlProvider.class,method = "getAllVipLevels")
    @ResultType(List.class)
    List<Map<String,Object>> getAllVipLevels(@Param("info") BusinessVipLevelInfo info);


    @Delete("delete from vip_reward_preview where reward_code=#{rewardCode} and business_no=#{businessNo}")
    int delBusinessTaskPreview(@Param("rewardCode") String rewardCode,@Param("businessNo")String businessNo);

    @Insert("insert into vip_reward_preview (reward_name,business_no,level,is_select,create_time,reward_code) " +
            "values" +
            "(#{info.rewardName},#{info.businessNo},#{info.level},#{info.isSelect},now(),#{info.rewardCode})")
    int addBusinessTaskPreview(@Param("info") BusinessVipLevelInfo info);


    @Update("update vip_reward_preview set is_select=#{info.isSelect},reward_name=#{info.rewardName} where   business_no=#{info.businessNo} " +
            "and level=#{info.level} and reward_code=#{info.rewardCode}")
    @ResultType(Integer.class)
    int updateVipRewardPreview(@Param("info") BusinessVipLevelInfo info);



    class SqlProvider{
        public String getAllVipLevels(final Map<String, Object> param){
            BusinessVipLevelInfo info = (BusinessVipLevelInfo) param.get("info");
            StringBuffer sql = new StringBuffer();
            sql.append("select vip_level_name name,vip_level level from vip_grow_level_config where business_no=#{info.businessNo} ");
            if(StringUtils.isNotBlank(info.getBusinessNoFilter())){
                if(!"-1".equals(info.getBusinessNoFilter())){
                    sql.append(" and business_no in ("+info.getBusinessNoFilter()+") ");
                }
            }else{
                sql.append(" and 1=2 ");
            }
            sql.append(" order by vip_level ");
            return sql.toString();
        }

        public String editBusinessTask(final Map<String, Object> param){
            final BusinessTask info = (BusinessTask) param.get("info");
            String sql = " UPDATE vip_business_task_config set " +
                    " task_type=#{info.taskType},task_name=#{info.taskName},task_desc=#{info.taskDesc}, " +
                    " restrict_num=#{info.restrictNum},restrict_frequency=#{info.restrictFrequency},frequency_type=#{info.frequencyType}," +
                    "task_overview=#{info.taskOverview} ,grant_type=#{info.grantType},task_params=#{info.taskParams}" ;
            if(StringUtils.isNotBlank(info.getImgUrl())){
                sql += ",img_url=#{info.imgUrl}";
            }
            sql += " where id=#{info.id} and business_no=#{info.businessNo} ";
            return sql;
        }

        public String businessTaskPreview(final Map<String, Object> param){
            System.out.println( param instanceof Map);
            final BusinessVipLevelInfo info = (BusinessVipLevelInfo) param.get("info");
            final List<Map<String,Object>> allVipLevels = (List) param.get("allVipLevels");

            return new SQL(){{
                if(allVipLevels!=null && allVipLevels.size()>0){
                    int i = 0;
                    for (Map<String, Object> allVipLevel : allVipLevels) {
                        String name = allVipLevel.get("name")==null?"":allVipLevel.get("name").toString();
                        SELECT("max(case t.vip_level_name when '"+name+"' then t.is_select else 0 end) level"+i+" ");
                        i++;
                    }
                }
                SELECT("reward_name rewardName,reward_code rewardCode");
                String sql = "( select p.reward_name,p.is_select,c.vip_level_name,p.create_time,p.reward_code from vip_reward_preview p " +
                        "right join vip_grow_level_config c " +
                        "on c.vip_level = p.level " +
                        "where   p.business_no=#{info.businessNo} and c.business_no = #{info.businessNo} ";
                if(StringUtils.isNotBlank(info.getBusinessNoFilter())){
                    if(!"-1".equals(info.getBusinessNoFilter())){
                        sql +=" and p.business_no in ("+info.getBusinessNoFilter()+") ";
                    }
                }else{
                    sql +=" and 1=2 ";
                }
                 sql +=") t";
                FROM(sql);
                GROUP_BY(" t.reward_code");
                ORDER_BY("t.reward_code");
            }}.toString();
        }

        public String getSelectPageList(final Map<String, Object> param) {
            final BusinessInfo info = (BusinessInfo) param.get("info");
            final String businessNos = (String) param.get("businessNos");

            String sql = new SQL(){{
                SELECT(" bus.* ");
                FROM(" vip_business_info bus ");
                if(StringUtils.isNotBlank(info.getBusinessNo())){
                    WHERE(" bus.business_no = #{info.businessNo} ");
                }
                if(StringUtils.isNotBlank(info.getBusinessName())){
                    WHERE(" bus.business_name like concat(#{info.businessName},'%') ");
                }
                if(StringUtils.isNotBlank(businessNos)){
                    if(!"-1".equals(businessNos)){
                        WHERE(" bus.business_no in ("+businessNos+") ");
                    }
                }else{
                    WHERE(" 1=2 ");
                }
                ORDER_BY("bus.create_time desc");
            }}.toString();
            return sql;
        }
        public String deleteGrowLevelInfo(final Map<String, Object> param) {
            final String strs = (String) param.get("strs");
            return "delete from vip_grow_level_config where id in ("+strs+")";
        }
        public String deleteMLevelInfo(final Map<String, Object> param) {
            final String strs = (String) param.get("strs");
            return "delete from vip_m_level_config where id in ("+strs+")";
        }
        public String getBusinessTaskList(final Map<String, Object> param) {
            final String businessNo = (String) param.get("businessNo");
            return new SQL(){{
                SELECT(" tas.*,tt.task_type_name taskTypeName ");
                FROM(" vip_business_task_config tas ");
                LEFT_OUTER_JOIN(" vip_task_type_config tt ON tt.task_type_no=tas.task_type ");
                if(StringUtils.isNotBlank(businessNo)){
                    WHERE(" tas.business_no = #{businessNo} ");
                }
                ORDER_BY("tas.create_time desc");
            }}.toString();
        }
        public String getBusinessInfoListByUser(final Map<String, Object> param) {
            final String businessNos = (String) param.get("businessNos");
            String sql = new SQL(){{
                SELECT(" bus.* ");
                FROM(" vip_business_info bus ");
                if(StringUtils.isNotBlank(businessNos)){
                    if(!"-1".equals(businessNos)){
                        WHERE(" bus.business_no in ("+businessNos+") ");
                    }
                }else{
                    WHERE(" 1=2 ");
                }
            }}.toString();
            return sql;
        }
    }
}
