package cn.eeepay.framework.serviceImpl.activity;

import cn.eeepay.framework.dao.activity.PrizeInfoDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.activity.ActivityDetail;
import cn.eeepay.framework.model.activity.ActivityReward;
import cn.eeepay.framework.model.activity.AwardBlack;
import cn.eeepay.framework.model.activity.PrizeInfo;
import cn.eeepay.framework.model.sys.UserLoginInfo;
import cn.eeepay.framework.service.activity.ActivityRewardService;
import cn.eeepay.framework.service.activity.PrizeInfoService;
import cn.eeepay.framework.service.sys.RandomNumService;
import cn.eeepay.framework.util.CommonUtil;
import cn.eeepay.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service("prizeInfoService")
public class PrizeInfoServiceImpl implements PrizeInfoService {

    @Resource
    private PrizeInfoDao prizeInfoDao;
    @Resource
    private RandomNumService randomNumService;
    @Resource
    private ActivityRewardService activityRewardService;

    @Override
    public List<PrizeInfo> getPrizeInfoList(String actCode) {
        List<PrizeInfo> list=prizeInfoDao.getPrizeInfoList(actCode);
        if(list!=null&&list.size()>0){
            for(PrizeInfo item:list){
                item.setOpenStatusInt(Integer.valueOf(item.getOpenStatus()));
            }
        }
        return list;
    }
    @Transactional(value = "transactionManager")
    @Override
    public int addLuckPrizeTemplate(PrizeInfo info, Map<String, Object> msg) {
        if(!checkPrizeInfo(info)){
            msg.put("status", false);
            msg.put("msg", "新增失败,所属奖项概率超出100%!");
            return 2;
        }else{
            info.setAwardCode(randomNumService.getRandomNumberByData("LA","vipScore",5));
            if(info.getPrize()!=null&&"99".equals(info.getPrize().getRewardType())){
                info.setIsShot("0");
            }else{
                info.setIsShot("1");
            }
            if(StringUtils.isBlank(info.getOpenStatus())){
                info.setOpenStatus("0");
            }
            int num=prizeInfoDao.addLuckPrizeTemplate(info);
            if(num>0){
                savePrizeDetail(info);
                msg.put("status", true);
                msg.put("msg", "新增成功!");
                return 1;
            }else{
                msg.put("status", false);
                msg.put("msg", "新增失败!");
            }
        }
        return 0;
    }


    @Transactional(value = "transactionManager")
    @Override
    public int addLuckPrizeTemplate1(PrizeInfo info, Map<String, Object> msg) {
        if(StringUtils.isBlank(info.getAwardCode())){
            info.setAwardCode(randomNumService.getRandomNumberByData("LA","vipScore",5));
        }
        if(info.getPrize()!=null&&"99".equals(info.getPrize().getRewardType())){
            info.setIsShot("0");
        }else{
            info.setIsShot("1");
        }
        if(StringUtils.isBlank(info.getOpenStatus())){
            info.setOpenStatus("0");
        }
        int num=prizeInfoDao.addLuckPrizeTemplate1(info);
        if(num>0){
            savePrizeDetail(info);
            msg.put("status", true);
            msg.put("msg", "新增成功!");
            return 1;
        }else{
            msg.put("status", false);
            msg.put("msg", "新增失败!");
        }
        return 0;
    }

    @Override
    public PrizeInfo getLuckPrize(String awardCode) {
        PrizeInfo info=getPrizeInfo(null,awardCode,false ,true);
        return info;
    }

    @Transactional(value = "transactionManager")
    @Override
    public int editLuckPrizeTemplate(PrizeInfo info, Map<String, Object> msg) {
        if(!checkPrizeInfo(info)){
            msg.put("status", false);
            msg.put("msg", "修改失败,所属奖项概率超出100%!");
            return 2;
        }else{
            if(info.getPrize()!=null&&"99".equals(info.getPrize().getRewardType())){
                info.setIsShot("0");
            }else{
                info.setIsShot("1");
            }
            int num=prizeInfoDao.updateLuckPrize(info);
            if(num>0){
                savePrizeDetail(info);
                msg.put("status", true);
                msg.put("msg", "修改成功!");
                return 1;
            }else{
                msg.put("status", false);
                msg.put("msg", "修改失败!");
            }
        }
        return 0;
    }

