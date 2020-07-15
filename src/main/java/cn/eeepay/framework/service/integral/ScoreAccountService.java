package cn.eeepay.framework.service.integral;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.integral.AddScoreInfo;
import cn.eeepay.framework.model.integral.ScoreAccountCount;
import cn.eeepay.framework.model.integral.ScoreAccountInfo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/7/11/011.
 * @author liuks 
 * 积分出入账 service
 */
public interface ScoreAccountService {
    
    List<ScoreAccountInfo> getUserAccountList(ScoreAccountInfo info, Page<ScoreAccountInfo> page, String businessNo);

    int getUserAccountListCount(ScoreAccountInfo info, String businessNo);

    List<ScoreAccountInfo> getSelectPageList(ScoreAccountInfo info, Page<ScoreAccountInfo> page);

    ScoreAccountCount getSelectPageListCount(ScoreAccountInfo info);

    void importDetail(ScoreAccountInfo info, HttpServletResponse response) throws Exception;

    int addScore(AddScoreInfo info, Map<String,Object> msg);

}
