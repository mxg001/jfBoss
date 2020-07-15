package cn.eeepay.boss.action.integralMall;


import cn.eeepay.boss.system.DataAuthority;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.integralMall.GoodsManagement;
import cn.eeepay.framework.service.integral.ActivityChoiceService;
import cn.eeepay.framework.service.integralMall.GoodsManagementService;
import com.alibaba.fastjson.JSON;
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

@Controller
@RequestMapping(value = "/goodsManagement")
public class GoodsManagementAction {

    private static final Logger log = LoggerFactory.getLogger(GoodsManagementAction.class);

    @Resource
    private GoodsManagementService goodsManagementService;

    @Resource
    private ActivityChoiceService activityChoiceService;

    /**
     * 物品管理列表
     */
    @RequestMapping(value = "/getSelectPageList")
    @ResponseBody
    public Map<String,Object> getSelectPageList(@ModelAttribute("page") Page<GoodsManagement> page,
                                                @RequestParam("info") String param){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            GoodsManagement info = JSON.parseObject(param, GoodsManagement.class);
            goodsManagementService.getSelectPageList(info,page);
            msg.put("page", page);
            msg.put("status", true);
        } catch (Exception e){
            e.printStackTrace();
            log.error("物品管理列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "物品管理列表异常!");
        }
        return msg;
    }

    /**
     * 物品管理列表
     */
    @RequestMapping(value = "/getAllGoodsType")
    @ResponseBody
    public Map<String,Object> getAllGoodsType(@RequestParam("info") String param){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            GoodsManagement info = JSON.parseObject(param, GoodsManagement.class);
            msg.put("data", goodsManagementService.getAllGoodsType(info));
            msg.put("status", true);
        } catch (Exception e){
            e.printStackTrace();
            log.error("物品管理列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "物品管理列表异常!");
        }
        return msg;
    }

    /**
     * 新增物品
     */
    @RequestMapping(value = "/addGood")
    @ResponseBody
    @DataAuthority
    @SystemLog(description = "新增物品",operCode="goodsManagement.addGood")
    public Map<String,Object> addGoods(@RequestParam("info") String param,
                                       @RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            GoodsManagement info = JSON.parseObject(param, GoodsManagement.class);
            info.setBusinessNo(businessNo);
            int num = goodsManagementService.addGood(info);
            if(num>0){
                msg.put("msg", "新增成功!");
                msg.put("status", true);
            }else{
                msg.put("msg", "新增失败!");
                msg.put("status", false);
            }
        } catch (Exception e){
            e.printStackTrace();
            log.error("新增异常!",e);
            msg.put("status", false);
            msg.put("msg", "新增异常!");
        }
        return msg;
    }
    /**
     * 修改物品
     */
    @RequestMapping(value = "/updateGood")
    @ResponseBody
    @DataAuthority
    @SystemLog(description = "修改物品",operCode="goodsManagement.updateGood")
    public Map<String,Object> updateGoods(@RequestParam("info") String param,
                                            @RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            GoodsManagement info = JSON.parseObject(param, GoodsManagement.class);
            info.setBusinessNo(businessNo);
            int num = goodsManagementService.updateGood(info);
            if(num>0){
                msg.put("msg", "修改成功!");
                msg.put("status", true);
            }else{
                msg.put("msg", "修改失败!");
                msg.put("status", false);
            }
        } catch (Exception e){
            e.printStackTrace();
            log.error("修改异常!",e);
            msg.put("status", false);
            msg.put("msg", "修改异常!");
        }
        return msg;
    }

    /**
     * 逻辑删除
     */
    @RequestMapping(value = "/deleteGood")
    @ResponseBody
    @DataAuthority
    @SystemLog(description = "删除物品",operCode="goodsManagement.deleteGood")
    public Map<String,Object> deleteGood(@RequestParam("id") Long id,@RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            GoodsManagement oldInfo=goodsManagementService.getGoodInfoById(id,businessNo);
            if(oldInfo!=null){
                if(activityChoiceService.checkGood(oldInfo.getGoodsCode())){
                    msg.put("msg", "物品已有关联,不能删除!");
                    msg.put("status", false);
                    return msg;
                }
                int num = goodsManagementService.deleteGood(id,businessNo);
                if(num>0){
                    msg.put("msg", "删除成功!");
                    msg.put("status", true);
                }else{
                    msg.put("msg", "删除失败!");
                    msg.put("status", false);
                }
            }else{
                msg.put("msg", "删除失败-数据异常!");
                msg.put("status", false);
            }

        } catch (Exception e){
            e.printStackTrace();
            log.error("删除异常!",e);
            msg.put("status", false);
            msg.put("msg", "删除异常!");
        }
        return msg;
    }

    /**
     * 获取物品列表
     */
    @RequestMapping(value = "/getGoodsList")
    @ResponseBody
    public Map<String,Object> getGoodsList(@RequestParam("goodsType") String goodsType,@RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            List<GoodsManagement> list = goodsManagementService.getGoodsList(goodsType,businessNo);
            msg.put("list", list);
            msg.put("status", true);
        } catch (Exception e){
            e.printStackTrace();
            log.error("获取物品列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取物品列表异常!");
        }
        return msg;
    }
}
