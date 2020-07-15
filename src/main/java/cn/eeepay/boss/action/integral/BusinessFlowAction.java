package cn.eeepay.boss.action.integral;

import cn.eeepay.boss.system.DataAuthority;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.integral.BusinessFlow;
import cn.eeepay.framework.service.integral.BusinessFlowService;
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
 * Created by Administrator on 2019/7/15/015.
 * @author  liuks
 * 业务流水
 */
@Controller
@RequestMapping(value = "/businessFlowAction")
public class BusinessFlowAction {

    private static final Logger log = LoggerFactory.getLogger(BusinessFlowAction.class);

    @Resource
    private BusinessFlowService businessFlowService;

    /**
     * 业务流水查询
     */
    @RequestMapping(value = "/getSelectPageList")
    @ResponseBody
    public Map<String,Object> getSelectPageList(@ModelAttribute("page") Page<BusinessFlow> page,
                                                @RequestParam("info") String param) {
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            BusinessFlow info = JSON.parseObject(param, BusinessFlow.class);
            businessFlowService.getSelectPageList(info,page);
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
     * 业务流水查询-统计
     */
    @RequestMapping(value = "/getSelectPageListCount")
    @ResponseBody
    public Map<String,Object> getSelectPageListCount(@RequestParam("info") String param) {
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            BusinessFlow info = JSON.parseObject(param, BusinessFlow.class);
            int num=businessFlowService.getSelectPageListCount(info);
            msg.put("total", num);
            msg.put("status", true);
        } catch (Exception e){
            e.printStackTrace();
            log.error("业务流水查询-统计!",e);
            msg.put("status", false);
            msg.put("msg", "业务流水查询-统计!");
        }
        return msg;
    }

    /**
     * 导出业务流水列表
     */
    @RequestMapping(value="/importDetail")
    @ResponseBody
    @SystemLog(description = "导出业务流水列表",operCode="businessFlowAction.importDetail")
    public Map<String, Object> importDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        Map<String, Object> msg=new HashMap<String,Object>();
        try {
            BusinessFlow info = JSON.parseObject(param, BusinessFlow.class);
            businessFlowService.importDetail(info,response);
        }catch (Exception e){
            log.error("导出业务流水列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "导出业务流水列表异常!");
        }
        return msg;
    }

    /**
     * 下发
     */
    @RequestMapping(value = "/lowerHair")
    @ResponseBody
    @SystemLog(description = "下发",operCode="businessFlowAction.lowerHair")
    @DataAuthority
    public Map<String,Object> lowerHair(@RequestParam("rewardOrderNo") String rewardOrderNo, @RequestParam("businessNo") String businessNo) {
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            businessFlowService.lowerHair(rewardOrderNo,businessNo,msg);
        } catch (Exception e){
            e.printStackTrace();
            msg.put("status", false);
            msg.put("msg", "下发操作异常!");
        }
        return msg;
    }
}
