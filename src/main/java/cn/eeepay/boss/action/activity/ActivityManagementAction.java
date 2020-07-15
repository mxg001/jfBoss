package cn.eeepay.boss.action.activity;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.activity.*;
import cn.eeepay.framework.model.integral.BusinessFlow;
import cn.eeepay.framework.service.activity.*;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/activityManagement")
public class ActivityManagementAction {

    private static final Logger log = LoggerFactory.getLogger(ActivityManagementAction.class);

    @Resource
    private ActivityManagementService activityManagementService;

    @Resource
    private PrizeInfoService prizeInfoService;

    @Resource
    private ActivityDetailService activityDetailService;
    @Resource
    private LeaguerBlacklistService leaguerBlacklistService;
    @Resource
    private LeagerBagSerialService leagerBagSerialService;


    /**
     * 模板活动查询
     */
    @RequestMapping(value = "/getActivityList")
    @ResponseBody
    public Map<String,Object> getActivityList(){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            List<ActivityManagement> list= activityManagementService.getActivityList();
            msg.put("list", list);
            msg.put("status", true);
        } catch (Exception e){
            e.printStackTrace();
            log.error("模板活动查询异常!",e);
            msg.put("status", false);
            msg.put("msg", "模板活动查询异常!");
        }
        return msg;
    }

    /**
     * 下拉框
     * @return
     */
    @RequestMapping("/selectAllActivity")
    @ResponseBody
    public Object selectAllActivity(@Param("businessNo") String businessNo){
        if(StringUtils.isBlank(businessNo)){
            businessNo = "0";
        }
        List<ActivityManagement> list = null;
        try {
            list = activityDetailService.selectAllActivity(businessNo);
        } catch (Exception e) {
            log.error("查询活动下拉列表失败:" + e);
        }
        return list;
    }


    /**
     * 模板抽奖详情
     */
    @RequestMapping(value = "/getLuckTemplate")
    @ResponseBody
    public Map<String,Object> getLuckTemplate(@RequestParam("actCode") String actCode,
                                              @RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            if(StringUtil.isBlank(businessNo)){
                businessNo = "0";
            }
            ActivityManagement info= activityManagementService.getLuckDrawpConfig(actCode,businessNo);
            msg.put("info", info);
            msg.put("status", true);
        } catch (Exception e){
            e.printStackTrace();
            log.error("模板抽奖查询异常!",e);
            msg.put("status", false);
            msg.put("msg", "模板抽奖查询异常!");
        }
        return msg;
    }

    /**
     * 模板抽奖基础配置保存
     */
    @RequestMapping(value = "/saveLuckTemplate")
    @ResponseBody
    @SystemLog(description = "抽奖基础配置保存",operCode="activityManagement.saveLuckTemplate")
    public Map<String,Object> saveLuckTemplate(@RequestParam("info") String param,@RequestParam("actRule") String actRule){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ActivityManagement info = JSON.parseObject(param, ActivityManagement.class);
            info.setActRule(actRule);
            info.setBusinessNo("0");
           int num= activityManagementService.saveLuckTemplate(info);
           if(num>0){
               msg.put("msg", "保存成功!");
               msg.put("status", true);
           }else{
               msg.put("msg", "保存失败!");
               msg.put("status", false);
           }
        } catch (Exception e){
            e.printStackTrace();
            log.error("模板抽奖基础配置保存异常!",e);
            msg.put("status", false);
            msg.put("msg", "模板抽奖基础配置保存异常!");
        }
        return msg;
    }

    /**
     * 模板抽奖奖品新增
     */
    @RequestMapping(value = "/addLuckPrizeTemplate")
    @ResponseBody
    @SystemLog(description = "抽奖-奖项新增",operCode="activityManagement.addLuckPrizeTemplate")
    public Map<String,Object> addLuckPrizeTemplate(@RequestParam("info") String param){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            PrizeInfo info = JSON.parseObject(param, PrizeInfo.class);
            ActivityManagement actiInfo= activityManagementService.checkActivity(info.getActCode(),"0");
            if(actiInfo!=null){
                prizeInfoService.addLuckPrizeTemplate(info,msg);
            }else{
                msg.put("status", false);
                msg.put("msg", "奖项新增-数据异常!");
            }
        } catch (Exception e){
            e.printStackTrace();
            log.error("奖项新增异常!",e);
            msg.put("status", false);
            msg.put("msg", "奖项新增异常!");
        }
        return msg;
    }

    /**
     * 获取奖项详情
     */
    @RequestMapping(value = "/getLuckPrize")
    @ResponseBody
    public Map<String,Object> getLuckPrize(@RequestParam("awardCode") String awardCode){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            PrizeInfo info =prizeInfoService.getLuckPrize(awardCode);
            ActivityManagement actiInfo= activityManagementService.checkActivity(info.getActCode(),"0");
            if(actiInfo!=null){
                msg.put("status", true);
                msg.put("info",info);
            }else{
                msg.put("status", false);
                msg.put("msg", "获取奖项详情-数据异常!");
            }
        } catch (Exception e){
            e.printStackTrace();
            log.error("获取奖项详情异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取奖项详情异常!");
        }
        return msg;
    }

    /**
     * 模板抽奖奖品修改
     */
    @RequestMapping(value = "/editLuckPrizeTemplate")
    @ResponseBody
    @SystemLog(description = "抽奖-奖项修改",operCode="activityManagement.editLuckPrizeTemplate")
    public Map<String,Object> editLuckPrizeTemplate(@RequestParam("info") String param){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            PrizeInfo info = JSON.parseObject(param, PrizeInfo.class);
            ActivityManagement actiInfo= activityManagementService.checkActivity(info.getActCode(),"0");
            if(actiInfo!=null){
                prizeInfoService.editLuckPrizeTemplate(info,msg);
            }else{
                msg.put("status", false);
                msg.put("msg", "奖项修改-数据异常!");
            }
        } catch (Exception e){
            e.printStackTrace();
            log.error("奖项修改异常!",e);
            msg.put("status", false);
            msg.put("msg", "奖项修改异常!");
        }
        return msg;
    }

    /**
     * 奖项开关
     */
    @RequestMapping(value = "/closeOpenPrize")
    @ResponseBody
    @SystemLog(description = "抽奖-奖项开启/关闭",operCode="activityManagement.closeOpenPrize")
    public Map<String,Object> closeOpenPrize(@RequestParam("id") Long id,@RequestParam("status") int status){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            PrizeInfo info = prizeInfoService.getLuckPrizeById(id);
            ActivityManagement actiInfo= activityManagementService.checkActivity(info.getActCode(),"0");
            if(actiInfo!=null){
                int num =prizeInfoService.closeOpenPrize(id,status);
                if(num>0){
                    msg.put("status", true);
                    msg.put("msg", "操作成功!");
                }else{
                    msg.put("status", false);
                    msg.put("msg", "操作失败!");
                }
            }else{
                msg.put("status", false);
                msg.put("msg", "抽奖-奖项开启/关闭-数据异常!");
            }
        } catch (Exception e){
            log.error("开启/关闭奖项异常!",e);
            msg.put("status", false);
            msg.put("msg", "开启/关闭奖项异常!");
        }
        return msg;
    }

    /**
     * 删除奖项
     */
    @RequestMapping(value = "/deletePrize")
    @ResponseBody
    @SystemLog(description = "抽奖-奖项删除",operCode="activityManagement.deletePrize")
    public Map<String,Object> deletePrize(@RequestParam("id") Long id){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            PrizeInfo info = prizeInfoService.getLuckPrizeById(id);
            ActivityManagement actiInfo= activityManagementService.checkActivity(info.getActCode(),"0");
            if(actiInfo!=null){
                int num =prizeInfoService.deletePrize(id);
                if(num>0){
                    msg.put("status", true);
                    msg.put("msg", "删除奖项成功!");
                }else{
                    msg.put("status", false);
                    msg.put("msg", "删除奖项失败!");
                }
            }else{
                msg.put("status", false);
                msg.put("msg", "抽奖-删除奖项-数据异常!");
            }
        } catch (Exception e){
            log.error("抽奖-奖项删除异常!",e);
            msg.put("status", false);
            msg.put("msg", "抽奖-奖项删除异常!");
        }
        return msg;
    }

    /**
     * 获取用户激活详情
     */
    @RequestMapping(value = "/getActivityTemp")
    @ResponseBody
    public Map<String,Object> getActivityTemp(@RequestParam("actCode") String actCode,
                                              @RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ActivityManagement info= activityManagementService.getActivityTemp(actCode,"0");
            msg.put("info", info);
            msg.put("status", true);
        } catch (Exception e){
            e.printStackTrace();
            log.error("获取用户激活详情异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取用户激活详情异常!");
        }
        return msg;
    }
    /**
     * 用户激活配置保存
     */
    @RequestMapping(value = "/saveActivityTemp")
    @ResponseBody
    @SystemLog(description = "用户激活配置保存",operCode="activityManagement.saveActivityTemp")
    public Map<String,Object> saveActivityTemp(@RequestParam("info") String param,
                                               @RequestParam("awardDesc") String awardDesc,
                                               @RequestParam("actRule") String actRule){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ActivityManagement info = JSON.parseObject(param, ActivityManagement.class);
            info.setActRule(actRule);
            info.setAwardDesc(awardDesc);
            info.setBusinessNo("0");
            int num= activityManagementService.saveActivityTemp(info);
            if(num>0){
                msg.put("msg", "保存成功!");
                msg.put("status", true);
            }else{
                msg.put("msg", "保存失败!");
                msg.put("status", false);
            }
        } catch (Exception e){
            e.printStackTrace();
            log.error("用户激活配置保存异常!",e);
            msg.put("status", false);
            msg.put("msg", "用户激活配置保存异常!");
        }
        return msg;
    }


    /**
     * 用户激活新增内容
     */
    @RequestMapping(value = "/addActiInfo")
    @ResponseBody
    @SystemLog(description = "活动内容新增",operCode="activityManagement.addActiInfo")
    public Map<String,Object> addActiInfo(@RequestParam("info") String param,@RequestParam("activeDesc") String activeDesc){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ActivityDetail info = JSON.parseObject(param, ActivityDetail.class);
            info.setActiveDesc(activeDesc);
            ActivityManagement actiInfo= activityManagementService.checkActivity(info.getActCode(),"0");
            if(actiInfo!=null){
                activityDetailService.addActiInfo(info,msg);
            }else{
                msg.put("status", false);
                msg.put("msg", "活动内容新增-数据异常!");
            }
        } catch (Exception e){
            e.printStackTrace();
            log.error("活动内容新增异常!",e);
            msg.put("status", false);
            msg.put("msg", "活动内容新增异常!");
        }
        return msg;
    }

    /**
     * 获取用户激活详情
     */
    @RequestMapping(value = "/getActiDetail")
    @ResponseBody
    public Map<String,Object> getActiDetail(@RequestParam("actDetCode") String actDetCode){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ActivityDetail info= activityDetailService.getActiDetail(actDetCode);
            ActivityManagement actiInfo= activityManagementService.checkActivity(info.getActCode(),"0");
            if(actiInfo!=null){
                msg.put("info", info);
                msg.put("status", true);
            }else{
                msg.put("status", false);
                msg.put("msg", "获取用户激活详情-数据异常!");
            }
        } catch (Exception e){
            e.printStackTrace();
            log.error("获取用户激活详情异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取用户激活详情异常!");
        }
        return msg;
    }

    /**
     * 用户激活内容修改
     */
    @RequestMapping(value = "/editActiInfo")
    @ResponseBody
    @SystemLog(description = "活动内容修改",operCode="activityManagement.editActiInfo")
    public Map<String,Object> editActiInfo(@RequestParam("info") String param,@RequestParam("activeDesc") String activeDesc){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ActivityDetail info = JSON.parseObject(param, ActivityDetail.class);
            info.setActiveDesc(activeDesc);
            ActivityManagement actiInfo= activityManagementService.checkActivity(info.getActCode(),"0");
            if(actiInfo!=null){
                activityDetailService.editActiInfo(info,msg);
            }else{
                msg.put("status", false);
                msg.put("msg", "用户激活内容修改-数据异常!");
            }
        } catch (Exception e){
            e.printStackTrace();
            log.error("活动内容修改异常!",e);
            msg.put("status", false);
            msg.put("msg", "活动内容修改异常!");
        }
        return msg;
    }

    /**
     * 活动内容删除
     */
    @RequestMapping(value = "/deleteActiInfo")
    @ResponseBody
    @SystemLog(description = "活动内容删除",operCode="activityManagement.deleteActiInfo")
    public Map<String,Object> deletePrize(@RequestParam("actDetCode") String actDetCode){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ActivityDetail info=activityDetailService.checkActiInfo(actDetCode);
            ActivityManagement actiInfo= activityManagementService.checkActivity(info.getActCode(),"0");
            if(actiInfo!=null){
                int num =activityDetailService.deleteActiInfo(actDetCode);
                if(num>0){
                    msg.put("status", true);
                    msg.put("msg", "活动内容删除成功!");
                }else{
                    msg.put("status", false);
                    msg.put("msg", "活动内容删除失败!");
                }
            }else{
                msg.put("status", false);
                msg.put("msg", "活动内容删除-数据异常!");
            }
        } catch (Exception e){
            log.error("活动内容删除异常!",e);
            msg.put("status", false);
            msg.put("msg", "活动内容删除异常!");
        }
        return msg;
    }


    /**
     * 会员现金礼遇季活动属性保存
     */
    @RequestMapping(value = "/saveCashMeetConfig")
    @ResponseBody
    @SystemLog(description = "会员现金礼遇季活动属性保存",operCode="activityManagement.saveCashMeetConfig")
    public Map<String,Object> saveCashMeetConfig(@RequestParam("info") String param){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ActivityManagement info = JSON.parseObject(param, ActivityManagement.class);
//            info.setBusinessNo("0");
            int num= activityManagementService.saveLuckTemplate(info);
            if(num>0){
                msg.put("msg", "保存成功!");
                msg.put("status", true);
            }else{
                msg.put("msg", "保存失败!");
                msg.put("status", false);
            }
        } catch (Exception e){
            e.printStackTrace();
            log.error("模板抽奖基础配置保存异常!",e);
            msg.put("status", false);
            msg.put("msg", "模板抽奖基础配置保存异常!");
        }
        return msg;
    }

    /**
     * 获取后置条件
     */
    @RequestMapping(value = "/getAfterCondition")
    @ResponseBody
    public Map<String,Object> getAfterCondition(@RequestParam("actCode") String actCode,@RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            final List<ActivityDetail> activityDetail = activityDetailService.getActivityDetailByActCodeAndActiveType(actCode, Arrays.asList(10),businessNo);
            if(activityDetail!=null){
                msg.put("info", activityDetail);
                msg.put("status", true);
            }else{
                msg.put("status", false);
                msg.put("msg", "获取后置条件异常-数据异常!");
            }
        } catch (Exception e){
            e.printStackTrace();
            log.error("获取后置条件异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取后置条件异常!");
        }
        return msg;
    }

    /**
     * 修改后置条件
     */
    @RequestMapping(value = "/updateAfterCondition")
    @ResponseBody
    @SystemLog(description = "修改活动后置条件",operCode="activityManagement.updateAfterCondition")
    public Map<String,Object> updateAfterCondition(@RequestParam("info") String info,@RequestParam("policyList") String policyList){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ActivityDetail detail = JSON.parseObject(info, ActivityDetail.class);
            if(detail.getPolicyType() == 1){
                final List<PolicyDetail> policyDetails = JSON.parseObject(policyList, new TypeReference<List<PolicyDetail>>(){});
                detail.setPolicyList(policyDetails);
            }
            int num= activityDetailService.updateAfterCondition(detail,msg);
            if(num > 0){
                msg.put("msg", "修改成功!");
                msg.put("status", true);
            }else{
                msg.put("status", false);
                msg.put("msg", "修改后置条件失败!");
            }
        } catch (Exception e){
            e.printStackTrace();
            log.error("修改后置条件异常!",e);
            msg.put("status", false);
            msg.put("msg", "修改后置条件异常!");
        }
        return msg;
    }


    /**
     * 获取后置条件详情
     */
    @RequestMapping(value = "/getAfterConditionDetail")
    @ResponseBody
    public Map<String,Object> getAfterConditionDetail(@RequestParam("actCode") String actCode,@RequestParam("actDetCode") String actDetCode){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            final ActivityDetail activityDetail = activityDetailService.getActivityDetailByActCodeAndActDetCode(actCode, actDetCode);
            if(activityDetail!=null){
                msg.put("info", activityDetail);
                msg.put("status", true);
            }else{
                msg.put("status", false);
                msg.put("msg", "获取后置条件详情异常-数据异常!");
            }
        } catch (Exception e){
            e.printStackTrace();
            log.error("获取后置条件详情异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取后置条件详情异常!");
        }
        return msg;
    }

    /**
     * 获取活动内容
     */
    @RequestMapping(value = "/getActivityContent")
    @ResponseBody
    public Map<String,Object> getActivityContent(@RequestParam("actCode") String actCode,@RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            final List<ActivityDetail> activityDetail = activityDetailService.getActivityDetailByActCodeAndActiveType(actCode, Arrays.asList(5,6,7,8,9),businessNo);
            if(activityDetail!=null){
                msg.put("info", activityDetail);
                msg.put("status", true);
            }else{
                msg.put("status", false);
                msg.put("msg", "获取活动内容异常-数据异常!");
            }
        } catch (Exception e){
            e.printStackTrace();
            log.error("获取活动内容异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取活动内容异常!");
        }
        return msg;
    }


    /**
     * 获取活动内容详情
     */
    @RequestMapping(value = "/getActivityContentDetail")
    @ResponseBody
    public Map<String,Object> getActivityContentDetail(@RequestParam("actCode") String actCode,@RequestParam("actDetCode") String actDetCode){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            final ActivityDetail activityDetail = activityDetailService.getActivityDetailByActCodeAndActDetCode(actCode, actDetCode);
            if(activityDetail!=null){
                msg.put("info", activityDetail);
                msg.put("status", true);
            }else{
                msg.put("status", false);
                msg.put("msg", "获取活动内容详情异常-数据异常!");
            }
        } catch (Exception e){
            e.printStackTrace();
            log.error("获取活动内容详情异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取活动内容详情异常!");
        }
        return msg;
    }

    /**
     * 修改活动内容
     */
    @RequestMapping(value = "/updateActivityContent")
    @ResponseBody
    @SystemLog(description = "修改活动内容",operCode="activityManagement.updateActivityContent")
    public Map<String,Object> updateActivityContent(@RequestParam("info") String info,@RequestParam("policyList") String policyList){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ActivityDetail detail = JSON.parseObject(info, ActivityDetail.class);
            if(detail.getPolicyType() == 1){
                final List<PolicyDetail> policyDetails = JSON.parseObject(policyList, new TypeReference<List<PolicyDetail>>(){});
                detail.setPolicyList(policyDetails);
            }
            log.info("修改活动内容开始....");
            int num= activityDetailService.updateActivityContent(detail,msg);
            if(num > 0){
                msg.put("msg", "修改成功!");
                msg.put("status", true);
            }else{
                msg.put("status", false);
                msg.put("msg", "修改活动内容失败!");
            }
        } catch (Exception e){
            e.printStackTrace();
            log.error("修改活动内容异常!",e);
            msg.put("status", false);
            msg.put("msg", "修改活动内容异常!");
        }
        return msg;
    }

    /**
     * 活动内容开关
     */
    @RequestMapping(value = "/activitySwitch")
    @ResponseBody
    @SystemLog(description = "修改活动内容开关",operCode="activityManagement.activitySwitch")
    public Map<String,Object> activitySwitch(@RequestParam("actDetCode") String actDetCode,@RequestParam("actCode") String actCode,@RequestParam("status") Integer status){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num= activityDetailService.updateActivityDetailOpenStatus(actCode,actDetCode,status);
            if(num > 0){
                msg.put("msg", "操作成功!");
                msg.put("status", true);
            }else{
                msg.put("status", false);
                msg.put("msg", "操作失败!");
            }
        } catch (Exception e){
            e.printStackTrace();
            log.error("操作异常!",e);
            msg.put("status", false);
            msg.put("msg", "操作异常!");
        }
        return msg;
    }


    /**
     * 会员现金礼遇季活动内容保存
     */
    @RequestMapping(value = "/saveCashMeetActivityContent")
    @ResponseBody
    @SystemLog(description = "会员现金礼遇季活动内容保存",operCode="activityManagement.saveCashMeetActivityContent")
    public Map<String,Object> saveCashMeetActivityContent(@RequestParam("actCode") String actCode,@RequestParam("info") String param,@RequestParam("policyList") String policyList){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ActivityDetail info = JSON.parseObject(param, ActivityDetail.class);
            if(info.getPolicyType() == 1){
                final List<PolicyDetail> policyDetails = JSON.parseObject(policyList, new TypeReference<List<PolicyDetail>>(){});
                info.setPolicyList(policyDetails);
            }
            info.setActCode(actCode);
            final int num = activityDetailService.saveActivityContent(info);
            if(num>0){
                msg.put("msg", "保存成功!");
                msg.put("status", true);
            }else{
                msg.put("msg", "保存失败!");
                msg.put("status", false);
            }
        } catch (Exception e){
            e.printStackTrace();
            log.error("活动内容保存异常!",e);
            msg.put("status", false);
            msg.put("msg", "活动内容保存异常!");
        }
        return msg;
    }

    /**
     * 黑名单查询
     */
    @RequestMapping(value = "/getActivityBlackList")
    @ResponseBody
    public Map<String,Object> getActivityBlackList(@ModelAttribute("page") Page<LeaguerBlacklist> page,
                                                @RequestParam("info") String param) {
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            LeaguerBlacklist info = JSON.parseObject(param, LeaguerBlacklist.class);
            leaguerBlacklistService.getActivityBlackList(info.getServiceNo(), info.getLeaguerNo(), page);
            msg.put("page", page);
            msg.put("status", true);
        } catch (Exception e){
            e.printStackTrace();
            log.error("业务流水查询异常!",e);
            msg.put("status", false);
            msg.put("msg", "业务流水查询异常!");
        }
        return msg;
    }

    /**
     * 保存黑名单
     */
    @RequestMapping(value = "/saveActivityBlack")
    @ResponseBody
    @SystemLog(description = "活动添加黑名单",operCode="activityManagement.saveActivityBlack")
    public Map<String,Object> saveActivityBlack(@RequestParam("info") String param){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            LeaguerBlacklist info = JSON.parseObject(param, LeaguerBlacklist.class);
            final String actDetCode = info.getServiceNo();
            final String leaguerNo = info.getLeaguerNo();
            if(StringUtils.isBlank(actDetCode) || StringUtils.isBlank(leaguerNo)){
                msg.put("msg", "用户信息错误，保存失败!");
                msg.put("status", false);
                return msg;
            }
            final LeaguerBlacklist listByActDetCode = leaguerBlacklistService.findByActDetCode(info.getServiceNo(),info.getLeaguerNo());
            if(listByActDetCode != null){
                msg.put("msg", "该用户已经在黑名单中!");
                msg.put("status", false);
                return msg;
            }
            final int num = leaguerBlacklistService.insertActivityBlack(info);
            if(num>0){
                msg.put("msg", "保存成功!");
                msg.put("status", true);
            }else{
                msg.put("msg", "保存失败!");
                msg.put("status", false);
            }
        } catch (Exception e){
            e.printStackTrace();
            log.error("活动黑名单保存异常!",e);
            msg.put("status", false);
            msg.put("msg", "活动黑名单保存异常!");
        }
        return msg;
    }

    /**
     * 删除黑名单
     */
    @RequestMapping(value = "/deleteActivityBlack")
    @ResponseBody
    @SystemLog(description = "活动删除黑名单",operCode="activityManagement.deleteActivityBlack")
    public Map<String,Object> deleteActivityBlack(@RequestParam("serviceNo") String serviceNo,@RequestParam("leaguerNo") String leaguerNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            final int num = leaguerBlacklistService.deleteActivityBlack(serviceNo,leaguerNo);
            if(num>0){
                msg.put("msg", "删除成功!");
                msg.put("status", true);
            }else{
                msg.put("msg", "删除失败!");
                msg.put("status", false);
            }
        } catch (Exception e){
            e.printStackTrace();
            log.error("活动黑名单删除异常!",e);
            msg.put("status", false);
            msg.put("msg", "活动黑名单删除异常!");
        }
        return msg;
    }

    /**
     * 红包记录查询
     */
    @RequestMapping(value = "/getLeagerBagSerialList")
    @ResponseBody
    public Map<String,Object> getLeagerBagSerialList(@ModelAttribute("page") Page<LeagerBagSerial> page,
                                                   @RequestParam("info") String param) {
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            LeagerBagSerial info = JSON.parseObject(param, LeagerBagSerial.class);
            leagerBagSerialService.findListByPage(info, page);
            msg.put("page", page);
            msg.put("status", true);
        } catch (Exception e){
            e.printStackTrace();
            log.error("业务流水查询异常!",e);
            msg.put("status", false);
            msg.put("msg", "业务流水查询异常!");
        }
        return msg;
    }

    /**
     * 导出业务流水列表
     */
    @RequestMapping(value="/importLeagerBagSerial")
    @ResponseBody
    @SystemLog(description = "导出业务流水列表",operCode="businessFlowAction.importDetail")
    public Map<String, Object> importLeagerBagSerial(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        Map<String, Object> msg=new HashMap<String,Object>();
        try {
            LeagerBagSerial info = JSON.parseObject(param, LeagerBagSerial.class);
            leagerBagSerialService.importLeagerBagSerial(info,response);
        }catch (Exception e){
            log.error("导出业务流水列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "导出业务流水列表异常!");
        }
        return msg;
    }


    /**
     * 删除活动内容
     */
    @RequestMapping(value = "/deleteChildActivity")
    @ResponseBody
    @SystemLog(description = "删除子活动内容",operCode="activityManagement.deleteChildActivity")
    public Map<String,Object> deleteChildActivity(@RequestParam("actDetCode") String actDetCode){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num =activityDetailService.deleteActiInfoByActDetCode(actDetCode);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "删除子活动内容成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "删除子活动内容失败!");
            }
        } catch (Exception e){
            log.error("删除子活动内容异常!",e);
            msg.put("status", false);
            msg.put("msg", "删除子活动内容异常!");
        }
        return msg;
    }


    /**
     * 获取已添加活动列表
     */
    @RequestMapping(value = "/getBusinessChildActivity")
    @ResponseBody
    public Map<String,Object> getBusinessChildActivity(@RequestParam("actCode") String actCode){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            final List<ActivityDetail> activityDetailList = activityDetailService.getActivityDetailList(actCode);
            msg.put("status", true);
            msg.put("info",activityDetailList);
        } catch (Exception e){
            log.error("获取已添加活动异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取已添加活动异常!");
        }
        return msg;
    }

}
