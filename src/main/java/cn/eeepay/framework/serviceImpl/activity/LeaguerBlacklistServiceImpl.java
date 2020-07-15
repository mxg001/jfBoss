package cn.eeepay.framework.serviceImpl.activity;

import cn.eeepay.framework.dao.activity.LeaguerBlacklistDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.activity.LeaguerBlacklist;
import cn.eeepay.framework.model.integral.BusinessFlow;
import cn.eeepay.framework.model.sys.UserLoginInfo;
import cn.eeepay.framework.service.activity.LeaguerBlacklistService;
import cn.eeepay.framework.service.integral.LeaguerService;
import cn.eeepay.framework.util.StringUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class LeaguerBlacklistServiceImpl implements LeaguerBlacklistService {

    @Resource
    private LeaguerBlacklistDao leaguerBlacklistDao;
    @Resource
    private LeaguerService leaguerService;


    @Override
    public List<LeaguerBlacklist> getActivityBlackList(String serviceNo, String leaguerNo, Page<LeaguerBlacklist> page) {
        List<LeaguerBlacklist> list = leaguerBlacklistDao.findListByActDetCode(serviceNo,leaguerNo,page);
        page.setResult(list);
        return list;
    }

    @Override
    public LeaguerBlacklist findByActDetCode(String actDetCode, String leaguerNo) {
        return leaguerBlacklistDao.findByActDetCode(actDetCode,leaguerNo);
    }

    @Override
    public int insertActivityBlack(LeaguerBlacklist info) {
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final String realName = principal.getRealName();
        info.setOperater(realName);
        info.setCreateTime(new Date());
        return leaguerBlacklistDao.insertBlackList(info);
    }

    @Override
    public int deleteActivityBlack(String serviceNo, String leaguerNo) {
        return leaguerBlacklistDao.deleteActivityBlack(serviceNo,leaguerNo);
    }
}
