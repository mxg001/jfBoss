package cn.eeepay.framework.serviceImpl.integral;

import cn.eeepay.framework.dao.activity.ActivityDetailDao;
import cn.eeepay.framework.dao.activity.ActivityManagementDao;
import cn.eeepay.framework.dao.activity.ActivityRewardDao;
import cn.eeepay.framework.dao.activity.PrizeInfoDao;
import cn.eeepay.framework.dao.integral.BusinessInfoDao;
import cn.eeepay.framework.dao.integral.LeaguerDao;
import cn.eeepay.framework.dao.integral.TaskRewardDao;
import cn.eeepay.framework.dao.sys.SysConfigDao;
import cn.eeepay.framework.model.activity.*;
import cn.eeepay.framework.model.integral.BusinessInfo;
import cn.eeepay.framework.model.integral.LeaguerInfo;
import cn.eeepay.framework.model.integral.TaskRewardConfig;
import cn.eeepay.framework.model.sys.UserLoginInfo;
import cn.eeepay.framework.service.activity.ActivityDetailService;
import cn.eeepay.framework.service.activity.ActivityRewardService;
import cn.eeepay.framework.service.activity.ConditionForStandardService;
import cn.eeepay.framework.service.activity.PrizeInfoService;
import cn.eeepay.framework.service.integral.ActivityChoiceService;
import cn.eeepay.framework.service.sys.RandomNumService;
import cn.eeepay.framework.util.CellUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

@Service("activityChoiceService")
public class ActivityChoiceServiceImpl implements ActivityChoiceService {

    private static final Logger log = LoggerFactory.getLogger(ActivityChoiceServiceImpl.class);

    @Resource
    private RandomNumService randomNumService;
    @Resource
    private ActivityManagementDao activityManagementDao;
    @Resource
    private BusinessInfoDao businessInfoDao;
    @Resource
    private ActivityDetailDao activityDetailDao;
    @Resource
    private ActivityRewardDao activityRewardDao;
    @Resource
    private PrizeInfoDao prizeInfoDao;
    @Resource
    private LeaguerDao leaguerDao;
    @Resource
    private SysConfigDao sysConfigDao;
    @Resource
    private TaskRewardDao taskRewardDao;
    @Resource
    private ActivityDetailService activityDetailService;
    @Resource
    private PrizeInfoService prizeInfoService;
    @Resource
    private ActivityRewardService activityRewardService;
    @Resource
    private ConditionForStandardService conditionForStandardService;
    /**
     * 业务关系紧密，引用了各子集dao
     * @param businessNo
     * @param ids
     * @param msg
     * @return
     */
    @Transactional(value = "transactionManager")
    @Override
    public int addActivityList(String businessNo, String ids, Map<String, Object> msg) {
        if(StringUtils.isNotBlank(ids)){
            BusinessInfo info=businessInfoDao.BusinessInfo(businessNo);
            if(info!=null){
                String[] strs=ids.split(",");
                if(strs!=null&&strs.length>0){
                    int num=0;
                    for(String str:strs){
                        num = num+copyActivity(Long.valueOf(str),info.getBusinessNo());
                    }
                    msg.put("status", true);
                    msg.put("msg", "任务选择添加成功!");
                    return  num;
                }
            }
        }
        msg.put("status", false);
        msg.put("msg", "任务选择添加失败!");
        return 0;
    }

