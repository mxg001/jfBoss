package cn.eeepay.boss.action.integral;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.integral.PopupConfig;
import cn.eeepay.framework.service.integral.PopupService;
import cn.eeepay.framework.util.DateUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/pop")
@Controller
public class PopupAction {

    @Resource
    private PopupService popupService;

    private final static Logger log = LoggerFactory.getLogger(PopupAction.class);

    /**
     * 新增弹窗
     * @param param
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public Map<String, Object> add(@RequestParam("info") String param){
        Map<String, Object> result = new HashMap<>();
        try {
            PopupConfig popupConfig = JSON.parseObject(param, PopupConfig.class);
            if(null != popupConfig.getId()){
                popupService.edit(popupConfig);
            }else{
                popupService.add(popupConfig);
            }
            result.put("status", true);
        } catch (Exception e) {
            result.put("status", false);
            result.put("msg", "操作失败！");
        }
        return result;
    }


    /**
     * 获取业务组列表
     */
    @RequestMapping(value = "/selectWithPage")
    @ResponseBody
    public Map<String,Object> selectWithPage(@ModelAttribute("page") Page<PopupConfig> page){
        Map<String, Object> map = new HashMap<>();
        try {
            popupService.selectWithPage(page);
            map.put("page", page);
            map.put("status", true);
        } catch (Exception e) {
            log.error("获取弹窗列表异常!",e);
            map.put("status", false);
            map.put("msg", "获取弹窗列表异常!");
        }
        return map;
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String,Object> delete(String id){
        Map<String, Object> map = new HashMap<>();
        try {
            popupService.delete(id);
            map.put("status", true);
            map.put("msg", "删除成功!");
        } catch (Exception e) {
            log.error("删除弹窗失败!",e);
            map.put("status", false);
            map.put("msg", "删除弹窗失败!");
        }
        return map;
    }

    @RequestMapping("/getPopupConfig")
    @ResponseBody
    public Map<String, Object> getPopupConfig(int id){
        Map<String, Object> map = new HashMap<>();
        try {
            PopupConfig popupConfig = popupService.selectById(id);
            map.put("status", true);
            map.put("info", popupConfig);
        } catch (Exception e) {
            log.error("获取弹窗信息失败!",e);
            map.put("status", false);
            map.put("msg", "获取弹窗信息失败!");
        }
        return map;
    }

}
