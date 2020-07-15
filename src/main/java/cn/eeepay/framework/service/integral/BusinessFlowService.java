package cn.eeepay.framework.service.integral;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.integral.BusinessFlow;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/7/15/015.
 * @author liuks
 * 业务流水service
 */
public interface BusinessFlowService {

    List<BusinessFlow> getSelectPageList(BusinessFlow info, Page<BusinessFlow> page);

    int getSelectPageListCount(BusinessFlow info);

    void importDetail(BusinessFlow info, HttpServletResponse response) throws Exception;

    int lowerHair(String rewardOrderNo, String businessNo, Map<String, Object> msg);
}
