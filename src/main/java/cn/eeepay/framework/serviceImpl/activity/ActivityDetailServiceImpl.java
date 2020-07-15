package cn.eeepay.framework.serviceImpl.activity;

import cn.eeepay.framework.dao.activity.ActivityDetailDao;
import cn.eeepay.framework.model.activity.*;
import cn.eeepay.framework.service.activity.*;
import cn.eeepay.framework.service.sys.RandomNumService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service("activityDetailService")
public class ActivityDetailServiceImpl implements ActivityDetailService {

    @Resource
    private ActivityDetailDao activityDetailDao;
    @Resource
    private RandomNumService randomNumService;

    @Resource
    private ActivityRewardService activityRewardService;
    @Resource
    private PrizeInfoService prizeInfoService;
    @Resource
    private ConditionForStandardService conditionForStandardService;
    @Resource
    private ActivityManagementService activityManagementService;

    @Override
    public ActivityDetail getLuckDrawpDetail(String actCode) {
        return activityDetailDao.getLuckDrawpDetail(actCode);
    }

    /**
     * 抽奖保存ActivityDetail
     * @param actCode
     * @param luckDrawpDetail
     * @return
     */
    @Transactional(value = "transactionManager")
    @Override
    public int saveLuckDetTemplate(String actCode,ActivityDetail luckDrawpDetail) {
        ActivityDetail oldInfo=activityDetailDao.getLuckDrawpDetail(actCode);
        int num=0;
        if("2".equals(luckDrawpDetail.getExpendType())){
            luckDrawpDetail.setExpendNum(BigDecimal.ONE);
        }else{
            luckDrawpDetail.setAmountRangeMin(null);
        }
        if(oldInfo==null){//新增
            luckDrawpDetail.setActCode(actCode);
            luckDrawpDetail.setActDetCode(randomNumService.getRandomNumberByData("AD","vipScore",5));
            luckDrawpDetail.setActiveType("4");
            num= activityDetailDao.insertLuckActTemplate(luckDrawpDetail);
        }else{
            num=activityDetailDao.updateLuckActTemplate(luckDrawpDetail);
        }
        return num;
    }


    @Transactional(value = "transactionManager")
    @Override
    public int saveActivityContent(ActivityDetail detail){
        Map<String, Object> msg = new HashMap<>();
        final BigDecimal tradAmountUpdateValue = detail.getTradAmountUpdateValue();
        final Integer memberUpdateValue = detail.getMemberUpdateValue();
        final BigDecimal inviteFriendUpdateValue = detail.getInviteFriendUpdateValue();

        JSONObject condition = new JSONObject();
        if(tradAmountUpdateValue != null){
            condition.put("activityContent.trans.amount",tradAmountUpdateValue);
            condition.put("activityContent.trans.amount.condition",">=");
        }
        if(memberUpdateValue != null){
            condition.put("activityContent.consume.bounds",memberUpdateValue);
            condition.put("activityContent.consume.bounds.condition",">=");
        }
        if(inviteFriendUpdateValue != null){
            condition.put("activityContent.inviteUser.transAmount",inviteFriendUpdateValue);
            condition.put("activityContent.inviteUser.transAmount.condition",">=");
        }

        if(condition.size() == 0){
            int num = conditionForStandardService.deleteConditionByActDetCode(detail.getActDetCode());
            if(num == 0){
                //达标条件删除失败，不做处理
            }
        }
        detail.setActDetCode(randomNumService.getRandomNumberByData("AD","vipScore",5));
        ConditionForStandard conditionForStandard = conditionForStandardService.findConditionByActDetCode(detail.getActDetCode());
        if(conditionForStandard == null){
            conditionForStandard = new ConditionForStandard();
            conditionForStandard.setCreateTime(new Date());
            conditionForStandard.setActDetCode(detail.getActDetCode());
            conditionForStandard.setExtend(condition.toJSONString());
            int num = conditionForStandardService.insertCondition(conditionForStandard);
            if(num == 0){
                //达标条件添加失败，不做处理
            }
        }else{
            int num = conditionForStandardService.updateCondtionByActDetCode(detail.getActDetCode(),condition.toJSONString());
            if(num == 0){
                //达标条件修改失败，不做处理
            }
        }
        //如果是登录app,单日奖励次数上限默认1
        if(detail.getActiveType().equals("5")){
            detail.setExpendNumber(1);
        }

        if(detail.getPolicyType() == 1){
            for (PolicyDetail policyDetail : detail.getPolicyList()) {
                PrizeInfo prizeInfo = new PrizeInfo();
                prizeInfo.setDayNums(policyDetail.getDayNums());
                prizeInfo.setAwardOdds(policyDetail.getAwardOdds());
                prizeInfo.setActCode(detail.getActCode());
                prizeInfo.setActDetCode(detail.getActDetCode());
                prizeInfo.setOpenStatus("1");
                prizeInfo.setDayAwardGrant(0);
                prizeInfo.setDayMax(-1);
                prizeInfo.setAwardGrantNum(0);
                prizeInfo.setAwardName(policyDetail.getAwardNum().toString());
                prizeInfo.setAwardDesc(policyDetail.getAwardNum().toString());
                int i = prizeInfoService.addLuckPrizeTemplate1(prizeInfo, msg);
                if(i == 0){
                    throw new RuntimeException(msg.get("msg").toString());
                }
                ActivityReward activityReward = new ActivityReward();
                activityReward.setAwardNum(policyDetail.getAwardNum().toString());
                activityReward.setRewardType("8");
                activityReward.setCalcType("1");
                activityReward.setLinkCode(prizeInfo.getAwardCode());
                i = activityRewardService.addActRewardInfo(activityReward);
                if(i == 0){
                    throw new RuntimeException("活动明细奖励失败");
                }
            }
        }
        return activityDetailDao.insertActiInfo(detail);
    }

