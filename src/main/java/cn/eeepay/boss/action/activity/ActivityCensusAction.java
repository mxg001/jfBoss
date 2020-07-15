package cn.eeepay.boss.action.activity;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.activity.ActivityCensus;
import cn.eeepay.framework.service.activity.ActivityCensusService;
import com.alibaba.fastjson.JSON;
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
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/activityCensus")
public class ActivityCensusAction {

    private static final Logger log = LoggerFactory.getLogger(ActivityCensusAction.class);

    @Resource
    private ActivityCensusService activityCensusService;

    /**
     * 活动数据统计查询
     */
    @RequestMapping(value = "/getSelectPageList")
    @ResponseBody
    public Map<String,Object> getSelectPageList(@ModelAttribute("page") Page<ActivityCensus> page,
                                                   @RequestParam("info") String param){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ActivityCensus info = JSON.parseObject(param, ActivityCensus.class);
            activityCensusService.getSelectPageList(info,page);
            msg.put("page", page);
            msg.put("status", true);
        } catch (Exception e){
            e.printStackTrace();
            log.error("活动数据统计异常!",e);
            msg.put("status", false);
            msg.put("msg", "活动数据统计异常!");
        }
        return msg;
    }

    /**
     * 导出活动数据统计
     */
    @RequestMapping(value="/importDetail")
    @ResponseBody
    @SystemLog(description = "导出活动数据统计",operCode="activityCensus.importDetail")
    public Map<String, Object> importDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        Map<String, Object> msg=new HashMap<String,Object>();
        try {
            ActivityCensus info = JSON.parseObject(param, ActivityCensus.class);
            activityCensusService.importDetail(info,response);
        }catch (Exception e){
            log.error("导出活动数据统计异常!",e);
            msg.put("status", false);
            msg.put("msg", "导出活动数据统计异常!");
        }
        return msg;
    }
}
