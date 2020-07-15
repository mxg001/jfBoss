package cn.eeepay.framework.dao.sys;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by Administrator on 2019/7/26/026.
 * @author  liuks
 * 查询系统配置
 */
public interface SysConfigDao {


    @Select("select param_value from vip_sys_config where param_key=#{key}")
    String getByKey(@Param("key")String key);

    @Update(
            "update vip_sys_config set param_value=#{value} where param_key=#{key} "
    )
    int updateKey(@Param("key")String key, @Param("value")String value);
}