    @Override
    public List<ActivityDetail> getActivityDetailList(String actCode) {
        return activityDetailDao.getActivityDetailList(actCode);
    }

    @Override
    public List<ActivityDetail> getActivityDetailByActCodeAndActiveType(String actCode,List<Integer> activeTypeList,String businessNo) {
        final String activeTypes = StringUtils.join(activeTypeList, ",");
        final List<ActivityDetail> details = activityDetailDao.getActivityDetailByActCodeAndActiveType(actCode, activeTypes);
        if(CollectionUtil.isNotEmpty(details)){
            for (ActivityDetail detail : details) {
                if(detail.getExpiryDateType().equals("activity")){
                    final ActivityManagement activity = activityManagementService.findActivityDetailByActCodeAndBusinessNo(actCode, businessNo);
                    if(activity != null){
                        final String actBeginTime = DateUtil.formatDateTime(activity.getActBeginTime());
                        final String actEndTime = DateUtil.formatDateTime(activity.getActEndTime());
                        detail.setExpiryNum(actBeginTime+"-"+actEndTime);
                    }
                }
            }
        }
        return details;
    }

    @Override
    public List<ActivityDetail> getActivityDetailByActCodeAndActiveType(String actCode,List<Integer> activeTypeList) {
        final String activeTypes = StringUtils.join(activeTypeList, ",");
        return activityDetailDao.getActivityDetailByActCodeAndActiveType(actCode, activeTypes);
    }

    /**
     * 用户激活保存ActivityDetail
     * @param info
     * @return
     */
    @Transactional(value = "transactionManager")
    @Override
    public int addActiInfo(ActivityDetail info, Map<String, Object> msg) {
        List<ActivityDetail> list=activityDetailDao.getActivityDetailList(info.getActCode());
        if(list!=null&&list.size()>0){
            for(ActivityDetail item:list){
                if(item.getActiveType().equals(info.getActiveType())){
                    msg.put("msg", "该类型的活动内容已存在!");
                    msg.put("status", false);
                    return 0;
                }
            }
        }

        info.setActDetCode(randomNumService.getRandomNumberByData("AD","vipScore",5));
        if("3".equals(info.getActiveType())){
            info.setAwardMethod("3");
        }
        int num= activityDetailDao.insertActiInfo(info);
        if(num>0){
            saveActivityRewardList(info);
            msg.put("msg", "活动内容新增成功!");
            msg.put("status", true);
        }else{
            msg.put("msg", "活动内容新增失败!");
            msg.put("status", false);
        }
        return num;
    }

