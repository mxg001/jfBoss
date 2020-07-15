package cn.eeepay.boss.action.integral;

import cn.eeepay.boss.system.DataAuthority;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.integral.LeaguerInfo;
import cn.eeepay.framework.service.integral.LeaguerService;
import cn.eeepay.framework.util.ResponseUtil;
import com.alibaba.fastjson.JSON;
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
 * Created by Administrator on 2019/7/10/010.
 */
@Controller
@RequestMapping(value = "/leaguerAction")
public class LeaguerAction {


    private static final Logger log = LoggerFactory.getLogger(LeaguerAction.class);

    @Resource
    private LeaguerService leaguerService;

    /**
     * 会员查询列表
     */
    @RequestMapping(value = "/getSelectPageList")
    @ResponseBody
    public Map<String,Object> getSelectPageList(@ModelAttribute("page") Page<LeaguerInfo> page,
                                                @RequestParam("info") String param) {
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            LeaguerInfo info = JSON.parseObject(param, LeaguerInfo.class);
            leaguerService.getSelectPageList(info,page);
            msg.put("page", page);
            msg.put("status", true);
        } catch (Exception e){
            e.printStackTrace();
            log.error("会员查询列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "会员查询列表异常!");
        }
        return msg;
    }
    /**
     * 会员查询列表-统计
     */
    @RequestMapping(value = "/getSelectPageListCount")
    @ResponseBody
    public Map<String,Object> getSelectPageListCount(@RequestParam("info") String param) {
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            LeaguerInfo info = JSON.parseObject(param, LeaguerInfo.class);
            int num=leaguerService.getSelectPageListCount(info);
            msg.put("total", num);
            msg.put("status", true);
        } catch (Exception e){
            e.printStackTrace();
            log.error("会员查询列表-统计异常!",e);
            msg.put("status", false);
            msg.put("msg", "会员查询列表-统计异常!");
        }
        return msg;
    }

    /**
     * 获取会员详情
     */
    @RequestMapping(value = "/getLeaguerInfo")
    @ResponseBody
    @DataAuthority
    public Map<String,Object> getLeaguerInfo(@RequestParam("id") Long id,
                                                @RequestParam("businessNo") String businessNo) {
        Map<String, Object> msg= getLeaguerInfoBase(id,businessNo,0);
        return msg;
    }
    /**
     * 获取会员详情
     */
    @RequestMapping(value = "/leaguerDetailSensitive")
    @ResponseBody
    @DataAuthority
    public Map<String,Object> leaguerDetailSensitive(@RequestParam("id") Long id,
                                             @RequestParam("businessNo") String businessNo ) {
        Map<String, Object> msg= getLeaguerInfoBase(id,businessNo,1);
        return msg;
    }
    //showSensitive 0 隐藏 1显示
    private Map<String,Object> getLeaguerInfoBase(Long id,String businessNo,int showSensitive){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            leaguerService.getLeaguerInfo(id,businessNo,msg,showSensitive);
        } catch (Exception e){
            e.printStackTrace();
            log.error("获取会员详情异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取会员详情异常!");
        }
        return msg;
    }


    /**
     * 导出会员列表
     */
    @RequestMapping(value="/importDetail")
    @ResponseBody
    @SystemLog(description = "导出会员列表",operCode="leaguerAction.importDetail")
    public Map<String, Object> importDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        Map<String, Object> msg=new HashMap<String,Object>();
        try {
            LeaguerInfo info = JSON.parseObject(param, LeaguerInfo.class);
            leaguerService.importDetail(info,response);
        }catch (Exception e){
            log.error("导出会员列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "导出会员列表异常!");
        }
        return msg;
    }

    /**
     * 通过会员编号或者来源用户号 获取会员信息
     * 只能获取操作人具有业务组数据权限的会员
     */
    @RequestMapping(value = "/checkLeaguerInfo")
    @ResponseBody
    public Map<String,Object> checkLeaguerInfo(@RequestParam("leaguerNo") String leaguerNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            LeaguerInfo info=leaguerService.checkLeaguerInfo(leaguerNo,"");
            msg.put("info", info);
            msg.put("status", true);
        } catch (Exception e){
            e.printStackTrace();
            log.error("获取会员信息异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取会员信息异常!");
        }
        return msg;
    }

    /**
     * 通过会员编号或者来源用户号 获取会员信息
     * 只能获取操作人具有业务组数据权限的会员
     * 某个业务组下的
     */
    @RequestMapping(value = "/getLeaguerInfoByBus")
    @ResponseBody
    public Map<String,Object> getLeaguerInfoByBus(@RequestParam("leaguerNo") String leaguerNo,@RequestParam("businessNo") String businessNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            LeaguerInfo info=leaguerService.checkLeaguerInfo(leaguerNo,businessNo);
            msg.put("info", info);
            msg.put("status", true);
        } catch (Exception e){
            e.printStackTrace();
            log.error("获取会员信息异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取会员信息异常!");
        }
        return msg;
    }


    /**
     * 模板下载
     */
    @RequestMapping("/downloadTemplate")
    public String downloadAdjustAccTemplate(HttpServletRequest request, HttpServletResponse response){
        String filePath = request.getServletContext().getRealPath("/")+ File.separator+"template"+File.separator+"leaguerImportTemplate.xlsx";
        log.info(filePath);
        ResponseUtil.download(response, filePath,"会员同步模板.xlsx");
        return null;
    }
    /**
     * 会员同步
     */
    @RequestMapping(value="/importDiscount")
    @ResponseBody
    @SystemLog(description = "会员同步",operCode="leaguerAction.importDiscount")
    public Map<String, Object> importDiscount(@RequestParam("file") MultipartFile file,@RequestParam("businessNo") String businessNo){
        Map<String, Object> msg = new HashMap<>();
        try {
            if (!file.isEmpty()) {
                String format=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                if(!format.equals(".xls") && !format.equals(".xlsx")){
                    msg.put("status", false);
                    msg.put("msg", "导入文件格式错误!");
                    return msg;
                }
            }
            msg = leaguerService.importDiscount(file,businessNo);
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "导入失败!");
            e.printStackTrace();
            log.error("导入失败!",e);
        }
        return msg;
    }

    /**
     * 根据用户编号和会员名称查询会员信息
     */
    @RequestMapping(value = "/findLeaguerInfoByUserNo")
    @ResponseBody
    public Map<String,Object> findLeaguerInfoByUserNo(@RequestParam("key") String key){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            final List<LeaguerInfo> info = leaguerService.findLeaguerInfoByUserNo(key, 50);
            msg.put("info", info);
            msg.put("status", true);
        } catch (Exception e){
            e.printStackTrace();
            log.error("获取会员信息异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取会员信息异常!");
        }
        return msg;
    }

}
