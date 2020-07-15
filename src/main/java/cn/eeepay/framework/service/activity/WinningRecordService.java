package cn.eeepay.framework.service.activity;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.activity.WinningRecord;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface WinningRecordService {

    List<WinningRecord> getSelectPageList(WinningRecord info, Page<WinningRecord> page);

    int getWinningRecordCount(WinningRecord info);

    void importDetail(WinningRecord info, HttpServletResponse response) throws Exception;


}