    @Override
    public ActivityDetail getActiDetail(String actDetCode) {
        ActivityDetail info=getActiDetailByCode(actDetCode);
        return info;
    }

    @Override
    public ActivityDetail getActivityDetailByActCodeAndActDetCode(String actCode, String actDetCode){
        final ActivityDetail info = activityDetailDao.getActivityDetailByActCodeAndActDetCode(actCode, actDetCode);
        if(info!=null){
            ConditionForStandard conditionForStandard = conditionForStandardService.findConditionByActDetCode(actDetCode);
            if(conditionForStandard != null){
                final String extend = conditionForStandard.getExtend();
                final JSONObject condition = JSONObject.parseObject(extend);
                BigDecimal userTransAmount = condition.getBigDecimal("inviteUser.transAmount");
                Integer userTransPersons = condition.getInteger("inviteUser.persons");
                Integer memberAwardNum = condition.getInteger("useScore.nums");
                Integer transAmountNum = condition.getInteger("trans.nums");
                info.setUserTransAmount(userTransAmount);
                info.setUserTransPersons(userTransPersons);
                info.setMemberAwardNum(memberAwardNum);
                info.setTransAmountNum(transAmountNum);

                BigDecimal tradAmountUpdateValue = condition.getBigDecimal("activityContent.trans.amount");
                Integer memberUpdateValue = condition.getInteger("activityContent.consume.bounds");
                BigDecimal inviteFriendUpdateValue = condition.getBigDecimal("activityContent.inviteUser.transAmount");

                info.setTradAmountUpdateValue(tradAmountUpdateValue);
                info.setMemberUpdateValue(memberUpdateValue);
                info.setInviteFriendUpdateValue(inviteFriendUpdateValue);
            }

            if(info.getPolicyType() == 1){
                List<PrizeInfo> prizeInfos = prizeInfoService.getLuckPrizeByActCodeAndActDetCode(actCode,actDetCode);
                if(prizeInfos != null){
                    List<PolicyDetail> policyList = new ArrayList<>();
                    for (PrizeInfo prizeInfo : prizeInfos) {
                        PolicyDetail policyDetail = new PolicyDetail();
                        policyDetail.setAwardOdds(prizeInfo.getAwardOdds());
                        policyDetail.setDayNums(prizeInfo.getDayNums());
                        policyDetail.setId(prizeInfo.getId());
                        policyDetail.setAwardCode(prizeInfo.getAwardCode());
                        policyList.add(policyDetail);
                        final ActivityReward actRewardInfo = activityRewardService.getActRewardInfo(prizeInfo.getAwardCode());
                        if(actRewardInfo != null){
                            policyDetail.setAwardNum(new BigDecimal(actRewardInfo.getAwardNum()));
                        }
                    }
                    info.setPolicyList(policyList);
                }
            }
        }
        return info;
    }

    private ActivityDetail getActiDetailByCode(String actDetCode){
        ActivityDetail info=activityDetailDao.getActivityDetailByCode(actDetCode);
        if(info!=null){
            info.setActReward(activityRewardService.getActRewardList(actDetCode));
        }
        return info;
    }


    @Transactional(value = "transactionManager")
    @Override
    public int editActiInfo(ActivityDetail info, Map<String, Object> msg) {
        int num= activityDetailDao.updateActiInfo(info);
        if(num>0){
            saveActivityRewardList(info);
            msg.put("msg", "活动内容修改成功!");
            msg.put("status", true);
        }else{
            msg.put("msg", "活动内容修改失败!");
            msg.put("status", false);
        }
        return num;
    }

    @Transactional(value = "transactionManager")
    @Override
    public int deleteActiInfo(String actDetCode) {
        int num= activityDetailDao.deleteActiInfoByCode(actDetCode);
        if(num>0){
            activityRewardService.deleteActRewardByLinkCode(actDetCode);
        }
        return num;
    }

