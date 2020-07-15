package cn.eeepay.framework.service.activity;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.activity.ActivityDetail;
import cn.eeepay.framework.model.activity.AwardBlack;
import cn.eeepay.framework.model.activity.PrizeInfo;

import java.util.List;
import java.util.Map;

public interface PrizeInfoService {

    List<PrizeInfo> getPrizeInfoList(String actCode);

    int addLuckPrizeTemplate(PrizeInfo info, Map<String, Object> msg);

    int addLuckPrizeTemplate1(PrizeInfo info, Map<String, Object> msg);

    PrizeInfo getLuckPrize(String awardCode);

    int editLuckPrizeTemplate(PrizeInfo info, Map<String, Object> msg);

    int closeOpenPrize(Long id, int status);

    int deletePrize(Long id);

    PrizeInfo getLuckPrizeById(Long id);

    PrizeInfo getLuckPrizeByCode(String awardCode);

    List<AwardBlack> getAwardBlackList(AwardBlack info, Page<AwardBlack> page);

    int addAwardBlack(AwardBlack info);

    int deleteAwardBlack(AwardBlack info);

    int prizeTimesReset();

    List<PrizeInfo> getLuckPrizeByActCodeAndActDetCode(String actCode, String actDetCode);

    int deletePrizeInfoAndActivityReward(PrizeInfo prizeInfo);

}
