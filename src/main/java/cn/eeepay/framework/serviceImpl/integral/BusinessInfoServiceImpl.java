package cn.eeepay.framework.serviceImpl.integral;

import cn.eeepay.framework.dao.integral.BusinessInfoDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.integral.*;
import cn.eeepay.framework.model.sys.UserLoginInfo;
import cn.eeepay.framework.service.integral.BusinessInfoService;
import cn.eeepay.framework.service.integral.TaskRewardService;
import cn.eeepay.framework.service.sys.RandomNumService;
import cn.eeepay.framework.util.CommonUtil;
import cn.eeepay.framework.util.RandomNumber;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Administrator on 2019/7/2/002.
 * @author liuks
 * 业务组操作service实现类
 */
@Service("businessInfoService")
public class BusinessInfoServiceImpl implements BusinessInfoService {


    @Resource
    private BusinessInfoDao businessInfoDao;
    @Resource
    private TaskRewardService taskRewardService;
    @Resource
    private RandomNumService randomNumService;

    @Override
    public List<BusinessInfo> getBusinessInfoListAll() {
        return businessInfoDao.getBusinessInfoListAll();
    }
    @Override
    public List<BusinessInfo> getBusinessInfoListByUser() {
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return businessInfoDao.getBusinessInfoListByUser(principal.getBusinessNos());
    }

    @Override
    public List<BusinessInfo> getSelectPageList(BusinessInfo info, Page<BusinessInfo> page) {
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return businessInfoDao.getSelectPageList(info,page,principal.getBusinessNos());
    }

    @Override
    @Transactional(value = "transactionManager")
    public int addBusinessInfo(BusinessInfo info, Map<String, Object> msg) {
        if(info!=null&& StringUtils.isNotBlank(info.getBusinessNo())&&StringUtils.isNotBlank(info.getBusinessName())){
            if(checkData(info,msg)){
                //可以新增
                info.setDigestKey(RandomNumber.mumberRandom("KEY",10,4));//生成验签
                int num=businessInfoDao.addBusinessInfo(info);
                if(num>0){
                    msg.put("status", true);
                    msg.put("msg", "新增业务组成功!");
                    return num;
                }else{
                    msg.put("status", false);
                    msg.put("msg", "新增业务组失败!");
                }
            }
        }else{
            msg.put("status", false);
            msg.put("msg", "新增业务组失败,业务组不能为空!");
        }
        return 0;
    }

    @Override
    public BusinessInfo getBusinessBasic(String businessNo,Map<String,Object> msg) {
        BusinessInfo info=businessInfoDao.BusinessInfo(businessNo);
        if(info!=null){
            info.setGrowLevelInfoList(businessInfoDao.getGrowLevelInfoList(info.getBusinessNo()));
            info.setmLevelInfoList(businessInfoDao.getMLevelInfoList(info.getBusinessNo()));
            msg.put("status",true);
            msg.put("info",info);
            return info;
        }
        msg.put("status",false);
        msg.put("msg","获取基本业务组信息失败");
        return info;
    }

    @Override
    @Transactional(value = "transactionManager")
    public int updateBusinessBasic(BusinessInfo info, Map<String, Object> msg) {
        info.setLevelNum(BigDecimal.ZERO);//固定值暂时未用上为0
        int num=businessInfoDao.updateBusinessBasic(info);
        if(num>0){
            updateGrowLevelList(info);
            updateMLevelInfo(info);
            msg.put("status", true);
            msg.put("msg", "基本资料保存成功!");
            return  num;
        }
        msg.put("status", false);
        msg.put("msg", "基本资料保存失败!");
        return 0;
    }

    @Override
    public void getBusinessTaskList(String businessNo,Map<String,Object> msg) {
        BusinessInfo info=businessInfoDao.BusinessInfo(businessNo);
        if(info!=null){
            List<BusinessTask> list=businessInfoDao.getBusinessTaskList(info.getBusinessNo());
            msg.put("list", list);
            msg.put("status", true);
            return;
        }
        msg.put("msg", "获取业务组-任务选择失败!");
        msg.put("status", false);
        return;
    }

