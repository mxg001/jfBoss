package cn.eeepay.framework.service.activity;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.activity.ActivityManagement;
import cn.eeepay.framework.model.activity.RangeName;

import java.util.List;
import java.util.Map;

public interface ActivityManagementService {

    List<ActivityManagement> getActivityList();

    ActivityManagement getLuckDrawpConfig(String actCode, String businessNo);

    ActivityManagement findActivityDetailByActCodeAndBusinessNo(String actCode, String businessNo);

    int saveLuckTemplate(ActivityManagement info);

    ActivityManagement getActivityTemp(String actCode, String businessNo);

    int saveActivityTemp(ActivityManagement info);

    List<ActivityManagement> getActivityListBusNo(ActivityManagement info);

    ActivityManagement checkActivity(String actCode, String businessNo);

    int activitySwitch(Long id, int status, String businessNo);

    int homePageSwitch(Long id, int status, String businessNo);

    int importMemberAll(String actCode, String actRangeType);

    List<RangeName> getRangeNameList(RangeName info, Page<RangeName> page);

    int addRangeName(RangeName info, Map<String, Object> msg);

    int deleteRangeName(RangeName info);
}
