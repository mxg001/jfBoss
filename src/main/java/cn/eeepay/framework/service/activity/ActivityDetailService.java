package cn.eeepay.framework.service.activity;

import cn.eeepay.framework.model.activity.ActivityDetail;
import cn.eeepay.framework.model.activity.ActivityManagement;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ActivityDetailService {

    ActivityDetail getLuckDrawpDetail(String actCode);

    int saveLuckDetTemplate(String actCode,ActivityDetail luckDrawpDetail);

    int saveActivityContent(ActivityDetail detail);

    List<ActivityDetail> getActivityDetailList(String actCode);

    List<ActivityDetail> getActivityDetailByActCodeAndActiveType(String actCode,List<Integer> activeTypeList,String businessNo);

    List<ActivityDetail> getActivityDetailByActCodeAndActiveType(String actCode,List<Integer> activeTypeList);

    int addActiInfo(ActivityDetail info, Map<String, Object> msg);

    ActivityDetail getActiDetail(String actDetCode);

    ActivityDetail getActivityDetailByActCodeAndActDetCode(String actCode, String actDetCode);

    int editActiInfo(ActivityDetail info, Map<String, Object> msg);

    int deleteActiInfo(String actDetCode);

    int deleteActiInfoByActDetCode(String actCode);

    ActivityDetail checkActiInfo(String actDetCode);

    int insertActiInfo(ActivityDetail info);

    int updateAfterCondition(ActivityDetail detail,Map<String,Object> msg);

    int updateActivityContent(ActivityDetail detail,Map<String, Object> msg);

    int updateActivityDetailOpenStatus(String actCode,String actDetCode,Integer status);

    List<ActivityManagement> selectAllActivity(String businessNo);
}
