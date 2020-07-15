package cn.eeepay.boss.action.integral;

import cn.eeepay.boss.system.DataAuthority;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.integral.BusinessInfo;
import cn.eeepay.framework.model.integral.BusinessTask;
import cn.eeepay.framework.service.integral.BusinessInfoService;
import com.alibaba.fastjson.JSON;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/7/2/002.
 * @author liuks
 * 业务组Action
 */
@Controller
@RequestMapping(value = "/businessInfoAction")
public class BusinessInfoAction {

    private static final Logger log = LoggerFactory.getLogger(BusinessInfoAction.class);

    @Resource
    private BusinessInfoService businessInfoService;

    /**
     * 获取业务组列表
     */
    @RequestMapping(value = "/getSelectPageList")
    @ResponseBody
    public Map<String,Object> getSelectPageList(@ModelAttribute("page") Page<BusinessInfo> page,
                                            @RequestParam("info") String param){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            BusinessInfo info = JSON.parseObject(param, BusinessInfo.class);
            businessInfoService.getSelectPageList(info,page);
            msg.put("page", page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("获取业务组列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取业务组列表异常!");
        }
        return msg;
    }
    /**
     * 新增业务组
     */
    @RequestMapping(value = "/addBusinessInfo")
    @ResponseBody
    @SystemLog(description = "新增业务组",operCode="businessInfoAction.addBusinessInfo")
    public Map<String,Object> addBusinessInfo(@RequestParam("info") String param){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            BusinessInfo info = JSON.parseObject(param, BusinessInfo.class);
            businessInfoService.addBusinessInfo(info,msg);
        } catch (Exception e){
            log.error("新增业务组异常!",e);
            msg.put("status", false);
            msg.put("msg", "新增业务组异常!");
        }
        return msg;
    }

    /**
     * 获取基本业务组信息
     */

    @RequestMapping(value = "/getBusinessBasic")
    @ResponseBody
    @DataAuthority
    public Map<String,Object> getBusinessBasic(@RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            businessInfoService.getBusinessBasic(businessNo,msg);
        } catch (Exception e){
            log.error("获取基本业务组信息异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取基本业务组信息异常!");
        }
        return msg;
    }
    /**
     * 修改基本资料信息
     */
    @RequestMapping(value = "/updateBusinessBasic")
    @ResponseBody
    @DataAuthority
    @SystemLog(description = "修改基本资料信息",operCode="businessInfoAction.updateBusinessBasic")
    public Map<String,Object> updateBusinessBasic(@RequestParam("info") String param,
                                                  @RequestParam("scoreDesc") String scoreDesc,
                                                  @RequestParam("levelDesc") String levelDesc,
                                                  @RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            BusinessInfo info = JSON.parseObject(param, BusinessInfo.class);
            info.setScoreDesc(scoreDesc);
            info.setLevelDesc(levelDesc);
            info.setBusinessNo(businessNo);
            businessInfoService.updateBusinessBasic(info,msg);
        } catch (Exception e){
            log.error("修改基本资料信息异常!",e);
            msg.put("status", false);
            msg.put("msg", "修改基本资料信息异常!");
        }
        return msg;
    }


    /**
     * 获取业务组-任务选择
     */
    @RequestMapping(value = "/getBusinessTaskList")
    @ResponseBody
    @DataAuthority
    public Map<String,Object> getBusinessTaskList(@RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            businessInfoService.getBusinessTaskList(businessNo,msg);
        } catch (Exception e){
            log.error("获取业务组-任务选择异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取业务组-任务选择异常!");
        }
        return msg;
    }
    /**
     * 添加任务选择
     */
    @RequestMapping(value = "/addBusinessTask")
    @ResponseBody
    @DataAuthority
    @SystemLog(description = "添加任务选择",operCode="businessInfoAction.addBusinessTask")
    public Map<String,Object> addBusinessTask(@RequestParam("ids") String ids,
                                              @RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            businessInfoService.addBusinessTask(businessNo,ids,msg);
        } catch (Exception e){
            log.error("添加任务选择异常!",e);
            msg.put("status", false);
            msg.put("msg", "添加任务选择异常!");
        }
        return msg;
    }

    /**
     * 获取任务
     */
    @RequestMapping(value = "/getBusinessTask")
    @ResponseBody
    @DataAuthority
    public Map<String,Object> getBusinessTask(@RequestParam("id") Long id,
                                              @RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            businessInfoService.getBusinessTask(id,businessNo,msg);
        } catch (Exception e){
            log.error("获取任务异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取任务异常!");
        }
        return msg;
    }

    /**
     * 修改任务
     */
    @RequestMapping(value = "/editBusinessTask")
    @ResponseBody
    @DataAuthority
    @SystemLog(description = "修改任务",operCode="businessInfoAction.editBusinessTask")
    public Map<String,Object> editBusinessTask(@RequestParam("info") String param,
                                               @RequestParam("taskOverview")String taskOverview,
                                               @RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            BusinessTask info = JSON.parseObject(param, BusinessTask.class);
            info.setBusinessNo(businessNo);
            info.setTaskOverview(taskOverview);
            businessInfoService.editBusinessTask(info,msg);
        } catch (Exception e){
            log.error("修改任务异常!",e);
            msg.put("status", false);
            msg.put("msg", "修改任务异常!");
        }
        return msg;
    }

    /**
     * 业务组下开启/关闭任务
     */
    @RequestMapping(value = "/closeBusinessTask")
    @ResponseBody
    @DataAuthority
    @SystemLog(description = "开启/关闭任务",operCode="businessInfoAction.closeBusinessTask")
    public Map<String,Object> closeBusinessTask(@RequestParam("id") Long id,
                                                @RequestParam("businessNo") String businessNo,
                                                @RequestParam("sta") int sta){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            businessInfoService.closeBusinessTask(id,businessNo,sta,msg);
        } catch (Exception e){
            log.error("开启/关闭任务异常!",e);
            msg.put("status", false);
            msg.put("msg", "开启/关闭任务异常!");
        }
        return msg;
    }

    /**
     * 业务组下删除任务
     */
    @RequestMapping(value = "/deleteBusinessTask")
    @ResponseBody
    @DataAuthority
    @SystemLog(description = "删除任务",operCode="businessInfoAction.deleteBusinessTask")
    public Map<String,Object> delBusinessTask(@RequestParam("id") Long id,
                                               @RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            businessInfoService.deleteBusinessTask(id,businessNo,msg);
        } catch (Exception e){
            log.error("删除任务异常!",e);
            msg.put("status", false);
            msg.put("msg", "删除任务异常!");
        }
        return msg;
    }
    /**
     * 获取业务组下拉列表，用于勾选(角色处下拉)
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getBusinessInfoListAll")
    @ResponseBody
    public Object getBusinessInfoListAll() {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        try {
            List<BusinessInfo> list = businessInfoService.getBusinessInfoListAll();
            jsonMap.put("status", true);
            jsonMap.put("list", list);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询业务组异常!", e);
            jsonMap.put("status", false);
        }
        return jsonMap;
    }

    /**
     * 获取业务组下拉列表(会员下拉,需要过滤)
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getBusinessInfoListByUser")
    @ResponseBody
    public Object getBusinessInfoListByUser() {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        try {
            List<BusinessInfo> list = businessInfoService.getBusinessInfoListByUser();
            jsonMap.put("status", true);
            jsonMap.put("list", list);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询业务组异常!", e);
            jsonMap.put("status", false);
        }
        return jsonMap;
    }


    /**
     * 会员等级与权益预览
     */
    @RequestMapping(value = "/businessTaskPreview")
    @ResponseBody
    @DataAuthority
    public Map<String,Object> businessTaskPreview(@RequestParam("businessNo") String businessNo) {
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            List<Map<Object,Object>> list=businessInfoService.businessTaskPreview(businessNo);
            msg.put("list",list);
            msg.put("status", true);
        } catch (Exception e){
            e.printStackTrace();
            log.error("查询会员等级与权益预览异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询会员等级与权益预览异常!");
        }
        return msg;
    }


    /**
     * 获取会员等级与权益预览表头
     */
    @RequestMapping(value = "/getBusinessTaskPreviewTableHeader")
    @ResponseBody
    @DataAuthority
    public Map<String,Object> getBusinessTaskPreviewTableHeader(@Param("businessNo") String businessNo) {
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            msg.put("data",businessInfoService.getAllVipLevels(businessNo));
            msg.put("status", true);
        } catch (Exception e){
            e.printStackTrace();
            log.error("获取会员等级与权益预览表头异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取会员等级与权益预览表头异常!");
        }
        return msg;
    }

    /**
     * 批量保存会员等级与权益预览
     */
    @RequestMapping(value = "/saveBatchBusinessTaskPreview")
    @ResponseBody
    @DataAuthority
    @SystemLog(description = "保存会员等级与权益预览",operCode="businessInfoAction.saveBatchBusinessTaskPreview")
    public Map<String,Object> saveBatchBusinessTaskPreview(@Param("rewardLevel")String rewardLevel,@Param("businessNo") String businessNo) {
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num=businessInfoService.saveBatchBusinessTaskPreview(rewardLevel,businessNo);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "保存成功");
            }else{
                msg.put("status", false);
                msg.put("msg", "保存失败");
            }
        } catch (Exception e){
            e.printStackTrace();
            log.error("保存会员等级与权益预览异常!",e);
            msg.put("status", false);
            msg.put("msg", "保存会员等级与权益预览异常!");
        }
        return msg;
    }

}
