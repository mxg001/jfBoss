package cn.eeepay.framework.service.activity;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.activity.ActivityCensus;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ActivityCensusService {

    List<ActivityCensus> getSelectPageList(ActivityCensus info, Page<ActivityCensus> page);

    void importDetail(ActivityCensus info, HttpServletResponse response) throws Exception;
}
