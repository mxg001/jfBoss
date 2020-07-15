package cn.eeepay.framework.service.activity;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.activity.LeagerBagSerial;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface LeagerBagSerialService {

    public List<LeagerBagSerial> findListByPage(LeagerBagSerial info, Page<LeagerBagSerial> page);

    public void importLeagerBagSerial(LeagerBagSerial info, HttpServletResponse response) throws Exception;

}
