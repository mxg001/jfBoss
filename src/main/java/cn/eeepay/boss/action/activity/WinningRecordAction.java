package cn.eeepay.boss.action.activity;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.activity.WinningRecord;
import cn.eeepay.framework.service.activity.WinningRecordService;
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
 * 中奖记录查询
 */
@Controller
@RequestMapping(value = "/winningRecord")
public class WinningRecordAction {

    private static final Logger log = LoggerFactory.getLogger(WinningRecordAction.class);

    @Resource
    private WinningRecordService winningRecordService;

    /**
     * 抽奖记录查询
     */
    @RequestMapping(value = "/getSelectPageList")
    @ResponseBody
    public Map<String,Object> getWinningRecordList(@ModelAttribute("page") Page<WinningRecord> page,
                                                   @RequestParam("info") String param){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            WinningRecord info = JSON.parseObject(param, WinningRecord.class);
            winningRecordService.getSelectPageList(info,page);
            msg.put("page", page);
            msg.put("status", true);
        } catch (Exception e){
            e.printStackTrace();
            log.error("抽奖记录查询异常!",e);
            msg.put("status", false);
            msg.put("msg", "抽奖记录查询异常!");
        }
        return msg;
    }
    /**
     * 抽奖记录查询统计
     */
    @RequestMapping(value = "/getWinningRecordCount")
    @ResponseBody
    public Map<String,Object> getWinningRecordCount(@RequestParam("info") String param){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            WinningRecord info = JSON.parseObject(param, WinningRecord.class);
            int num=winningRecordService.getWinningRecordCount(info);
            msg.put("total", num);
            msg.put("status", true);
        } catch (Exception e){
            e.printStackTrace();
            log.error("抽奖记录查询统计异常!",e);
            msg.put("status", false);
            msg.put("msg", "抽奖记录查询统计异常!");
        }
        return msg;
    }
    /**
     * 导出抽奖记录
     */
    @RequestMapping(value="/importDetail")
    @ResponseBody
    @SystemLog(description = "导出抽奖记录",operCode="winningRecord.importDetail")
    public Map<String, Object> importDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        Map<String, Object> msg=new HashMap<String,Object>();
        try {
            WinningRecord info = JSON.parseObject(param, WinningRecord.class);
            winningRecordService.importDetail(info,response);
        }catch (Exception e){
            log.error("导出抽奖记录异常!",e);
            msg.put("status", false);
            msg.put("msg", "导出抽奖记录异常!");
        }
        return msg;
    }
}