    @Transactional(value = "transactionManager")
    @Override
    public int deleteActiInfoByActDetCode(String actDetCode) {
        final ActivityDetail activityDetail = activityDetailDao.getActivityDetailByCode(actDetCode);
        if(activityDetail != null){
            conditionForStandardService.deleteConditionByActDetCode(actDetCode);
            final List<PrizeInfo> luckPrizeByActCodeAndActDetCode = prizeInfoService.getLuckPrizeByActCodeAndActDetCode(activityDetail.getActCode(), activityDetail.getActDetCode());
            if(CollectionUtil.isNotEmpty(luckPrizeByActCodeAndActDetCode)){
                for (PrizeInfo prizeInfo : luckPrizeByActCodeAndActDetCode) {
                    prizeInfoService.deletePrizeInfoAndActivityReward(prizeInfo);
                }
            }
        }
        return activityDetailDao.deleteActiInfoByCode(actDetCode);
    }



    @Override
    public ActivityDetail checkActiInfo(String actDetCode) {
        ActivityDetail info=activityDetailDao.getActivityDetailByCode(actDetCode);
        return info;
    }

    @Override
    public int insertActiInfo(ActivityDetail info) {
        return activityDetailDao.insertActiInfo(info);
    }

    /**
     * 修改后置条件
     * @param detail
     * @return
     */
    @Transactional(value = "transactionManager")
    @Override
    public int updateAfterCondition(ActivityDetail detail,Map<String,Object> msg) {
        final BigDecimal userTransAmount = detail.getUserTransAmount();
        final Integer userTransPersons = detail.getUserTransPersons();
        final Integer memberAwardNum = detail.getMemberAwardNum();
        final Integer transAmountNum = detail.getTransAmountNum();

        JSONObject condition = new JSONObject();
        condition.put("inviteUser.transAmount",userTransAmount);
        condition.put("inviteUser.transAmount.condition",">=");
        condition.put("inviteUser.persons",userTransPersons);
        condition.put("inviteUser.persons.condition",">=");

        condition.put("useScore.nums",memberAwardNum);
        condition.put("useScore.nums.condition",">=");

        condition.put("trans.nums",transAmountNum);
        condition.put("trans.nums.condition",">=");


        ConditionForStandard conditionForStandard = conditionForStandardService.findConditionByActDetCode(detail.getActDetCode());
        if(conditionForStandard == null){
            conditionForStandard = new ConditionForStandard();
            conditionForStandard.setCreateTime(new Date());
            conditionForStandard.setActDetCode(detail.getActDetCode());
            conditionForStandard.setExtend(condition.toJSONString());
            int num = conditionForStandardService.insertCondition(conditionForStandard);
            if(num == 0){
                //达标条件添加失败，不做处理
            }
        }else{
            int num = conditionForStandardService.updateCondtionByActDetCode(detail.getActDetCode(),condition.toJSONString());
            if(num == 0){
                //达标条件修改失败，不做处理
            }
        }

        final ActivityDetail oldDetail = activityDetailDao.findById(detail.getId());
        if(oldDetail.getPolicyType() == 1 || detail.getPolicyType() == 1){
            final List<PrizeInfo> prizeInfos = prizeInfoService.getLuckPrizeByActCodeAndActDetCode(detail.getActCode(), detail.getActDetCode());
            if(CollectionUtil.isNotEmpty(prizeInfos)){
                for (PrizeInfo prizeInfo : prizeInfos) {
                    prizeInfoService.deletePrizeInfoAndActivityReward(prizeInfo);
                }
            }
            if(CollectionUtil.isNotEmpty(detail.getPolicyList())){
                for (PolicyDetail policyDetail : detail.getPolicyList()) {
                    final String awardCode = policyDetail.getAwardCode();
                    PrizeInfo prizeInfo = new PrizeInfo();
                    Integer dayAwardGrant = 0;
                    Integer awardGrantNum = 0;
                    if(StringUtils.isNotBlank(awardCode)){
                        final PrizeInfo tempPrizeInfo = prizeInfos.stream().filter(it -> it.getAwardCode().equals(awardCode)).findFirst().orElse(null);
                        if(tempPrizeInfo != null){
                            dayAwardGrant = tempPrizeInfo.getDayAwardGrant();
                            awardGrantNum = tempPrizeInfo.getAwardGrantNum();
                        }
                    }
                    prizeInfo.setDayAwardGrant(dayAwardGrant);
                    prizeInfo.setAwardCode(awardCode);
                    prizeInfo.setDayNums(policyDetail.getDayNums());
                    prizeInfo.setAwardOdds(policyDetail.getAwardOdds());
                    prizeInfo.setActCode(detail.getActCode());
                    prizeInfo.setActDetCode(detail.getActDetCode());
                    prizeInfo.setOpenStatus("1");
                    prizeInfo.setDayMax(-1);
                    prizeInfo.setAwardGrantNum(awardGrantNum);
                    prizeInfo.setAwardName(policyDetail.getAwardNum().toString());
                    prizeInfo.setAwardDesc(policyDetail.getAwardNum().toString());
                    int i = prizeInfoService.addLuckPrizeTemplate1(prizeInfo, msg);
                    if(i == 0){
                        throw new RuntimeException(msg.get("msg").toString());
                    }
                    ActivityReward activityReward = new ActivityReward();
                    activityReward.setAwardNum(policyDetail.getAwardNum().toString());
                    activityReward.setRewardType("8");
                    activityReward.setCalcType("1");
                    activityReward.setLinkCode(prizeInfo.getAwardCode());
                    i = activityRewardService.addActRewardInfo(activityReward);
                    if(i == 0){
                        throw new RuntimeException("添加活动明细奖励配置失败");
                    }
                }
            }
        }
        return activityDetailDao.updateActivityDetail(detail);
    }

