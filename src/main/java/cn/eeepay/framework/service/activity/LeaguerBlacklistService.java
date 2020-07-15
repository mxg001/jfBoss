package cn.eeepay.framework.service.activity;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.activity.LeaguerBlacklist;

import java.util.List;

public interface LeaguerBlacklistService {

    public List<LeaguerBlacklist> getActivityBlackList(String serviceNo, String leaguerNo, Page<LeaguerBlacklist> page);

    public LeaguerBlacklist findByActDetCode(String actDetCode, String leaguerNo);

    public int insertActivityBlack(LeaguerBlacklist info);

    public int deleteActivityBlack(String serviceNo, String leaguerNo);

}
