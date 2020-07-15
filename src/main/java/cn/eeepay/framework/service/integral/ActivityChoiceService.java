package cn.eeepay.framework.service.integral;

import cn.eeepay.framework.model.activity.ActivityManagement;
import cn.eeepay.framework.model.activity.ShowConfig;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface ActivityChoiceService {

    int addActivityList(String businessNo, String ids, Map<String, Object> msg);

    int deleteActivityList(String businessNo, String actCode, Map<String, Object> msg);

    Map<String, Object> importMember(MultipartFile file, ActivityManagement info,String businessNo,String type);

    ShowConfig getShowConfig(String actCode);

    int saveShowConfig(ShowConfig info);

    boolean checkGood(String goodsCode);
}