    @Transactional(value = "transactionManager")
    @Override
    public int closeOpenPrize(Long id, int status) {
        return prizeInfoDao.closeOpenPrize(id,status);
    }

    @Transactional(value = "transactionManager")
    @Override
    public int deletePrize(Long id) {
        PrizeInfo oldInfo =getPrizeInfo(id,null,true ,false);
        return deletePrizeInfo(oldInfo);
    }

    @Override
    public PrizeInfo getLuckPrizeById(Long id) {
        return prizeInfoDao.getPrizeInfoById(id);
    }

    @Override
    public PrizeInfo getLuckPrizeByCode(String awardCode) {
        return  prizeInfoDao.getPrizeInfoByCode(awardCode);
    }

    @Override
    public List<AwardBlack> getAwardBlackList(AwardBlack info, Page<AwardBlack> page) {
        return prizeInfoDao.getAwardBlackList(info,page);
    }

    @Override
    @Transactional(value = "transactionManager")
    public int addAwardBlack(AwardBlack info) {
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setOperator(principal.getUsername());
        return prizeInfoDao.addAwardBlack(info);
    }

    @Override
    @Transactional(value = "transactionManager")
    public int deleteAwardBlack(AwardBlack info) {
        return prizeInfoDao.deleteAwardBlack(info);
    }

    @Override
    public int prizeTimesReset() {
        return prizeInfoDao.prizeTimesReset();
    }

    @Override
    public List<PrizeInfo> getLuckPrizeByActCodeAndActDetCode(String actCode, String actDetCode) {
        return prizeInfoDao.getLuckPrizeByActCodeAndActDetCode(actCode,actDetCode);
    }

    @Override
    public int deletePrizeInfoAndActivityReward(PrizeInfo prizeInfo) {
        int num=prizeInfoDao.deletePrizeInfo(prizeInfo.getId());
        if(num>0){
            activityRewardService.deleteActRewardByLinkCode(prizeInfo.getAwardCode());
        }
        return num;
    }


    private int deletePrizeInfo(PrizeInfo oldInfo){
        int num=prizeInfoDao.deletePrizeInfo(oldInfo.getId());
        if(num>0){
            activityRewardService.deleteActReward(oldInfo.getPrize());
        }
        return num;
    }

    /**
     * 通过不同的条件，获取奖项数据
     * @param id
     * @param awardCode
     * @param idSta ture ID,false code
     * @param imgSta 是否需要图片链接转换
     * @return
     */
    private PrizeInfo getPrizeInfo(Long id,String awardCode,boolean idSta,boolean imgSta){
        PrizeInfo info=null;
        if(idSta){
            if(id!=null){
                info=prizeInfoDao.getPrizeInfoById(id);
            }
        }else{
            if(StringUtils.isNotBlank(awardCode)){
                info=prizeInfoDao.getPrizeInfoByCode(awardCode);
            }
        }
        if(info!=null){
            if(imgSta){
                info.setAwardImg(CommonUtil.getImgUrlAgent(info.getAwardImg()));
            }
            info.setPrize(activityRewardService.getActRewardInfo(info.getAwardCode()));
        }
        return info;
    }

    private int savePrizeDetail(PrizeInfo info){
        int num=0;
        if(info!=null&&info.getPrize()!=null){
            ActivityReward addInfo=info.getPrize();
            addInfo.setLinkCode(info.getAwardCode());
            if(addInfo.getId()==null){
                num=activityRewardService.addActRewardInfo(addInfo);
            }else{
                num=activityRewardService.updateActRewardInfo(addInfo);
            }
        }
        return num;
    }

    /**
     * 交易所有奖项概率是否超过100%
     * @param info
     * @return
     */
    private boolean checkPrizeInfo(PrizeInfo info){
        BigDecimal count=prizeInfoDao.checkPrize(info);
        if(count!=null){
            if(count.add(info.getAwardOdds()).compareTo(new BigDecimal("100"))>0){
                return false;
            }
        }else{
            if(info.getAwardOdds().compareTo(new BigDecimal("100"))>0){
                return false;
            }
        }
        return true;
    }
}
