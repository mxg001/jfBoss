package cn.eeepay.framework.serviceImpl.activity;

import cn.eeepay.framework.dao.activity.ConditionForStandardDao;
import cn.eeepay.framework.model.activity.ConditionForStandard;
import cn.eeepay.framework.service.activity.ConditionForStandardService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ConditionForStandardServiceImpl implements ConditionForStandardService {

    @Resource
    private ConditionForStandardDao conditionForStandardDao;

    @Override
    public ConditionForStandard findConditionByActDetCode(String actDetCode) {
        return conditionForStandardDao.findConditionByActDetCode(actDetCode);
    }

    @Override
    public int updateCondtionByActDetCode(String actDetCode,String extend) {
        return conditionForStandardDao.updateCondtionByActDetCode(actDetCode,extend);
    }

    @Override
    public int insertCondition(ConditionForStandard conditionForStandard) {
        return conditionForStandardDao.insertCondition(conditionForStandard);
    }

    @Override
    public int deleteConditionByActDetCode(String actDetCode) {
        return conditionForStandardDao.deleteConditionByActDetCode(actDetCode);
    }
}
