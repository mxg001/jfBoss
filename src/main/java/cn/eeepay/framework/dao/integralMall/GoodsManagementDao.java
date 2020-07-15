package cn.eeepay.framework.dao.integralMall;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.integralMall.GoodsManagement;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface GoodsManagementDao {

    /**
     * 物品管理列表查询
     */
    @SelectProvider(type= GoodsManagementDao.SqlProvider.class,method="getSelectPageList")
    @ResultType(GoodsManagement.class)
    List<GoodsManagement> getSelectPageList(@Param("info")GoodsManagement info,@Param("page") Page<GoodsManagement> page);

    @Insert(
            " INSERT INTO vip_goods_config " +
                    " (goods_code,goods_name,goods_type,business_no,origin_id, " +
                    "  create_time,operator,goods_status,is_delete) " +
                    " VALUES " +
                    " (#{info.goodsCode},#{info.goodsName},#{info.goodsType},#{info.businessNo},#{info.originId}, " +
                    "  now(),#{info.operator},'1','0')"
    )
    int addGood(@Param("info")GoodsManagement info);

    @Update(
            " update vip_goods_config set is_delete='1' where id=#{id} and business_no=#{businessNo}"
    )
    int deleteGood(@Param("id")Long id,@Param("businessNo")String businessNo);

    @Update(
            "update vip_goods_config " +
                    " set goods_name=#{info.goodsName},goods_type=#{info.goodsType},business_no=#{info.businessNo}, " +
                    "     origin_id=#{info.originId} " +
                    "  where id=#{info.id}"
    )
    int updateGood(@Param("info")GoodsManagement info);

    @SelectProvider(type= GoodsManagementDao.SqlProvider.class,method="getSelectPageList")
    @ResultType(GoodsManagement.class)
    List<GoodsManagement> getAllGoodsType(@Param("info")GoodsManagement info);

    @SelectProvider(type= GoodsManagementDao.SqlProvider.class,method="getSelectPageList")
    @ResultType(GoodsManagement.class)
    List<GoodsManagement> getGoodsList(@Param("info")GoodsManagement info);

    @Select(
            " select * from vip_goods_config where goods_code = #{goodsCode} "
    )
    GoodsManagement getGoodsByCode(@Param("goodsCode")String goodsCode);

    @Select(
            " select * from vip_goods_config where id = #{id} AND business_no=#{businessNo} "
    )
    GoodsManagement getGoodInfoById(@Param("id")Long id,@Param("businessNo") String businessNo);

    class SqlProvider{
        public String getSelectPageList(final Map<String, Object> param) {
            GoodsManagement info = (GoodsManagement) param.get("info");
            StringBuffer sb=new StringBuffer();
            sb.append(" select ");
            sb.append("   goods.*,bus.business_name ");
            sb.append("  from  vip_goods_config goods ");
            sb.append("    LEFT JOIN  vip_business_info bus ON bus.business_no=goods.business_no ");
            sb.append(" where 1=1 ");
            if(StringUtils.isNotBlank(info.getGoodsName())){
                sb.append(" and goods.goods_name like concat('%',#{info.goodsName},'%') ");
            }
            if(StringUtils.isNotBlank(info.getGoodsType())){
                sb.append(" and goods.goods_type = #{info.goodsType} ");
            }
            if(StringUtils.isNotBlank(info.getBusinessNo())){
                sb.append(" and goods.business_no = #{info.businessNo} ");
            }
            if(StringUtils.isNotBlank(info.getBusinessNoFilter())){
                if(!"-1".equals(info.getBusinessNoFilter())){
                    sb.append(" and goods.business_no in ("+info.getBusinessNoFilter()+") ");
                }
            }else{
                sb.append(" and 1=2 ");
            }
            sb.append(" and goods.is_delete = '0' ");
            sb.append(" ORDER BY goods.create_time desc ");
            return  sb.toString();
        }
    }
}
