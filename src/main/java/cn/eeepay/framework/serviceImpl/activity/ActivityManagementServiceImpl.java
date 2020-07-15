package cn.eeepay.framework.serviceImpl.activity;

import cn.eeepay.framework.dao.activity.ActivityManagementDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.activity.ActivityManagement;
import cn.eeepay.framework.model.activity.RangeName;
import cn.eeepay.framework.model.sys.UserLoginInfo;
import cn.eeepay.framework.service.activity.ActivityDetailService;
import cn.eeepay.framework.service.activity.ActivityManagementService;
import cn.eeepay.framework.service.activity.PrizeInfoService;
import cn.eeepay.framework.util.CommonUtil;
import cn.eeepay.framework.util.StringUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service("activityManagementService")
public class ActivityManagementServiceImpl implements ActivityManagementService {

    @Resource
    private ActivityManagementDao activityManagementDao;
    @Resource
    private PrizeInfoService prizeInfoService;
    @Resource
    private ActivityDetailService activityDetailService;
    @Override
    public List<ActivityManagement> getActivityList() {
        return activityManagementDao.getActivityList();
    }

    /**
     * 获取抽奖详情
     * @param actCode
     * @param businessNo
     * @return
     */
    @Override
    public ActivityManagement getLuckDrawpConfig(String actCode, String businessNo) {
        ActivityManagement info=activityManagementDao.getActivityInfo(actCode,businessNo);
        if(info!=null){
            info.setLuckDrawpDetail(activityDetailService.getLuckDrawpDetail(actCode));
            info.setPrizeInfoList(prizeInfoService.getPrizeInfoList(actCode));
        }
        return info;
    }

    @Override
    public ActivityManagement findActivityDetailByActCodeAndBusinessNo(String actCode, String businessNo) {
        return activityManagementDao.findActivityDetailByActCodeAndBusinessNo(actCode,businessNo);
    }

    /**
     * 修改 抽奖基础配置
     * @param info
     * @return
     */
    @Transactional(value = "transactionManager")
    @Override
    public int saveLuckTemplate(ActivityManagement info) {
        int num=activityManagementDao.saveActivity(info);
        if(num>0){
            if(info.getLuckDrawpDetail()!=null){
                activityDetailService.saveLuckDetTemplate(info.getActCode(),info.getLuckDrawpDetail());
            }
        }
        return num;
    }

    @Override
    public ActivityManagement getActivityTemp(String actCode, String businessNo) {
        ActivityManagement info=activityManagementDao.getActivityInfo(actCode,businessNo);
        if(info!=null){
            info.setActImg(CommonUtil.getImgUrlAgent(info.getActImg()));
            info.setActiDetailList(activityDetailService.getActivityDetailList(actCode));
        }
        return info;
    }

    @Transactional(value = "transactionManager")
    @Override
    public int saveActivityTemp(ActivityManagement info) {
        int num=activityManagementDao.saveActivity(info);
        return num;
    }

    //业务组下--
    @Override
    public List<ActivityManagement> getActivityListBusNo(ActivityManagement info) {
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setBusinessNoFilter(principal.getBusinessNos());
        List<ActivityManagement>  list=activityManagementDao.getActivityListBusNo(info);
        if(list!=null&&list.size()>0){
            for(ActivityManagement item:list){
                if(item.getOpenStatus()!=null){
                    item.setOpenStatusInt(Integer.valueOf(item.getOpenStatus()));
                }
                if(item.getShowStatus()!=null){
                    item.setShowStatusInt(Integer.valueOf(item.getShowStatus()));
                }
            }
        }
        return list;
    }

    @Override
    public ActivityManagement checkActivity(String actCode, String businessNo) {
        return activityManagementDao.getActivityInfo(actCode,businessNo);
    }

    @Override
    @Transactional(value = "transactionManager")
    public int activitySwitch(Long id, int status, String businessNo) {
        return activityManagementDao.activitySwitch(id,status,businessNo);
    }

    @Override
    @Transactional(value = "transactionManager")
    public int homePageSwitch(Long id, int status, String businessNo) {
        return activityManagementDao.homePageSwitch(id,status,businessNo);
    }

    @Override
    @Transactional(value = "transactionManager")
    public int importMemberAll(String actCode, String actRangeType) {
        int num=activityManagementDao.updateActRangeType(actCode,actRangeType);
        //清空数据
        if("0".equals(actRangeType)){
            activityManagementDao.deleteActRangeList(actCode);
        }
        return num;
    }

    @Override
    public List<RangeName> getRangeNameList(RangeName info, Page<RangeName> page) {
        List<RangeName> list=activityManagementDao.getRangeNameList(info,page);
        if(page.getResult()!=null&&page.getResult().size()>0){
            for(RangeName item:page.getResult()){
                item.setMobilePhone(StringUtil.sensitiveInformationHandle(item.getMobilePhone(),0));
            }
        }
        return list;
    }

    @Override
    public int addRangeName(RangeName info, Map<String, Object> msg) {
        List<RangeName> checkListRange=activityManagementDao.getRangeNameInfo(info.getLeaguerNo(),info.getActCode());
        if(checkListRange!=null&&checkListRange.size()>0){
            msg.put("status", false);
            msg.put("msg", "新增失败-该会员已存在!");
            return 0;
        }
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setOperator(principal.getUsername());
        int num =activityManagementDao.insertRangeName(info);
        if(num>0){
            msg.put("status", true);
            msg.put("msg", "新增成功!");
        }else{
            msg.put("status", false);
            msg.put("msg", "新增失败!");
        }
        return num;
    }

    @Override
    public int deleteRangeName(RangeName info) {
        return activityManagementDao.deleteRangeName(info);
    }

}