    @Override
    @Transactional(value = "transactionManager")
    public int addBusinessTask(String businessNo, String ids, Map<String, Object> msg) {
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(StringUtils.isNotBlank(ids)){
            BusinessInfo info=businessInfoDao.BusinessInfo(businessNo);
            if(info!=null){
                String[] strs=ids.split(",");
                if(strs!=null&&strs.length>0){
                    int num=0;
                    for(String str:strs){
                        TaskRewardInfo oldInfo=taskRewardService.getTaskRewardInfoToCopy(Long.valueOf(str));
                        num = num+addTaskRewardInfo(info.getBusinessNo(),principal.getUsername(),oldInfo);
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

    /**
     * 新增业务组-任务
     * @param businessNo
     * @param userName
     * @param oldInfo
     * @return
     */
    private int addTaskRewardInfo(String businessNo,String userName,TaskRewardInfo oldInfo){
        int num=0;
        if(checkBusinessTask(businessNo,oldInfo.getTaskNo())){
            //新增主数据
            BusinessTask addInfo=new BusinessTask();

            addInfo.setBusinessNo(businessNo);
            addInfo.setTaskNo(oldInfo.getTaskNo());
            addInfo.setTaskName(oldInfo.getTaskName());
            addInfo.setTaskType(oldInfo.getTaskType());
            addInfo.setTaskDesc(oldInfo.getTaskDesc());
            addInfo.setTaskParams(oldInfo.getTaskParams());
            addInfo.setRestrictNum(oldInfo.getRestrictNum());
            addInfo.setRestrictFrequency(oldInfo.getRestrictFrequency());
            addInfo.setFrequencyType(oldInfo.getFrequencyType());
            addInfo.setCreater(userName);
            addInfo.setTaskOverview(oldInfo.getTaskOverview());
            addInfo.setGrantType(oldInfo.getGrantType());
            addInfo.setImgUrl(oldInfo.getImgUrl());
            num=businessInfoDao.addBusinessTask(addInfo);
            if(num>0){
                //新增子表数据
                addEntry(businessNo,userName,oldInfo);
            }
        }
        return num;
    }

    /**
     * 复制新增权益
     * @param businessNo 业务组编号
     * @param userName 操作人
     * @param oldInfo 原任务实体
     * @return
     */
    private int addEntry(String businessNo,String userName,TaskRewardInfo oldInfo){
        List<TaskRewardConfig> list=oldInfo.getTaskRewardConfigList();
        int num=0;
        if(list!=null&&list.size()>0) {
            for (TaskRewardConfig oldItem : list) {

                TaskRewardConfig addInfo = new TaskRewardConfig();
                addInfo.setBusinessNo(businessNo);
                addInfo.setTaskNo(oldItem.getTaskNo());
                addInfo.setRewardNo(randomNumService.getRandomNumberByData("TR", "vipScore", 5));
                addInfo.setRewardName(oldItem.getRewardName());
                addInfo.setRewardType(oldItem.getRewardType());
                addInfo.setCalcType(oldItem.getCalcType());
                addInfo.setRewardTemplate(oldItem.getRewardTemplate());
                addInfo.setRewardTemplateFormula(oldItem.getRewardTemplateFormula());
                addInfo.setRewardEffeDay(oldItem.getRewardEffeDay());
                addInfo.setRewardNoBack(oldItem.getRewardNo());
                addInfo.setCreater(userName);

                num = num + taskRewardService.addTaskRewardEntry(addInfo);
            }
        }
        return num;
    }

    @Override
    public BusinessTask getBusinessTask(Long id,String businessNo,Map<String, Object> msg) {
        BusinessTask info=businessInfoDao.getBusinessTask(id,businessNo);
        if(info!=null){
            info.setImgUrl(CommonUtil.getImgUrlAgent(info.getImgUrl()));
            info.setTaskRewardConfigList(taskRewardService.getTaskRewardEntry(info.getBusinessNo(),info.getTaskNo()));
            msg.put("info", info);
            msg.put("status", true);
            return info;
        }
        msg.put("status", false);
        msg.put("msg", "获取任务失败!");
        return info;
    }

    @Override
    @Transactional(value = "transactionManager")
    public int editBusinessTask(BusinessTask info, Map<String, Object> msg) {
        if(checkTaskInfo(info,msg)){
            return 0;
        }
        int num=businessInfoDao.editBusinessTask(info);
        if(num>0){
            maintainEntry(info);
            msg.put("status", true);
            msg.put("msg", "修改任务成功!");
        }else{
            msg.put("status", false);
            msg.put("msg", "修改任务失败!");
        }
        return num;
    }
    private boolean checkTaskInfo(BusinessTask info,Map<String, Object> msg){
        if("TYPE002".equals(info.getTaskType())){
            if(info.getRestrictNum()!=null&&info.getRestrictNum().intValue()!=1){
                msg.put("status", false);
                msg.put("msg", "新手任务,可调用次数不为1");
                return true;
            }
        }
        if("1".equals(info.getGrantType())){
            if(info.getRestrictFrequency()!=null&&info.getRestrictFrequency().intValue()!=1){
                msg.put("status", false);
                msg.put("msg", "新手任务,当天调用次数不为1");
                return true;
            }
        }
        if(!"trans".equals(info.getTaskParams())){
            if(info.getTaskRewardConfigList()!=null&&info.getTaskRewardConfigList().size()>0){
                for (TaskRewardConfig taskRewardConfig : info.getTaskRewardConfigList()) {
                    if("2".equals(taskRewardConfig.getCalcType())){
                        msg.put("status",false);
                        msg.put("msg","当前触发类型不能含有公式类型的业务配置!");
                        return true;
                    }
                }
            }
        }
        return false;

    }
    @Override
    @Transactional(value = "transactionManager")
    public int closeBusinessTask(Long id, String businessNo,int sta,Map<String, Object> msg) {
        int num=businessInfoDao.closeBusinessTask(id,businessNo,sta);
        if(num>0){
            msg.put("status", true);
            msg.put("msg", "操作成功!");
            return num;
        }
        msg.put("status", false);
        msg.put("msg", "操作成功失败!");
        return 0;
    }

    @Override
    @Transactional(value = "transactionManager")
    public int deleteBusinessTask(Long id,String businessNo, Map<String, Object> msg) {
        BusinessTask oldInfo=businessInfoDao.getBusinessTask(id,businessNo);
        int num=businessInfoDao.deleteBusinessTask(id,businessNo);
        if(num>0){
            deleteEntry(oldInfo);
            msg.put("status", true);
            msg.put("msg", "删除任务成功!");
            return num;
        }
        msg.put("status", false);
        msg.put("msg", "删除任务失败!");
        return 0;
    }

    private int deleteEntry(BusinessTask info){
        int num=0;
        if(info!=null){
            List<TaskRewardConfig> list=taskRewardService.getTaskRewardEntry(info.getBusinessNo(),info.getTaskNo());
            if(list!=null&&list.size()>0){
                for(TaskRewardConfig item:list){
                    num =num + taskRewardService.deleteTaskReward(item);
                }
            }
        }
        return num;
    }

    private int maintainEntry(BusinessTask info){
        List<TaskRewardConfig> list=info.getTaskRewardConfigList();
        int num=0;
        List<TaskRewardConfig> oldList=taskRewardService.getTaskRewardEntry(info.getBusinessNo(),info.getTaskNo());
        String strs=handleTaskRewardConfig(oldList);
        if(list!=null&&list.size()>0){
            for(TaskRewardConfig item:list){
                if(item.getId()==null){//新增
                    item.setRewardNo(randomNumService.getRandomNumberByData("TR","vipScore",5));
                    item.setCreater(info.getCreater());
                    item.setBusinessNo(info.getBusinessNo());
                    item.setTaskNo(info.getTaskNo());
                    item.setRewardNoBack(null);
                    num= num + taskRewardService.addTaskRewardEntry(item);
                }else{
                    strs=handleString(strs,item.getId().toString());
                    //更新
                    num= num +taskRewardService.updateTaskRewardEntry(item);
                }
            }
        }
        //清除删除过的数据
        if(strs!=null&&!",".equals(strs)){
            strs= strs.substring(1,strs.length()-1);
            taskRewardService.deleteTaskRewardEntry(strs);
        }
        return num;
    }
    private String handleTaskRewardConfig(List<TaskRewardConfig> list){
        StringBuffer sb=new StringBuffer();
        sb.append(",");
        if(list!=null&&list.size()>0){
            for(TaskRewardConfig item:list){
                sb.append(item.getId()+",");
            }
        }
        return sb.toString();
    }

    private boolean checkBusinessTask(String businessNo,String taskNo){
        BusinessTask info=businessInfoDao.checkBusinessTask(businessNo,taskNo);
        if(info==null){
            return true;
        }
        return false;
    }

    private int updateGrowLevelList(BusinessInfo info){
        List<LevelInfo> list=info.getGrowLevelInfoList();
        int num=0;
        //数据库数据
        List<LevelInfo> oldList=businessInfoDao.getGrowLevelInfoList(info.getBusinessNo());
        String strs=handleList(oldList);

        if(list!=null&&list.size()>0){
            for(LevelInfo item:list){
                item.setBusinessNo(info.getBusinessNo());
                if(item.getId()==null){//新增
                    num = num + businessInfoDao.addGrowLevelInfo(item);
                }else{//更新
                    strs=handleString(strs,item.getId().toString());
                    num = num + businessInfoDao.updateGrowLevelInfo(item);
                }
            }
        }
        //清除删除过的数据
        if(strs!=null&&!",".equals(strs)){
            strs= strs.substring(1,strs.length()-1);
            businessInfoDao.deleteGrowLevelInfo(strs);
        }
        return num;
    }
    private int updateMLevelInfo(BusinessInfo info){
        List<LevelInfo> list=info.getmLevelInfoList();
        int num=0;
        //数据库数据
        List<LevelInfo> oldList=businessInfoDao.getMLevelInfoList(info.getBusinessNo());
        String strs=handleList(oldList);

        if(list!=null&&list.size()>0){
            for(LevelInfo item:list){
                item.setBusinessNo(info.getBusinessNo());
                if(item.getId()==null){//新增
                    num = num + businessInfoDao.addMLevelInfo(item);
                }else{//更新
                    strs=handleString(strs,item.getId().toString());
                    num = num + businessInfoDao.updateMLevelInfo(item);
                }
            }
        }
        //清除删除过的数据
        if(strs!=null&&!",".equals(strs)){
            strs= strs.substring(1,strs.length()-1);
            businessInfoDao.deleteMLevelInfo(strs);
        }
        return num;
    }

    private String handleList(List<LevelInfo> list){
        StringBuffer sb=new StringBuffer();
        sb.append(",");
        if(list!=null&&list.size()>0){
            for(LevelInfo item:list){
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


    /**
     * 验证编号，名称是否存在
     * @param info
     * @param msg
     * @return true 不存在重复 false 有重复
     */
    private boolean checkData(BusinessInfo info,Map<String, Object> msg){
        List<BusinessInfo> list=businessInfoDao.checkDataNo(info.getBusinessNo());
        if(list!=null&&list.size()>0){
            msg.put("status", false);
            msg.put("msg", "新增业务组失败,业务组编号已存在!");
            return false;
        }
        List<BusinessInfo> list2=businessInfoDao.checkDataName(info.getBusinessName());
        if(list2!=null&&list2.size()>0){
            msg.put("status", false);
            msg.put("msg", "新增业务组失败,业务组名称已存在!");
            return false;
        }
        return true;
    }


    @Override
    public List<Map<Object,Object>> businessTaskPreview(String businessNo) {
        List<Map<String,Object>> allVipLevels =  this.getAllVipLevels(businessNo);
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BusinessVipLevelInfo info = new BusinessVipLevelInfo();
        info.setBusinessNoFilter(principal.getBusinessNos());
        info.setBusinessNo(businessNo);
        List<Map<Object,Object>> list  = businessInfoDao.businessTaskPreview(info,allVipLevels);

        return list;
    }

    @Override
    public List<Map<String,Object>> getAllVipLevels(String businessNo) {
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BusinessVipLevelInfo info = new BusinessVipLevelInfo();
        info.setBusinessNoFilter(principal.getBusinessNos());
        info.setBusinessNo(businessNo);
        return businessInfoDao.getAllVipLevels(info);
    }

    @Transactional(value = "transactionManager")
    @Override
    public int saveBatchBusinessTaskPreview(String rewardLevel, String businessNo) {
        JSONArray rewardLevelArr = JSON.parseArray(rewardLevel);
        List<Map<Object,Object>> oldlist=businessTaskPreview(businessNo);
        List<BusinessVipLevelInfo> addList=new ArrayList<BusinessVipLevelInfo>();
        List<BusinessVipLevelInfo> updateList=new ArrayList<BusinessVipLevelInfo>();
        String strs=handleListPreview(oldlist);
        for (int i = 0; i < rewardLevelArr.size(); i++) {
            JSONObject jsonObject = JSON.parseObject(rewardLevelArr.get(i).toString());
            String rewardName = jsonObject.getString("rewardName");
            String rewardCode = jsonObject.getString("rewardCode");
            JSONArray isSelects = JSON.parseArray(jsonObject.get("isSelect").toString());
            JSONArray levels = JSON.parseArray(jsonObject.get("levels").toString());
            String newCode="";
            if(StringUtils.isNotBlank(rewardCode)&&rewardCode.indexOf("HM")<=-1){
                strs=handleString(strs,rewardCode);
            }else{
                newCode=randomNumService.getRandomNumberByData("HX","vipScore",5);
            }
            if(levels!=null && isSelects!=null && levels.size()== isSelects.size()){
                for (int j = 0; j < isSelects.size(); j++) {
                    int isSelect = Integer.parseInt(isSelects.get(j).toString());
                    int level = Integer.parseInt(levels.get(j).toString());
                    BusinessVipLevelInfo info = new BusinessVipLevelInfo();
                    info.setRewardName(rewardName);
                    info.setIsSelect(isSelect);
                    info.setBusinessNo(businessNo);
                    info.setLevel(level);

                    if(StringUtils.isNotBlank(rewardCode)&&rewardCode.indexOf("HM")<=-1){
                        info.setRewardCode(rewardCode);
                        updateList.add(info);
                    }else{
                        info.setRewardCode(newCode);
                        addList.add(info);
                    }

                }
            }

        }
        int num=0;
        if(addList.size()>0){
            for(BusinessVipLevelInfo item:addList){
                num=num+businessInfoDao.addBusinessTaskPreview(item);
            }
        }
        if(updateList.size()>0){
            for(BusinessVipLevelInfo item:updateList){
                num=num+businessInfoDao.updateVipRewardPreview(item);
            }
        }
        //清除删除过的数据
        if(strs!=null&&!",".equals(strs)){
            strs= strs.substring(1,strs.length()-1);
            String[] delStrS=strs.split(",");
            if(delStrS!=null&&delStrS.length>0){
                for(String item:delStrS){
                    num=num+businessInfoDao.delBusinessTaskPreview(item,businessNo);
                }
            }
        }
        return num;

    }
    private String handleListPreview(List<Map<Object,Object>> list){
        StringBuffer sb=new StringBuffer();
        sb.append(",");
        if(list!=null&&list.size()>0){
            for(Map<Object,Object> item:list){
                sb.append(item.get("rewardCode")+",");
            }
        }
        return sb.toString();
    }

}
