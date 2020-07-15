package cn.eeepay.framework.service.activity;

import cn.eeepay.framework.model.activity.ConditionForStandard;

public interface ConditionForStandardService {

    ConditionForStandard findConditionByActDetCode(String actDetCode);

    int updateCondtionByActDetCode(String actDetCode, String extend);

    int insertCondition(ConditionForStandard conditionForStandard);

    int deleteConditionByActDetCode(String actDetCode);
}