    /**
     * 修改活动内容
     * @param detail
     * @return
     */
    @Transactional(value = "transactionManager")
    @Override
    public int updateActivityContent(ActivityDetail detail,Map<String,Object> msg) {
        final BigDecimal tradAmountUpdateValue = detail.getTradAmountUpdateValue();
        final Integer memberUpdateValue = detail.getMemberUpdateValue();
        final BigDecimal inviteFriendUpdateValue = detail.getInviteFriendUpdateValue();

        JSONObject condition = new JSONObject();
        if(tradAmountUpdateValue != null){
            condition.put("activityContent.trans.amount",tradAmountUpdateValue);
            condition.put("activityContent.trans.amount.condition",">=");
        }
        if(memberUpdateValue != null){
            condition.put("activityContent.consume.bounds",memberUpdateValue);
            condition.put("activityContent.consume.bounds.condition",">=");
        }
        if(inviteFriendUpdateValue != null){
            condition.put("activityContent.inviteUser.transAmount",inviteFriendUpdateValue);
            condition.put("activityContent.inviteUser.transAmount.condition",">=");
        }

        if(condition.size() == 0){
            int num = conditionForStandardService.deleteConditionByActDetCode(detail.getActDetCode());
            if(num == 0){
                //达标条件删除失败，不做处理
            }
        }
        ConditionForStandard conditionForStandard = conditionForStandardService.findConditionByActDetCode(detail.getActDetCode());
        if(conditionForStandard == null){
            conditionForStandard = new ConditionForStandard();
            conditionForStandard.setCreateTime(new Date());
            conditionForStandard.setActDetCode(detail.getActDetCode());
            conditionForStandard.setExtend(condition.toJSONString());
            int num = conditionForStandardService.insertCondition(conditionForStandard);
            if(num == 0){
                //达标条件添加失败，不做处理
            }
        }else{
            int num = conditionForStandardService.updateCondtionByActDetCode(detail.getActDetCode(),condition.toJSONString());
            if(num == 0){
                //达标条件修改失败，不做处理
            }
        }

        //如果是登录app,单日奖励次数上限默认1
        if(detail.getActiveType().equals("5")){
            detail.setExpendNumber(1);
        }

        final ActivityDetail oldDetail = activityDetailDao.findById(detail.getId());
        if(oldDetail.getPolicyType() == 1 || detail.getPolicyType() == 1){
            final List<PrizeInfo> prizeInfos = prizeInfoService.getLuckPrizeByActCodeAndActDetCode(detail.getActCode(), detail.getActDetCode());
            if(CollectionUtil.isNotEmpty(prizeInfos)){
                for (PrizeInfo prizeInfo : prizeInfos) {
                    prizeInfoService.deletePrizeInfoAndActivityReward(prizeInfo);
                }
            }
            if(CollectionUtil.isNotEmpty(detail.getPolicyList())){
                for (PolicyDetail policyDetail : detail.getPolicyList()) {
                    final String awardCode = policyDetail.getAwardCode();
                    Integer dayAwardGrant = 0;
                    Integer awardGrantNum = 0;
                    PrizeInfo prizeInfo = new PrizeInfo();
                    if(StringUtils.isNotBlank(awardCode)){
                        final PrizeInfo tempPrizeInfo = prizeInfos.stream().filter(it -> it.getAwardCode().equals(awardCode)).findFirst().orElse(null);
                        if(tempPrizeInfo != null){
                            dayAwardGrant = tempPrizeInfo.getDayAwardGrant();
                            awardGrantNum = tempPrizeInfo.getAwardGrantNum();
                        }
                    }
                    prizeInfo.setDayAwardGrant(dayAwardGrant);
                    prizeInfo.setAwardCode(awardCode);
                    prizeInfo.setDayNums(policyDetail.getDayNums());
                    prizeInfo.setAwardOdds(policyDetail.getAwardOdds());
                    prizeInfo.setActCode(detail.getActCode());
                    prizeInfo.setActDetCode(detail.getActDetCode());
                    prizeInfo.setAwardGrantNum(awardGrantNum);
                    prizeInfo.setAwardName(policyDetail.getAwardNum().toString());
                    prizeInfo.setAwardDesc(policyDetail.getAwardNum().toString());
                    prizeInfo.setOpenStatus("1");
                    prizeInfo.setDayMax(-1);
                    int i = prizeInfoService.addLuckPrizeTemplate1(prizeInfo, msg);
                    if(i == 0){
                        throw new RuntimeException(msg.get("msg").toString());
                    }
                    ActivityReward activityReward = new ActivityReward();
                    activityReward.setAwardNum(policyDetail.getAwardNum().toString());
                    activityReward.setRewardType("8");
                    activityReward.setCalcType("1");
                    activityReward.setLinkCode(prizeInfo.getAwardCode());
                    i = activityRewardService.addActRewardInfo(activityReward);
                    if(i == 0){
                        throw new RuntimeException("添加活动明细奖励配置失败");
                    }
                }
            }
        }
        return activityDetailDao.updateActivityDetail(detail);
    }

