package cn.eeepay.framework.serviceImpl.sys;

import cn.eeepay.framework.dao.sys.SysConfigDao;
import cn.eeepay.framework.service.sys.ConfigLoadedService;
import cn.eeepay.framework.util.ALiYunOssUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("configLoadedService")
public class ConfigLoadedServiceImpl implements ConfigLoadedService {

    @Resource
    private SysConfigDao sysConfigDao;
    @Override
    public void initConfig() {
        //加载阿里云key
        ALiYunOssUtil.ACCESS_KEY = sysConfigDao.getByKey("Ali_OSS_ACCESS_KEY");
        ALiYunOssUtil.ACCESS_KEY_SECRET= sysConfigDao.getByKey("Ali_OSS_ACCESS_SECRET");
    }
}
