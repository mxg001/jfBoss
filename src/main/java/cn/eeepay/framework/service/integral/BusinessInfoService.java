package cn.eeepay.framework.service.integral;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.integral.BusinessInfo;
import cn.eeepay.framework.model.integral.BusinessTask;
import cn.eeepay.framework.model.integral.BusinessVipLevelInfo;
import cn.eeepay.framework.model.integral.TaskRewardInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/7/2/002.
 * @author liuks
 * 业务组service
 */
public interface BusinessInfoService {

    List<BusinessInfo> getBusinessInfoListAll();

    List<BusinessInfo> getSelectPageList(BusinessInfo info, Page<BusinessInfo> page);

    int addBusinessInfo(BusinessInfo info, Map<String, Object> msg);

    BusinessInfo getBusinessBasic(String businessNo,Map<String,Object> msg);

    int updateBusinessBasic(BusinessInfo info, Map<String, Object> msg);

    void getBusinessTaskList(String businessNo,Map<String,Object> msg);

    int addBusinessTask(String businessNo, String ids, Map<String, Object> msg);

    BusinessTask getBusinessTask(Long id,String businessNo,Map<String, Object> msg);

    int editBusinessTask(BusinessTask info, Map<String, Object> msg);

    int closeBusinessTask(Long id, String businessNo,int sta, Map<String, Object> msg);

    int deleteBusinessTask(Long id,String businessNo,Map<String, Object> msg);

    List<BusinessInfo> getBusinessInfoListByUser();

    List<Map<Object,Object>> businessTaskPreview(String businessNo);

    List<Map<String,Object>> getAllVipLevels(String businessNo);

    int saveBatchBusinessTaskPreview(String rewardLevel, String businessNo);

}
