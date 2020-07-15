package cn.eeepay.boss.action.integral;

import cn.eeepay.boss.system.DataAuthority;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.activity.*;
import cn.eeepay.framework.service.activity.ActivityDetailService;
import cn.eeepay.framework.service.activity.ActivityManagementService;
import cn.eeepay.framework.service.activity.PrizeInfoService;
import cn.eeepay.framework.service.integral.ActivityChoiceService;
import cn.eeepay.framework.util.ResponseUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 活动选择，针对业务组下的活动处理
 */
@Controller
@RequestMapping(value = "/activityChoice")
public class ActivityChoiceAction {

    private static final Logger log = LoggerFactory.getLogger(ActivityChoiceAction.class);

    @Resource
    private ActivityManagementService activityManagementService;
    @Resource
    private ActivityChoiceService activityChoiceService;
    @Resource
    private PrizeInfoService prizeInfoService;
    @Resource
    private ActivityDetailService activityDetailService;

    /**
     * 业务组下-活动查询
     */
    @RequestMapping(value = "/getActivityList")
    @ResponseBody
    public Map<String,Object> getActivityList(@RequestParam("info") String param){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ActivityManagement info = JSON.parseObject(param, ActivityManagement.class);
            List<ActivityManagement> list= activityManagementService.getActivityListBusNo(info);
            msg.put("list", list);
            msg.put("status", true);
        } catch (Exception e){
            e.printStackTrace();
            log.error("业务组-活动选择查询异常!",e);
            msg.put("status", false);
            msg.put("msg", "业务组-活动选择查询异常!");
        }
        return msg;
    }

    /**
     * 添加活动选择
     */
    @RequestMapping(value = "/addActivityList")
    @ResponseBody
    @DataAuthority
    @SystemLog(description = "添加活动选择",operCode="activityChoice.addActivityList")
    public Map<String,Object> addActivityList(@RequestParam("ids") String ids,
                                              @RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            activityChoiceService.addActivityList(businessNo,ids,msg);
        } catch (Exception e){
            log.error("添加活动选择异常!",e);
            msg.put("status", false);
            msg.put("msg", "添加活动选择异常!");
        }
        return msg;
    }

    /**
     * 业务组-活动删除
     */
    @RequestMapping(value = "/deleteActivityList")
    @ResponseBody
    @DataAuthority
    @SystemLog(description = "业务组-活动删除",operCode="activityChoice.deleteActivityList")
    public Map<String,Object> deleteActivityList(@RequestParam("actCode") String actCode,
                                              @RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num=activityChoiceService.deleteActivityList(businessNo,actCode,msg);
            if(num>0){
                msg.put("msg", "活动删除成功!");
                msg.put("status", true);
            }else{
                msg.put("msg", "活动删除失败!");
                msg.put("status", false);
            }
        } catch (Exception e){
            log.error("业务组-活动删除异常!",e);
            msg.put("status", false);
            msg.put("msg", "业务组-活动删除异常!");
        }
        return msg;
    }
    /**
     * 业务组-抽奖详情
     */
    @RequestMapping(value = "/getLuckDrawpConfig")
    @ResponseBody
    public Map<String,Object> getLuckDrawpConfig(@RequestParam("actCode") String actCode,
                                              @RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ActivityManagement info= activityManagementService.getLuckDrawpConfig(actCode,businessNo);
            msg.put("info", info);
            msg.put("status", true);
        } catch (Exception e){
            e.printStackTrace();
            log.error("业务组-抽奖查询异常!",e);
            msg.put("status", false);
            msg.put("msg", "业务组-抽奖查询异常!");
        }
        return msg;
    }

    /**
     * 业务组-抽奖基础配置保存
     */
    @RequestMapping(value = "/saveLuckConfig")
    @ResponseBody
    @DataAuthority
    @SystemLog(description = "业务组-抽奖基础配置保存",operCode="activityChoice.saveLuckConfig")
    public Map<String,Object> saveLuckTemplate(@RequestParam("info") String param,
                                               @RequestParam("actRule") String actRule,
                                               @RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ActivityManagement info = JSON.parseObject(param, ActivityManagement.class);
            info.setActRule(actRule);
            info.setBusinessNo(businessNo);
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
            log.error("业务组-抽奖基础配置保存异常!",e);
            msg.put("status", false);
            msg.put("msg", "业务组-抽奖基础配置保存异常!");
        }
        return msg;
    }

    /**
     * 业务组-抽奖奖品新增
     */
    @RequestMapping(value = "/addLuckPrize")
    @ResponseBody
    @DataAuthority
    @SystemLog(description = "抽奖-奖项新增",operCode="activityChoice.addLuckPrize")
    public Map<String,Object> addLuckPrizeTemplate(@RequestParam("info") String param, @RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            PrizeInfo info = JSON.parseObject(param, PrizeInfo.class);
            ActivityManagement actiInfo= activityManagementService.checkActivity(info.getActCode(),businessNo);
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
    public Map<String,Object> getLuckPrize(@RequestParam("awardCode") String awardCode,@RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            PrizeInfo info =prizeInfoService.getLuckPrize(awardCode);
            ActivityManagement actiInfo= activityManagementService.checkActivity(info.getActCode(),businessNo);
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
     * 抽奖奖品修改
     */
    @RequestMapping(value = "/editLuckPrize")
    @ResponseBody
    @DataAuthority
    @SystemLog(description = "抽奖-奖项修改",operCode="activityChoice.editLuckPrize")
    public Map<String,Object> editLuckPrize(@RequestParam("info") String param,@RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            PrizeInfo info = JSON.parseObject(param, PrizeInfo.class);
            ActivityManagement actiInfo= activityManagementService.checkActivity(info.getActCode(),businessNo);
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
    @DataAuthority
    @SystemLog(description = "抽奖-奖项开启/关闭",operCode="activityChoice.closeOpenPrize")
    public Map<String,Object> closeOpenPrize(@RequestParam("id") Long id,
                                             @RequestParam("status") int status,
                                             @RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            PrizeInfo info = prizeInfoService.getLuckPrizeById(id);
            ActivityManagement actiInfo= activityManagementService.checkActivity(info.getActCode(),businessNo);
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
    @DataAuthority
    @SystemLog(description = "抽奖-奖项删除",operCode="activityChoice.deletePrize")
    public Map<String,Object> deletePrize(@RequestParam("id") Long id,@RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            PrizeInfo info = prizeInfoService.getLuckPrizeById(id);
            ActivityManagement actiInfo= activityManagementService.checkActivity(info.getActCode(),businessNo);
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
    @RequestMapping(value = "/getActivity")
    @ResponseBody
    public Map<String,Object> getActivity(@RequestParam("actCode") String actCode,
                                              @RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ActivityManagement info= activityManagementService.getActivityTemp(actCode,businessNo);
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
    @RequestMapping(value = "/saveActivity")
    @ResponseBody
    @DataAuthority
    @SystemLog(description = "用户激活配置保存",operCode="activityChoice.saveActivity")
    public Map<String,Object> saveActivity(@RequestParam("info") String param,
                                               @RequestParam("awardDesc") String awardDesc,
                                               @RequestParam("actRule") String actRule,
                                               @RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ActivityManagement info = JSON.parseObject(param, ActivityManagement.class);
            info.setActRule(actRule);
            info.setAwardDesc(awardDesc);
            info.setBusinessNo(businessNo);
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
    @DataAuthority
    @SystemLog(description = "活动内容新增",operCode="activityChoice.addActiInfo")
    public Map<String,Object> addActiInfo(@RequestParam("info") String param,@RequestParam("businessNo") String businessNo,@RequestParam("activeDesc") String activeDesc){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ActivityDetail info = JSON.parseObject(param, ActivityDetail.class);
            info.setActiveDesc(activeDesc);
            ActivityManagement actiInfo= activityManagementService.checkActivity(info.getActCode(),businessNo);
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
    public Map<String,Object> getActiDetail(@RequestParam("actDetCode") String actDetCode,@RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ActivityDetail info= activityDetailService.getActiDetail(actDetCode);
            ActivityManagement actiInfo= activityManagementService.checkActivity(info.getActCode(),businessNo);
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
    @DataAuthority
    @SystemLog(description = "活动内容修改",operCode="activityChoice.editActiInfo")
    public Map<String,Object> editActiInfo(@RequestParam("info") String param,@RequestParam("businessNo") String businessNo,@RequestParam("activeDesc") String activeDesc){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ActivityDetail info = JSON.parseObject(param, ActivityDetail.class);
            info.setActiveDesc(activeDesc);
            ActivityManagement actiInfo= activityManagementService.checkActivity(info.getActCode(),businessNo);
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
    @DataAuthority
    @SystemLog(description = "活动内容删除",operCode="activityChoice.deleteActiInfo")
    public Map<String,Object> deletePrize(@RequestParam("actDetCode") String actDetCode,@RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ActivityDetail info=activityDetailService.checkActiInfo(actDetCode);
            ActivityManagement actiInfo= activityManagementService.checkActivity(info.getActCode(),businessNo);
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
     * 业务组-活动开关
     */
    @RequestMapping(value = "/activitySwitch")
    @ResponseBody
    @DataAuthority
    @SystemLog(description = "业务组-活动开关",operCode="activityChoice.activitySwitch")
    public Map<String,Object> activitySwitch(@RequestParam("id") Long id,
                                             @RequestParam("status") int status,
                                             @RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num =activityManagementService.activitySwitch(id,status,businessNo);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "操作成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "操作失败!");
            }
        } catch (Exception e){
            log.error("业务组-活动开关异常!",e);
            msg.put("status", false);
            msg.put("msg", "业务组-活动开关异常!");
        }
        return msg;
    }

    /**
     * 业务组-首页开关
     */
    @RequestMapping(value = "/homePageSwitch")
    @ResponseBody
    @DataAuthority
    @SystemLog(description = "业务组-首页开关",operCode="activityChoice.homePageSwitch")
    public Map<String,Object> homePageSwitch(@RequestParam("id") Long id,
                                                @RequestParam("status") int status,
                                                @RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num =activityManagementService.homePageSwitch(id,status,businessNo);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "操作成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "操作失败!");
            }
        } catch (Exception e){
            log.error("业务组-首页开关异常!",e);
            msg.put("status", false);
            msg.put("msg", "业务组-首页开关异常!");
        }
        return msg;
    }

    /**
     * 奖项黑名单查询
     */
    @RequestMapping(value = "/getAwardBlackList")
    @ResponseBody
    @DataAuthority
    public Map<String,Object> getAwardBlackList(@RequestParam("info") String param,
                                                @ModelAttribute("page") Page<AwardBlack> page,
                                                @RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            AwardBlack info = JSON.parseObject(param, AwardBlack.class);
            PrizeInfo priInfo = prizeInfoService.getLuckPrizeByCode(info.getAwardCode());
            ActivityManagement actiInfo= activityManagementService.checkActivity(priInfo.getActCode(),businessNo);
            if(actiInfo!=null){
                prizeInfoService.getAwardBlackList(info,page);
                msg.put("page", page);
                msg.put("status", true);
            }else{
                msg.put("status", false);
                msg.put("msg", "查询失败-数据异常!");
            }
        } catch (Exception e){
            e.printStackTrace();
            log.error("奖项黑名单查询异常!",e);
            msg.put("status", false);
            msg.put("msg", "奖项黑名单查询异常!");
        }
        return msg;
    }
    /**
     * 新增奖项黑名单
     */
    @RequestMapping(value = "/addAwardBlack")
    @ResponseBody
    @DataAuthority
    @SystemLog(description = "新增奖项黑名单",operCode="activityChoice.addAwardBlack")
    public Map<String,Object> addAwardBlack(@RequestParam("info") String param,
                                                @RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            AwardBlack info = JSON.parseObject(param, AwardBlack.class);
            PrizeInfo priInfo = prizeInfoService.getLuckPrizeByCode(info.getAwardCode());
            ActivityManagement actiInfo= activityManagementService.checkActivity(priInfo.getActCode(),businessNo);
            if(actiInfo!=null){
                if(StringUtils.isNotBlank(info.getLeaguerNo())){
                    int num= prizeInfoService.addAwardBlack(info);
                    if(num>0){
                        msg.put("status", true);
                        msg.put("msg", "新增成功!");
                    }else{
                        msg.put("status", false);
                        msg.put("msg", "新增失败!");
                    }
                }else{
                    msg.put("status", false);
                    msg.put("msg", "新增失败-不存在该会员!");
                }
            }else{
                msg.put("status", false);
                msg.put("msg", "新增失败-数据异常!");
            }

        } catch (Exception e){
            e.printStackTrace();
            log.error("新增奖项黑名单异常!",e);
            msg.put("status", false);
            msg.put("msg", "新增奖项黑名单异常!");
        }
        return msg;
    }
    /**
     * 删除奖项黑名单
     */
    @RequestMapping(value = "/deleteAwardBlack")
    @ResponseBody
    @DataAuthority
    @SystemLog(description = "删除奖项黑名单",operCode="activityChoice.deleteAwardBlack")
    public Map<String,Object> deleteAwardBlack(@RequestParam("info") String param,
                                            @RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            AwardBlack info = JSON.parseObject(param, AwardBlack.class);
            PrizeInfo priInfo = prizeInfoService.getLuckPrizeByCode(info.getAwardCode());
            ActivityManagement actiInfo= activityManagementService.checkActivity(priInfo.getActCode(),businessNo);
            if(actiInfo!=null){
                int num= prizeInfoService.deleteAwardBlack(info);
                if(num>0){
                    msg.put("status", true);
                    msg.put("msg", "删除成功!");
                }else{
                    msg.put("status", false);
                    msg.put("msg", "删除失败!");
                }
            }else{
                msg.put("status", false);
                msg.put("msg", "删除失败-数据异常!");
            }

        } catch (Exception e){
            e.printStackTrace();
            log.error("删除奖项黑名单异常!",e);
            msg.put("status", false);
            msg.put("msg", "删除奖项黑名单异常!");
        }
        return msg;
    }


    /**
     * 获取中奖记录显示配置
     */
    @RequestMapping(value = "/getPrizeDisplay")
    @ResponseBody
    public Map<String,Object> getPrizeDisplay(@RequestParam("actCode") String actCode,@RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ActivityManagement actiInfo= activityManagementService.checkActivity(actCode,businessNo);
            if(actiInfo!=null){
                ShowConfig info=activityChoiceService.getShowConfig(actCode);
                msg.put("status", true);
                msg.put("info",info);
            }else{
                msg.put("status", false);
                msg.put("msg", "获取中奖记录显示配置-数据异常!");
            }
        } catch (Exception e){
            log.error("获取中奖记录显示配置异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取中奖记录显示配置异常!");
        }
        return msg;
    }
    /**
     * 保存中奖记录显示配置
     */
    @RequestMapping(value = "/savePrizeDisplay")
    @ResponseBody
    @DataAuthority
    @SystemLog(description = "抽奖-保存中奖记录显示",operCode="activityChoice.savePrizeDisplay")
    public Map<String,Object> savePrizeDisplay(@RequestParam("info") String param,@RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ShowConfig info= JSON.parseObject(param, ShowConfig.class);
            ActivityManagement actiInfo= activityManagementService.checkActivity(info.getActCode(),businessNo);
            if(actiInfo!=null){
                int num=activityChoiceService.saveShowConfig(info);
                if(num>0){
                    msg.put("status", true);
                    msg.put("msg", "保存成功!");
                }else{
                    msg.put("status", false);
                    msg.put("msg", "保存失败!");
                }
            }else{
                msg.put("status", false);
                msg.put("msg", "保存中奖记录显示配置-数据异常!");
            }
        } catch (Exception e){
            log.error("保存中奖记录显示配置异常!",e);
            msg.put("status", false);
            msg.put("msg", "保存中奖记录显示配置异常!");
        }
        return msg;
    }



    /**
     * 适用范围修改
     */
    @RequestMapping(value = "/actRangeTypeEdit")
    @ResponseBody
    @DataAuthority
    @SystemLog(description = "适用范围修改",operCode="activityChoice.actRangeTypeEdit")
    public Map<String,Object> actRangeTypeEdit( @RequestParam("actCode") String actCode,
                                                @RequestParam("businessNo") String businessNo,
                                                @RequestParam("actRangeType") String actRangeType){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ActivityManagement info =activityManagementService.checkActivity(actCode,businessNo);
            if(info!=null){
                int num=activityManagementService.importMemberAll(actCode,actRangeType);
                if(num>0){
                    msg.put("status", true);
                    msg.put("msg", "适用范围修改成功");
                    return msg;
                }
            }else{
                msg.put("status", false);
                msg.put("msg", "适用范围修改失败-数据异常!");
            }
        } catch (Exception e){
            e.printStackTrace();
            log.error("适用范围修改失败异常!",e);
            msg.put("status", false);
            msg.put("msg", "适用范围修改失败异常!");
        }
        return msg;
    }

    /**
     * 获取简单的活动详情
     */
    @RequestMapping(value = "/getActCodeInfo")
    @ResponseBody
    @DataAuthority
    public Map<String,Object> getActCodeInfo(@RequestParam("actCode") String actCode,@RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ActivityManagement actiInfo= activityManagementService.checkActivity(actCode,businessNo);
            if(actiInfo!=null){
                msg.put("info", actiInfo);
                msg.put("status", true);
            }else{
                msg.put("status", false);
                msg.put("msg", "获取简单的活动详情-数据异常!");
            }

        } catch (Exception e){
            e.printStackTrace();
            log.error("获取简单的活动详情异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取简单的活动详情异常!");
        }
        return msg;
    }

    /**
     * 范围管理名单查询
     */
    @RequestMapping(value = "/getRangeNameList")
    @ResponseBody
    @DataAuthority
    public Map<String,Object> getRangeNameList(@RequestParam("info") String param,
                                                @ModelAttribute("page") Page<RangeName> page,
                                                @RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            RangeName info = JSON.parseObject(param, RangeName.class);
            ActivityManagement actiInfo= activityManagementService.checkActivity(info.getActCode(),businessNo);
            if(actiInfo!=null){
                activityManagementService.getRangeNameList(info,page);
                msg.put("page", page);
                msg.put("status", true);
            }else{
                msg.put("status", false);
                msg.put("msg", "范围管理名单查询-数据异常!");
            }
        } catch (Exception e){
            e.printStackTrace();
            log.error("范围管理名单查询异常!",e);
            msg.put("status", false);
            msg.put("msg", "范围管理名单查询异常!");
        }
        return msg;
    }

    /**
     * 模板下载
     */
    @RequestMapping("/downloadTemplate")
    public String downloadAdjustAccTemplate(HttpServletRequest request, HttpServletResponse response){
        String filePath = request.getServletContext().getRealPath("/")+ File.separator+"template"+File.separator+"rangeManagementTemplate.xlsx";
        log.info(filePath);
        ResponseUtil.download(response, filePath,"适用范围导入模板.xlsx");
        return null;
    }

    @RequestMapping(value="/importMember")
    @ResponseBody
    @SystemLog(description = "活动范围管理-批量导入",operCode="activityChoice.importMember")
    public  Object importMember(@RequestParam("file") MultipartFile file,
                                @RequestParam("actCode") String actCode,
                                @RequestParam("businessNo") String businessNo,
                                @RequestParam("type") String type){
        Map<String, Object> msg = new HashMap<>();
        try {
            ActivityManagement info =activityManagementService.checkActivity(actCode,businessNo);
            if(info!=null){
                if (!file.isEmpty()) {
                    String format=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                    if(!format.equals(".xls") && !format.equals(".xlsx")){
                        msg.put("status", false);
                        msg.put("msg", "适用范围导入文件格式错误");
                        return msg;
                    }
                }
                msg = activityChoiceService.importMember(file,info,businessNo,type);
            }else{
                msg.put("status", false);
                msg.put("msg", "适用范围导入失败-数据异常!");
            }
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "适用范围导入失败");
            log.error("适用范围导入失败",e);
        }
        return msg;
    }

    /**
     * 新增范围管理名单
     */
    @RequestMapping(value = "/addRangeName")
    @ResponseBody
    @DataAuthority
    @SystemLog(description = "新增范围管理名单",operCode="activityChoice.addRangeName")
    public Map<String,Object> addRangeName(@RequestParam("info") String param,
                                            @RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            RangeName info = JSON.parseObject(param, RangeName.class);
            ActivityManagement actiInfo= activityManagementService.checkActivity(info.getActCode(),businessNo);
            if(actiInfo!=null){
                if(StringUtils.isNotBlank(info.getLeaguerNo())){
                    activityManagementService.addRangeName(info,msg);
                }else{
                    msg.put("status", false);
                    msg.put("msg", "新增失败-不存在该会员!");
                }
            }else{
                msg.put("status", false);
                msg.put("msg", "新增失败-数据异常!");
            }

        } catch (Exception e){
            e.printStackTrace();
            log.error("新增范围管理名单异常!",e);
            msg.put("status", false);
            msg.put("msg", "新增范围管理名单异常!");
        }
        return msg;
    }
    /**
     * 删除范围管理名单
     */
    @RequestMapping(value = "/deleteRangeName")
    @ResponseBody
    @DataAuthority
    @SystemLog(description = "删除范围管理名单",operCode="activityChoice.deleteRangeName")
    public Map<String,Object> deleteRangeName(@RequestParam("info") String param,
                                               @RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            RangeName info = JSON.parseObject(param, RangeName.class);
            ActivityManagement actiInfo= activityManagementService.checkActivity(info.getActCode(),businessNo);
            if(actiInfo!=null){
                int num= activityManagementService.deleteRangeName(info);
                if(num>0){
                    msg.put("status", true);
                    msg.put("msg", "删除成功!");
                }else{
                    msg.put("status", false);
                    msg.put("msg", "删除失败!");
                }
            }else{
                msg.put("status", false);
                msg.put("msg", "删除失败-数据异常!");
            }

        } catch (Exception e){
            e.printStackTrace();
            log.error("删除范围管理名单异常!",e);
            msg.put("status", false);
            msg.put("msg", "删除范围管理名单异常!");
        }
        return msg;
    }

}
