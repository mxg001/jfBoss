package cn.eeepay.framework.serviceImpl.integral;

import cn.eeepay.framework.dao.integral.BusinessFlowDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.integral.AddScoreInfo;
import cn.eeepay.framework.model.integral.BusinessFlow;
import cn.eeepay.framework.model.integral.LeaguerInfo;
import cn.eeepay.framework.model.sys.SysDict;
import cn.eeepay.framework.model.sys.UserLoginInfo;
import cn.eeepay.framework.service.integral.BusinessFlowService;
import cn.eeepay.framework.service.sys.SysConfigService;
import cn.eeepay.framework.service.sys.SysDictService;
import cn.eeepay.framework.util.DateUtils;
import cn.eeepay.framework.util.HttpclientInterface;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2019/7/15/015.
 * @author  liuks
 * 业务流水实现类
 */
@Service("businessFlowService")
public class BusinessFlowServiceImpl implements BusinessFlowService {


    private static final Logger log = LoggerFactory.getLogger(BusinessFlowServiceImpl.class);

    @Resource
    private BusinessFlowDao businessFlowDao;

    @Resource
    private SysDictService sysDictService;

    @Resource
    private SysConfigService sysConfigService;

    @Override
    public List<BusinessFlow> getSelectPageList(BusinessFlow info, Page<BusinessFlow> page) {
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setBusinessNoFilter(principal.getBusinessNos());
        info.setPageSize(page.getPageSize());
        info.setPageFirst(page.getFirst());
        List<BusinessFlow> list=businessFlowDao.getSelectPageList(info);
        if(list!=null && list.size()>0){
            for (BusinessFlow businessFlow : list) {
                if(!org.springframework.util.StringUtils.isEmpty(businessFlow.getMobilePhone())){
                    businessFlow.setMobilePhone(StringUtil.sensitiveInformationHandle(businessFlow.getMobilePhone(),0));
                }
            }
        }
        page.setResult(list);
        return list;
    }

    @Override
    public int getSelectPageListCount(BusinessFlow info) {
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setBusinessNoFilter(principal.getBusinessNos());
        return businessFlowDao.getSelectPageListCount(info);
    }

