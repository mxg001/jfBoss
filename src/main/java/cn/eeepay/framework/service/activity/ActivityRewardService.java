package cn.eeepay.framework.service.activity;

import cn.eeepay.framework.model.activity.ActivityReward;

import java.util.List;

public interface ActivityRewardService {

    int deleteActReward(ActivityReward prize);

    ActivityReward getActRewardInfo(String linkCode);

    List<ActivityReward> getActRewardList(String linkCode);

    int addActRewardInfo(ActivityReward addInfo);

    int updateActRewardInfo(ActivityReward addInfo);

    int updateActRewardList(String strs);

    int deleteActRewardByLinkCode(String linkCode);

}
