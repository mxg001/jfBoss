package cn.eeepay.framework.serviceImpl.activity;

import cn.eeepay.framework.dao.activity.ActivityRewardDao;
import cn.eeepay.framework.model.activity.ActivityReward;
import cn.eeepay.framework.model.integralMall.GoodsManagement;
import cn.eeepay.framework.service.activity.ActivityRewardService;
import cn.eeepay.framework.service.integralMall.GoodsManagementService;
import cn.eeepay.framework.service.sys.RandomNumService;
import cn.eeepay.framework.service.sys.SysDictService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("activityRewardService")
public class ActivityRewardServiceImpl implements ActivityRewardService {

    @Resource
    private ActivityRewardDao activityRewardDao;
    @Resource
    private RandomNumService randomNumService;
    @Resource
    private GoodsManagementService goodsManagementService;
    @Resource
    private SysDictService sysDictService;

    @Override
    public int deleteActReward(ActivityReward prize) {
        return activityRewardDao.deleteActReward(prize);
    }

    @Override
    public ActivityReward getActRewardInfo(String linkCode) {
        return activityRewardDao.getActRewardInfo(linkCode);
    }

    @Override
    public List<ActivityReward> getActRewardList(String linkCode) {
        List<ActivityReward> list= activityRewardDao.getActRewardList(linkCode);
        if(list!=null&&list.size()>0){
            for(ActivityReward item:list){
                if("4".equals(item.getRewardType())||"5".equals(item.getRewardType())||"6".equals(item.getRewardType())){
                    GoodsManagement goodInfo=goodsManagementService.getGoodsByCode(item.getCalcType());
                    if(goodInfo!=null){
                        item.setGoodName(goodInfo.getGoodsName());
                    }
                }
            }
        }
        return list;
    }

    @Override
    public int addActRewardInfo(ActivityReward addInfo) {
        addInfo.setActRewardNo(randomNumService.getRandomNumberByData("AR","vipScore",5));
        Map<String, String> map= sysDictService.selectMapByKey("REWARD_TYPE");
        addInfo.setRewardName(map.get(addInfo.getRewardType()));
        return activityRewardDao.addActRewardInfo(addInfo);
    }

    @Override
    public int updateActRewardInfo(ActivityReward addInfo) {
        Map<String, String> map= sysDictService.selectMapByKey("REWARD_TYPE");
        addInfo.setRewardName(map.get(addInfo.getRewardType()));
        return activityRewardDao.updateActRewardInfo(addInfo);
    }

    @Override
    public int updateActRewardList(String strs) {
        return activityRewardDao.updateActRewardList(strs);
    }

    @Transactional(value = "transactionManager")
    @Override
    public int deleteActRewardByLinkCode(String linkCode) {
        return activityRewardDao.deleteActRewardByLinkCode(linkCode);
    }

}
