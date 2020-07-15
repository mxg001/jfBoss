package cn.eeepay.boss.action.integral;

import cn.eeepay.boss.system.DataAuthority;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.integral.AddScoreInfo;
import cn.eeepay.framework.model.integral.ScoreAccountCount;
import cn.eeepay.framework.model.integral.ScoreAccountInfo;
import cn.eeepay.framework.service.integral.ScoreAccountService;
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

/**
 * Created by Administrator on 2019/7/11/011.
 * @author  liuks 积分流水
 */
@Controller
@RequestMapping(value = "/scoreAccountAction")
public class ScoreAccountAction {

    private static final Logger log = LoggerFactory.getLogger(ScoreAccountAction.class);

    @Resource
    private ScoreAccountService scoreAccountService;

    /**
     * 获取会员的积分流水列表
     */
    @RequestMapping(value = "/getUserAccountList")
    @ResponseBody
    @DataAuthority
    public Map<String,Object> getUserAccountList(@ModelAttribute("page") Page<ScoreAccountInfo> page,
                                                @RequestParam("info") String param,
                                                @RequestParam("businessNo") String businessNo) {
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ScoreAccountInfo info = JSON.parseObject(param, ScoreAccountInfo.class);
            scoreAccountService.getUserAccountList(info,page,businessNo);
            msg.put("page", page);
            msg.put("status", true);
        } catch (Exception e){
            e.printStackTrace();
            log.error("获取会员的积分流水列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取会员的积分流水列表异常!");
        }
        return msg;
    }
    /**
     * 获取会员的积分流水列表-统计
     */
    @RequestMapping(value = "/getUserAccountListCount")
    @ResponseBody
    @DataAuthority
    public Map<String,Object> getUserAccountListCount(@RequestParam("info") String param,
                                                      @RequestParam("businessNo") String businessNo) {
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ScoreAccountInfo info = JSON.parseObject(param, ScoreAccountInfo.class);
            int num=scoreAccountService.getUserAccountListCount(info,businessNo);
            msg.put("total", num);
            msg.put("status", true);
        } catch (Exception e){
            e.printStackTrace();
            log.error("获取会员的积分流水列表-统计异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取会员的积分流水列表-统计异常!");
        }
        return msg;
    }

    /**
     * 积分流水列表查询
     */
    @RequestMapping(value = "/getSelectPageList")
    @ResponseBody
    public Map<String,Object> getSelectPageList(@ModelAttribute("page") Page<ScoreAccountInfo> page,
                                                @RequestParam("info") String param){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ScoreAccountInfo info = JSON.parseObject(param, ScoreAccountInfo.class);
            scoreAccountService.getSelectPageList(info,page);
            msg.put("page", page);
            msg.put("status", true);
        } catch (Exception e){
            e.printStackTrace();
            log.error("积分流水列表查询异常!",e);
            msg.put("status", false);
            msg.put("msg", "积分流水列表查询异常!");
        }
        return msg;
    }
    /**
     * 积分流水列表查询-统计
     */
    @RequestMapping(value = "/getSelectPageListCount")
    @ResponseBody
    public Map<String,Object> getSelectPageListCount(@RequestParam("info") String param) {
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ScoreAccountInfo info = JSON.parseObject(param, ScoreAccountInfo.class);
            ScoreAccountCount totalInfo=scoreAccountService.getSelectPageListCount(info);
            msg.put("totalInfo", totalInfo);
            msg.put("status", true);
        } catch (Exception e){
            e.printStackTrace();
            log.error("积分流水列表查询-统计异常!",e);
            msg.put("status", false);
            msg.put("msg", "积分流水列表查询-统计异常!");
        }
        return msg;
    }

    /**
     * 导出积分发行列表
     */
    @RequestMapping(value="/importDetail")
    @ResponseBody
    @SystemLog(description = "导出积分发行列表",operCode="scoreAccountAction.importDetail")
    public Map<String, Object> importDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        Map<String, Object> msg=new HashMap<String,Object>();
        try {
            ScoreAccountInfo info = JSON.parseObject(param, ScoreAccountInfo.class);
            scoreAccountService.importDetail(info,response);
        }catch (Exception e){
            log.error("导出积分发行列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "导出积分发行列表异常!");
        }
        return msg;
    }

    /**
     * 赠送积分
     */
    @RequestMapping(value = "/addScore")
    @ResponseBody
    @SystemLog(description = "赠送积分",operCode="scoreAccountAction.addScore")
    public Map<String,Object> addScore(@RequestParam("info") String param) {
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            AddScoreInfo info = JSON.parseObject(param, AddScoreInfo.class);
            scoreAccountService.addScore(info,msg);
        } catch (Exception e){
            e.printStackTrace();
            msg.put("status", false);
            msg.put("msg", "赠送积分异常!");
        }
        return msg;
    }

}