    @Override
    @Transactional(value = "transactionManager")
    public int deleteActivityList(String businessNo, String actCode, Map<String, Object> msg) {
        ActivityManagement oldActi=activityManagementDao.getActivityInfo(actCode,businessNo);
        int num=0;
        if(oldActi!=null){
            num= activityManagementDao.deleteActivity(oldActi);
            if(num>0){
                if("1".equals(oldActi.getActType())) {//用户激活
                    List<ActivityDetail> oldActiDeList= activityDetailDao.getActivityDetailList(oldActi.getActCode());
                    if(oldActiDeList!=null&&oldActiDeList.size()>0) {
                        for (ActivityDetail itemDe : oldActiDeList) {
                            int num1=activityDetailDao.deleteActiInfoByCode(itemDe.getActDetCode());
                            if(num1>0){
                                List<ActivityReward> oldActiReList=activityRewardDao.getActRewardList(itemDe.getActDetCode());
                                if(oldActiReList!=null&&oldActiReList.size()>0) {
                                    for (ActivityReward itemRe : oldActiReList) {
                                        activityRewardDao.deleteActReward(itemRe);
                                    }
                                }
                            }
                        }
                    }
                }else if("2".equals(oldActi.getActType())) {//积分抽奖
                    ActivityDetail oldActiDe= activityDetailDao.getLuckDrawpDetail(oldActi.getActCode());
                    int num1=activityDetailDao.deleteActiInfoByCode(oldActiDe.getActDetCode());
                    if(num1>0){
                        List<PrizeInfo> oldPrList=prizeInfoDao.getPrizeInfoList(oldActi.getActCode());
                        if(oldPrList!=null&&oldPrList.size()>0) {
                            for (PrizeInfo oldItemPr : oldPrList) {
                                int num2=prizeInfoDao.deletePrizeInfo(oldItemPr.getId());
                                if(num2>0){
                                    List<ActivityReward> oldActiReList=activityRewardDao.getActRewardList(oldItemPr.getAwardCode());
                                    if(oldActiReList!=null&&oldActiReList.size()>0) {
                                        for (ActivityReward itemRe : oldActiReList) {
                                            activityRewardDao.deleteActReward(itemRe);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }else if("3".equals(oldActi.getActType())){     //会员礼遇季
                    final List<ActivityDetail> activityDetail = activityDetailService.getActivityDetailByActCodeAndActiveType(oldActi.getActCode(), Arrays.asList(5,6,7,8,9,10));
                    activityDetailService.deleteActiInfo(oldActi.getActCode());
                    if(CollectionUtil.isNotEmpty(activityDetail)){
                        for (ActivityDetail detail : activityDetail) {
                            conditionForStandardService.deleteConditionByActDetCode(detail.getActDetCode());
                            final List<PrizeInfo> luckPrizeByActCodeAndActDetCode = prizeInfoService.getLuckPrizeByActCodeAndActDetCode(actCode, detail.getActDetCode());
                            if(CollectionUtil.isNotEmpty(luckPrizeByActCodeAndActDetCode)){
                                for (PrizeInfo prizeInfo : luckPrizeByActCodeAndActDetCode) {
                                    prizeInfoService.deletePrizeInfoAndActivityReward(prizeInfo);
                                }
                            }
                        }
                    }
                }
            }
        }
        return num;
    }

    /**
     * @Type 1 覆盖导入 2追加导入
     */
    @Override
    @Transactional(value = "transactionManager")
    public Map<String, Object> importMember(MultipartFile file, ActivityManagement info,String businessNo,String type){
        Map<String, Object> msg = new HashMap<>();
        Workbook wb =null;
        InputStream inputStream=null;
        List<RangeName> addList=new ArrayList<RangeName>();
        try{
            inputStream=file.getInputStream();
            wb = WorkbookFactory.create(inputStream);
            //读取第一个sheet
            Sheet sheet = wb.getSheetAt(0);
            // 遍历所有单元格，读取单元格
            int row_num = sheet.getLastRowNum();
            List<String> checkList=new ArrayList<String>();//校验
            UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if(row_num>50000){
                msg.put("result", false);
                msg.put("msg", "批量导入,最大条数50000条!");
                return msg;
            }

            for (int i = 1; i <= row_num; i++) {
                Row row = sheet.getRow(i);
                String businessName = CellUtil.getCellValue(row.getCell(0));//业务组
                String originUserNo = CellUtil.getCellValue(row.getCell(1));//用户编号
                String leaguerNo = CellUtil.getCellValue(row.getCell(2));//会员编号

                if(businessName==null||"".equals(businessName)){
                    msg.put("status", false);
                    msg.put("msg","导入失败,第"+(i+1)+"行,业务组为空!");
                    return msg;
                }
                BusinessInfo busInfo=businessInfoDao.BusinessInfoByName(businessName);
                if(busInfo==null){
                    msg.put("status", false);
                    msg.put("msg", "导入失败,第"+(i+1)+"行,业务组("+businessName+")不存在!");
                    return msg;
                }else{
                    if(!busInfo.getBusinessNo().equals(businessNo)){
                        msg.put("status", false);
                        msg.put("msg", "导入失败,第"+(i+1)+"行,业务组("+businessName+")与导入的活动不一致!");
                        return msg;
                    }
                }
                if(originUserNo==null||"".equals(originUserNo)){
                    msg.put("status", false);
                    msg.put("msg","导入失败,第"+(i+1)+"行,用户编号为空!");
                    return msg;
                }
                String originUserNo1=originUserNo.split("\\.")[0];
                LeaguerInfo leaInfo=leaguerDao.getLeaguerByOriNo(originUserNo1,busInfo.getBusinessNo());
                if(leaInfo==null){
                    msg.put("status", false);
                    msg.put("msg", "导入失败,第"+(i+1)+"行,该业务组("+businessName+")下用户编号("+originUserNo1+")的会员不存在!");
                    return msg;
                }else{
                    if("2".equals(type)){
                        //追加导入时,校验该会员是否已存在
                        List<RangeName> checkListRange=activityManagementDao.getRangeNameInfo(leaInfo.getLeaguerNo(),info.getActCode());
                        if(checkListRange!=null&&checkListRange.size()>0){
                            msg.put("status", false);
                            msg.put("msg", "导入失败,第"+(i+1)+"行,该业务组("+businessName+")下用户编号("+originUserNo1+")的会员已存在列表中了!");
                            return msg;
                        }
                    }
                }
                if(checkList.contains(leaInfo.getLeaguerNo())){
                    msg.put("status", false);
                    msg.put("msg", "导入失败,第"+(i+1)+"行,该业务组("+businessName+")下用户编号("+originUserNo1+")的会员重复!");
                    return msg;
                }else{
                    checkList.add(leaInfo.getLeaguerNo());
                }
                RangeName addInfo =new RangeName();
                addInfo.setLeaguerNo(leaInfo.getLeaguerNo());
                addInfo.setActCode(info.getActCode());
                addInfo.setOperator(principal.getUsername());
                addList.add(addInfo);
            }
        }catch (Exception e){
            log.error("适用范围导入失败",e);
            msg.put("status", false);
            msg.put("msg", "适用范围导入失败");
            return msg;
        }finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(wb!=null){
                try {
                    wb.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //数据操作--------------

        if("1".equals(type)){//覆盖导入
            activityManagementDao.deleteActRangeList(info.getActCode());
        }
        int num=0;
        if(addList.size()>0){
            int page=1;
            int pagesize=10000;
            while (true){
                List<RangeName> subList=pageBySubList(addList,pagesize,page);
                if(subList==null||subList.size()<=0){
                    break;
                }
                num=num+activityManagementDao.insertRangeNameBatch(subList);
                page=page+1;
            }
        }
        msg.put("status", true);
        msg.put("msg", "导入成功,总共"+addList.size()+"条数据,成功导入"+num+"条");
        return msg;
    }
    /**
     * 利用subList方法进行分页
     * @param list 分页数据
     * @param pagesize  页面大小
     * @param currentPage   当前页面
     */
    private  List<RangeName> pageBySubList(List<RangeName> list, int pagesize, int currentPage) {
        int totalcount = list.size();
        int pagecount = 0;
        List<RangeName> subList;
        if((currentPage - 1) * pagesize>=totalcount){
            return null;
        }
        int m = totalcount % pagesize;
        if (m > 0) {
            pagecount = totalcount / pagesize + 1;
        } else {
            pagecount = totalcount / pagesize;
        }
        if (m == 0) {
            subList = list.subList((currentPage - 1) * pagesize, pagesize * (currentPage));
        } else {
            if (currentPage == pagecount) {
                subList = list.subList((currentPage - 1) * pagesize, totalcount);
            } else {
                subList = list.subList((currentPage - 1) * pagesize, pagesize * (currentPage));
            }
        }
        return subList;
    }

    @Override
    public ShowConfig getShowConfig(String actCode) {
        ShowConfig info=activityManagementDao.getShowConfig(actCode);
        if(info!=null&&info.getConfigData()!=null&&!"".equals(info.getConfigData())){
            info.setWinningList(JSON.parseObject(info.getConfigData(), WinningList.class));
        }
        return info;
    }

    @Override
    @Transactional(value = "transactionManager")
    public int saveShowConfig(ShowConfig info) {
        int num=0;
        if(info!=null){
            if(info.getWinningList()!=null){
                info.setConfigData(JSON.toJSONString(info.getWinningList()));
            }
            if(info.getId()==null){
                UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                info.setOperater(principal.getUsername());
                num=activityManagementDao.addShowConfig(info);
            }else{
                num=activityManagementDao.updateShowConfig(info);
            }
        }
        return num;
    }

    @Override
    public boolean checkGood(String goodsCode) {
        ActivityReward oldInfo1=activityRewardDao.getRewardInfoByGoodCode(goodsCode);
        if(oldInfo1!=null){
            return true;
        }
        TaskRewardConfig oldInfo2=taskRewardDao.getTaskRewardEntryByGoodCode(goodsCode);
        if(oldInfo2!=null){
            return true;
        }
        return false;
    }

    /**
     * 复制活动模板数据，以及下级所有关联数据
     * @param id
     * @param businessNo
     */
    private int copyActivity(Long  id,String businessNo){
        ActivityManagement oldActi=activityManagementDao.getActivityInfoById(id,"0");
        ActivityManagement newActi=new ActivityManagement();
        newActi.setActCode(randomNumService.getRandomNumberByData("AT","vipScore",5));
        //生成链接
        newActi.setActLink(sysConfigDao.getByKey("JFAPI_SERVICE_OPEN_URL")+"/th/activity/"+newActi.getActCode());
        newActi.setSeqNo(1);
        newActi.setBusinessNo(businessNo);
        newActi.setOriActCode(oldActi.getActCode());
        newActi.setWithdrawalTimeEnd(oldActi.getWithdrawalTimeEnd());
        int num=activityManagementDao.insertCopyActivity(newActi,oldActi); //添加vip_activity表数据
        if(num>0){
            if("1".equals(oldActi.getActType())){//用户激活
                List<ActivityDetail> oldActiDeList= activityDetailDao.getActivityDetailList(oldActi.getActCode());
                if(oldActiDeList!=null&&oldActiDeList.size()>0){
                    for(ActivityDetail itemDe:oldActiDeList){
                        ActivityDetail newDe =new ActivityDetail();
                        newDe.setActDetCode(randomNumService.getRandomNumberByData("AD","vipScore",5));
                        newDe.setActCode(newActi.getActCode());
                        int num1=activityDetailDao.insertCopyActiDeInfo(newDe,itemDe); //添加vip_activity_detail表数据
                        if(num1>0){
                            List<ActivityReward> oldActiReList=activityRewardDao.getActRewardList(itemDe.getActDetCode());
                            if(oldActiReList!=null&&oldActiReList.size()>0){
                                for(ActivityReward itemRe:oldActiReList){
                                    ActivityReward newRe=new ActivityReward();
                                    newRe.setActRewardNo(randomNumService.getRandomNumberByData("AR","vipScore",5));
                                    newRe.setLinkCode(newDe.getActDetCode());
                                    activityRewardDao.insertCopyActiReInfo(newRe,itemRe);//添加vip_activity_reward表数据
                                }
                            }
                        }
                    }
                }
            }else if("2".equals(oldActi.getActType())){//积分抽奖
                ActivityDetail oldActiDe= activityDetailDao.getLuckDrawpDetail(oldActi.getActCode());
                ActivityDetail newActiDe =new ActivityDetail();
                newActiDe.setActDetCode(randomNumService.getRandomNumberByData("AD","vipScore",5));
                newActiDe.setActCode(newActi.getActCode());
                int num1=activityDetailDao.insertCopyActiDeInfo(newActiDe,oldActiDe);//添加vip_activity_detail表数据
                if(num1>0){
                    List<PrizeInfo> oldPrList=prizeInfoDao.getPrizeInfoList(oldActi.getActCode());
                    if(oldPrList!=null&&oldPrList.size()>0){
                        for(PrizeInfo oldItemPr:oldPrList){
                            PrizeInfo nowItemPr=new PrizeInfo();
                            nowItemPr.setAwardCode(randomNumService.getRandomNumberByData("LA","vipScore",5));
                            nowItemPr.setActCode(newActiDe.getActCode());
                            nowItemPr.setActDetCode(newActiDe.getActDetCode());
                            nowItemPr.setOriAwardCode(oldItemPr.getAwardCode());
                            int num2=prizeInfoDao.insertCopyPrInfo(nowItemPr,oldItemPr);//添加vip_lottery_award_config表数据
                            if(num2>0){
                                List<ActivityReward> oldActiReList=activityRewardDao.getActRewardList(oldItemPr.getAwardCode());
                                if(oldActiReList!=null&&oldActiReList.size()>0){
                                    for(ActivityReward itemRe:oldActiReList){
                                        ActivityReward newRe=new ActivityReward();
                                        newRe.setActRewardNo(randomNumService.getRandomNumberByData("AR","vipScore",5));
                                        newRe.setLinkCode(nowItemPr.getAwardCode());
                                        activityRewardDao.insertCopyActiReInfo(newRe,itemRe);//添加vip_activity_reward表数据
                                    }
                                }
                            }
                        }
                    }
                }
            }else if("3".equals(oldActi.getActType())){     //会员礼遇季红包
                final List<ActivityDetail> activityDetail = activityDetailService.getActivityDetailByActCodeAndActiveType(oldActi.getActCode(), Arrays.asList(5,6,7,8,9,10));
                if(activityDetail!=null){
                    for (ActivityDetail detail : activityDetail) {
                        List<PrizeInfo> prizeInfos = prizeInfoService.getLuckPrizeByActCodeAndActDetCode(oldActi.getActCode(),detail.getActDetCode());
                        final ConditionForStandard conditionByActDetCode = conditionForStandardService.findConditionByActDetCode(detail.getActDetCode());

                        detail.setActDetCode(randomNumService.getRandomNumberByData("AD","vipScore",5));
                        detail.setActCode(newActi.getActCode());
                        activityDetailService.insertActiInfo(detail);

                        if(conditionByActDetCode != null){
                            conditionByActDetCode.setId(null);
                            conditionByActDetCode.setActDetCode(detail.getActDetCode());
                            conditionForStandardService.insertCondition(conditionByActDetCode);
                        }

                        if(prizeInfos != null) {
                            for (PrizeInfo prizeInfo : prizeInfos) {
                                final String awardCode = prizeInfo.getAwardCode();
                                prizeInfo.setActCode(newActi.getActCode());
                                prizeInfo.setActDetCode(detail.getActDetCode());
                                prizeInfoService.addLuckPrizeTemplate(prizeInfo,new HashMap<>());
                                ActivityReward actRewardInfo = activityRewardService.getActRewardInfo(awardCode);
                                actRewardInfo.setLinkCode(prizeInfo.getAwardCode());
                                activityRewardService.addActRewardInfo(actRewardInfo);
                            }
                        }
                    }
                }
            }
        }
        return num;
    }
}
