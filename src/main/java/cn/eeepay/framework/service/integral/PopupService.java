package cn.eeepay.framework.service.integral;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.integral.PopupConfig;
import cn.eeepay.framework.model.integral.TaskRewardInfo;

import java.util.List;

public interface PopupService {
    int add(PopupConfig popupConfig);

    List<PopupConfig> selectWithPage(Page<PopupConfig> page);

    int delete(String id);

    PopupConfig selectById(int id);

    int edit(PopupConfig popupConfig);
}
