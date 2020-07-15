package cn.eeepay.framework.service.sys;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.sys.BossOperLog;

import java.util.List;

/**
 * BossOperLogService
 * @author YeXiaoMing
 * @date 2016年12月13日上午11:33:02
 */
public interface BossOperLogService {

	public  int insert(BossOperLog bossOperLog);
	
	BossOperLog queryDetail(Integer id);

	List<BossOperLog> queryByCondition(Page page, BossOperLog logInfo);
}