    @Override
    public void importDetail(BusinessFlow info, HttpServletResponse response) throws Exception {

        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setBusinessNoFilter(principal.getBusinessNos());
        List<BusinessFlow> list=businessFlowDao.getSelectPageList(info);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "业务流水查询列表"+sdf.format(new Date())+".xlsx" ;
        String fileNameFormat = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("taskOrderNo",null);
            maps.put("rewardOrderNo",null);
            maps.put("leaguerNo",null);
            maps.put("leaguerName",null);
            maps.put("businessName",null);
            maps.put("originUserNo",null);
            maps.put("mobilePhone",null);
            maps.put("rewardStatus",null);
            maps.put("rewardType",null);
            maps.put("calcTypeName",null);
            maps.put("fromType",null);
            maps.put("fromDesc",null);
            maps.put("fromSystem",null);
            maps.put("rewardName",null);
            maps.put("rewardNum",null);
            maps.put("originOrderNo",null);
            maps.put("createTime", null);

            data.add(maps);
        }else{

            Map<String, String> rewardStatusMap=new HashMap<String, String>();//下发状态
            rewardStatusMap.put("0","未发放");
            rewardStatusMap.put("1","已发放");
            Map<String, String> rewardTypeMap=sysDictService.selectMapByKey("REWARD_TYPE");//权益类型
            Map<String, String> fromTypeMap=sysDictService.selectMapByKey("FROM_TYPE");//权益来源类型
            Map<String, String> fromSystemMap=sysDictService.selectMapByKey("FROM_SYSTEM");//积分来源系统

            for (BusinessFlow item : list) {
                item.setMobilePhone(StringUtil.sensitiveInformationHandle(item.getMobilePhone(),0));
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("taskOrderNo",item.getTaskOrderNo());
                maps.put("rewardOrderNo",item.getRewardOrderNo());
                maps.put("leaguerNo",item.getLeaguerNo());
                maps.put("leaguerName",item.getLeaguerName());
                maps.put("businessName",item.getBusinessName());
                maps.put("originUserNo",item.getOriginUserNo());
                maps.put("mobilePhone",item.getMobilePhone());
                maps.put("rewardStatus",rewardStatusMap.get(item.getRewardStatus()));
                maps.put("rewardType",rewardTypeMap.get(item.getRewardType()));
                maps.put("calcTypeName",item.getCalcTypeName());
                maps.put("fromType",fromTypeMap.get(item.getFromType()));
                maps.put("fromDesc",item.getFromDesc());
                maps.put("fromSystem",fromSystemMap.get(item.getFromSystem()));
                maps.put("rewardName",item.getRewardName());
                maps.put("rewardNum",item.getRewardNum()==null?"":item.getRewardNum().toString());
                maps.put("originOrderNo",item.getOriginOrderNo());
                maps.put("createTime", item.getCreateTime()==null?"":sdf1.format(item.getCreateTime()));

                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"taskOrderNo","rewardOrderNo","leaguerNo","leaguerName","businessName",
                "originUserNo","mobilePhone","rewardStatus","rewardType","calcTypeName","fromType","fromDesc","fromSystem",
                "rewardName","rewardNum","originOrderNo","createTime"
        };
        String[] colsName = new String[]{"业务单号","流水号","会员编号","会员名称","业务组",
                "用户编号","用户手机号","下发状态","物品类型","物品名称","来源类型","来源描述","来源系统",
                "权益名称","数量","来源业务订单","创建时间"
        };
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            e.printStackTrace();
            log.error("导出业务流水查询列表异常!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }

    @Override
    public int lowerHair(String rewardOrderNo, String businessNo, Map<String, Object> msg) {
        BusinessFlow oldInfo=businessFlowDao.getLowerHair(rewardOrderNo,businessNo);
        if(oldInfo!=null){
            if("0".equals(oldInfo.getRewardStatus())){
                String reStr=lowerHairInterface(oldInfo);
                if(reStr!=null){
                    JSONObject json = JSONObject.parseObject(reStr);
                    if(json!=null){
                        if("200".equals(json.getString("code"))){
                            msg.put("status", true);
                            msg.put("msg", "下发操作成功!");
                            return 1;
                        }else{
                            msg.put("status", false);
                            msg.put("msg", json.getString("msg"));
                            return 0;
                        }
                    }else{
                        msg.put("status", false);
                        msg.put("msg", "下发操作接口异常!");
                        return 0;
                    }
                }else{
                    msg.put("status", false);
                    msg.put("msg", "下发操作接口异常!");
                    return 0;
                }
            }else{
                msg.put("status", false);
                msg.put("msg", "下发失败,该权益下发状态不允许下发!");
                return 0;
            }
        }
        msg.put("status", false);
        msg.put("msg", "下发操作失败!");
        return 0;
    }

    /**
     * 权益下发接口
     * @param info
     * @return
     */
    private String lowerHairInterface(BusinessFlow info){
        String returnStr=null;
        String baseUrl = sysConfigService.getByKey("JF_SERVICE_URL");
        if(StringUtils.isNotBlank(baseUrl)){
            String url=baseUrl+"/grantReward";
            Map<String,Object> dataMap = new HashMap<String,Object>();
            dataMap.put("rewardOrderNo",info.getRewardOrderNo());
            UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            dataMap.put("operater",principal.getUsername());

            Map<String,Object> claims = new TreeMap<>();
            claims.put("data",JSONObject.toJSONString(dataMap));
            claims.put("businessNo",info.getBusinessNo());
            claims.put("timestamp", new Date().getTime());
            String sign = HttpclientInterface.sign(info.getDigestKey(),dataMap);
            claims.put("sign",sign);

            log.info("权益下发接口请求,请求路径:{},参数：{}",url, JSONObject.toJSONString(claims));
            returnStr = HttpclientInterface.httpPost(url, claims);
            log.info("权益下发操作接口请求,返回结果：{}", returnStr);
        }
        return returnStr;
    }
}