    @Override
    public int updateActivityDetailOpenStatus(String actCode,String actDetCode, Integer status) {
        return activityDetailDao.updateActivityDetailOpenStatus(actCode,actDetCode,status);
    }


    @Override
    public List<ActivityManagement> selectAllActivity(String businessNo) {
        return activityDetailDao.selectAllActivity(businessNo);
    }

    /**
     * 保存活动权益list
     * @param info
     */
    private int saveActivityRewardList(ActivityDetail info){
        int num=0;
        List<ActivityReward> oldList=activityRewardService.getActRewardList(info.getActDetCode());
        String strs=handleList(oldList);
        if(info.getActReward()!=null&&info.getActReward().size()>0){
            for(ActivityReward item:info.getActReward()){
                if(item.getId()==null){//新增
                    item.setLinkCode(info.getActDetCode());
                    num=num+activityRewardService.addActRewardInfo(item);
                }else{
                    strs=handleString(strs,item.getId().toString());
                    num=num+activityRewardService.updateActRewardInfo(item);
                }
            }
        }
        //清除删除过的数据
        if(strs!=null&&!",".equals(strs)){
            strs= strs.substring(1,strs.length()-1);
            activityRewardService.updateActRewardList(strs);
        }
        return num;
    }
    private String handleList(List<ActivityReward> list){
        StringBuffer sb=new StringBuffer();
        sb.append(",");
        if(list!=null&&list.size()>0){
            for(ActivityReward item:list){
                sb.append(item.getId()+",");
            }
        }
        return sb.toString();
    }
    /**
     * @param strs ,1,2,3,4,5
     * @param oldStr 4
     */
    private String handleString(String strs,String oldStr){
        String str=","+oldStr+",";
        if(strs.indexOf(str)>-1){
            return strs.replace(str,",");
        }
        return strs;
    }
}
