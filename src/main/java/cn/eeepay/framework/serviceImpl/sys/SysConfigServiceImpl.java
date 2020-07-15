package cn.eeepay.framework.serviceImpl.sys;

import cn.eeepay.framework.dao.sys.SysConfigDao;
import cn.eeepay.framework.service.sys.SysConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2019/7/26/026.
 */
@Service("sysConfigService")
public class SysConfigServiceImpl implements SysConfigService {

    @Resource
    private SysConfigDao sysConfigDao;


    @Override
    public String getByKey(String key) {
        return sysConfigDao.getByKey(key);
    }

    @Transactional(value = "transactionManager")
    @Override
    public int updateKey(String key, String value) {
        return sysConfigDao.updateKey(key,value);
    }
}
