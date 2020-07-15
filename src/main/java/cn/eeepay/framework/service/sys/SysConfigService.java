package cn.eeepay.framework.service.sys;


/**
 * Created by Administrator on 2019/7/26/026.
 * 获取系统参数
 */
public interface SysConfigService {

    String getByKey(String key);

    int updateKey(String key,String value);

}
