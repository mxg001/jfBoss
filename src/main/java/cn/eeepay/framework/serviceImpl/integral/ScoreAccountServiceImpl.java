package cn.eeepay.framework.serviceImpl.integral;

import cn.eeepay.framework.dao.integral.ScoreAccountDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.integral.AddScoreInfo;
import cn.eeepay.framework.model.integral.LeaguerInfo;
import cn.eeepay.framework.model.integral.ScoreAccountCount;
import cn.eeepay.framework.model.integral.ScoreAccountInfo;
import cn.eeepay.framework.model.sys.SysDict;
import cn.eeepay.framework.model.sys.UserLoginInfo;
import cn.eeepay.framework.service.integral.LeaguerService;
import cn.eeepay.framework.service.integral.ScoreAccountService;
import cn.eeepay.framework.service.sys.RandomNumService;
import cn.eeepay.framework.service.sys.SysConfigService;
import cn.eeepay.framework.service.sys.SysDictService;
import cn.eeepay.framework.util.*;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2019/7/11/011.
 * @author  liuks
 * 积分入账 service实现
 */
@Service("scoreAccountService")
public class ScoreAccountServiceImpl implements ScoreAccountService {

    private static final Logger log = LoggerFactory.getLogger(ScoreAccountServiceImpl.class);

    @Resource
    private ScoreAccountDao scoreAccountDao;

    @Resource
    private SysDictService sysDictService;

    @Resource
    private SysConfigService sysConfigService;

    @Resource
    private LeaguerService leaguerService;

    @Resource
    private RandomNumService randomNumService;

    @Override
    public List<ScoreAccountInfo> getUserAccountList(ScoreAccountInfo info, Page<ScoreAccountInfo> page, String businessNo) {
        info.setPageSize(page.getPageSize());
        info.setPageFirst(page.getFirst());
        info.setBusinessNo(businessNo);
        List<ScoreAccountInfo> list=scoreAccountDao.getUserAccountList(info);
        page.setResult(list);
        return list;
    }

    @Override
    public int getUserAccountListCount(ScoreAccountInfo info, String businessNo) {
        info.setBusinessNo(businessNo);
        int num=scoreAccountDao.getUserAccountListCount(info);
        return num;
    }

    @Override
    public List<ScoreAccountInfo> getSelectPageList(ScoreAccountInfo info, Page<ScoreAccountInfo> page) {
        info.setPageSize(page.getPageSize());
        info.setPageFirst(page.getFirst());
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setBusinessNoFilter(principal.getBusinessNos());
        convertDate(info);
        List<ScoreAccountInfo> list=scoreAccountDao.getSelectPageList(info);
        if(list!=null && list.size()>0){
            for (ScoreAccountInfo scoreAccountInfo : list) {
                if(!org.springframework.util.StringUtils.isEmpty(scoreAccountInfo.getMobilePhone())){
                    scoreAccountInfo.setMobilePhone(StringUtil.sensitiveInformationHandle(scoreAccountInfo.getMobilePhone(),0));
                }
            }
        }
        page.setResult(list);
        return list;
    }

    private  void convertDate(ScoreAccountInfo info){
        if(StringUtils.isNotBlank(info.getScoreBatchDateStr())){
            SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
            try {
                Date date = format.parse(info.getScoreBatchDateStr());
                //选择时间所出月份1号 00:00:00.000
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.set(Calendar.DAY_OF_MONTH,1);
                cal.set(Calendar.HOUR_OF_DAY,0);
                cal.set(Calendar.MINUTE,0);
                cal.set(Calendar.SECOND,0);
                cal.set(Calendar.MILLISECOND,0);
                Date monthDate=cal.getTime();
                info.setScoreBatchDate(monthDate);
            } catch (ParseException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
    @Override
    public ScoreAccountCount getSelectPageListCount(ScoreAccountInfo info) {
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setBusinessNoFilter(principal.getBusinessNos());
        convertDate(info);
        return scoreAccountDao.getSelectPageListCount(info);
    }

    @Override
    public void importDetail(ScoreAccountInfo info, HttpServletResponse response) throws Exception{
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setBusinessNoFilter(principal.getBusinessNos());
        convertDate(info);
        List<ScoreAccountInfo> list=scoreAccountDao.getSelectPageList(info);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM") ;
        String fileName = "积分发行列表"+sdf.format(new Date())+".xlsx" ;
        String fileNameFormat = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("accountSerialNo",null);
            maps.put("leaguerNo",null);
            maps.put("leaguerName",null);
            maps.put("businessName",null);
            maps.put("originUserNo",null);
            maps.put("mobilePhone",null);
            maps.put("scoreBatchDate",null);
            maps.put("accType",null);
            maps.put("accScore",null);
            maps.put("fromType",null);
            maps.put("fromDesc",null);
            maps.put("fromSystem",null);
            maps.put("createTime", null);
            maps.put("serviceOrderNo",null);
            maps.put("operater",null);
            data.add(maps);
        }else{

            Map<String, String> accTypeMap=new HashMap<String, String>();//入账类型
            accTypeMap.put("1","入账");
            accTypeMap.put("2","出账");

            Map<String, String> fromTypeMap=sysDictService.selectMapByKey("FROM_TYPE");//权益来源类型
            Map<String, String> fromSystemMap=sysDictService.selectMapByKey("FROM_SYSTEM");//积分来源系统

            for (ScoreAccountInfo item : list) {
                item.setMobilePhone(StringUtil.sensitiveInformationHandle(item.getMobilePhone(),0));

                Map<String, String> maps = new HashMap<String, String>();

                maps.put("accountSerialNo",item.getAccountSerialNo());
                maps.put("leaguerNo",item.getLeaguerNo());
                maps.put("leaguerName",item.getLeaguerName());
                maps.put("businessName",item.getBusinessName());
                maps.put("originUserNo",item.getOriginUserNo());
                maps.put("mobilePhone",item.getMobilePhone());
                maps.put("scoreBatchDate", item.getScoreBatchDate()==null?"":sdf2.format(item.getScoreBatchDate()));
                maps.put("accType",accTypeMap.get(item.getAccType()));
                maps.put("accScore",item.getAccScore()==null?"":item.getAccScore().toString());
                maps.put("fromType",fromTypeMap.get(item.getFromType()));
                maps.put("fromDesc",item.getFromDesc());
                maps.put("fromSystem",fromSystemMap.get(item.getFromSystem()));
                maps.put("createTime", item.getCreateTime()==null?"":sdf1.format(item.getCreateTime()));
                maps.put("serviceOrderNo",item.getServiceOrderNo());
                maps.put("operater",item.getOperater());

                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"accountSerialNo","leaguerNo","leaguerName","businessName","originUserNo","mobilePhone",
                "scoreBatchDate","accType","accScore","fromType","fromDesc","fromSystem","createTime",
                "serviceOrderNo","operater"
        };
        String[] colsName = new String[]{"出入账流水号","会员编号","会员名称","业务组","用户编号","用户手机号",
                "流水批次","积分出入账类型","记账积分","来源类型","来源描述","来源系统","创建时间",
                "来源业务订单","操作人"
        };
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            e.printStackTrace();
            log.error("导出积分发行列表异常!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }

    }

    @Override
    public int addScore(AddScoreInfo info,Map<String,Object> msg) {
        //校验会员号是否是自己
        LeaguerInfo old=leaguerService.checkLeaguerInfo(info.getLeaguerNo(),"");
        if(old!=null){
            info.setLeaguerNo(old.getLeaguerNo());
            //调用增加积分接口
            String reStr=addScoreInterface(old,info);
            if(reStr!=null){
                JSONObject json = JSONObject.parseObject(reStr);
                if(json!=null){
                    if("200".equals(json.getString("code"))){
                        msg.put("status", true);
                        msg.put("msg", "赠送积分成功!");
                        return 1;
                    }else{
                        msg.put("status", false);
                        msg.put("msg", json.getString("msg"));
                        return 0;
                    }
                }else{
                    msg.put("status", false);
                    msg.put("msg", "新增积分接口异常!");
                    return 0;
                }
            }else{
                msg.put("status", false);
                msg.put("msg", "新增积分接口异常!");
                return 0;
            }
        }else{
            msg.put("status", false);
            msg.put("msg", "该会员号不存在!");
        }

        return 0;
    }

    private String addScoreInterface(LeaguerInfo old,AddScoreInfo info){
        String returnStr=null;
        String baseUrl = sysConfigService.getByKey("JF_SERVICE_URL");
        if(StringUtils.isNotBlank(baseUrl)){
            String url=baseUrl+"/score/addScore";

            //当前时间所出月份1号 00:00:00.000
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, info.getRewardEffeDay().intValue());
            cal.set(Calendar.DAY_OF_MONTH,1);

            Map<String,String> dataMap = new HashMap<String,String>();
            dataMap.put("orderNo",randomNumService.getRandomNumberByData("ADD","vipScore",13));
            dataMap.put("operateValue",info.getAccScore()==null?"0":info.getAccScore().toString());
            dataMap.put("leaguerNo",info.getLeaguerNo());
            dataMap.put("fromSystem","boss");
            dataMap.put("scoreFrom","6");
            dataMap.put("fromDesc","手动新增");
            dataMap.put("effeDate",DateUtils.format(cal.getTime(),"yyyy-MM-dd"));
            UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            dataMap.put("operater",principal.getUsername());

            Map<String,Object> claims = new TreeMap<>();
            claims.put("data",JSONObject.toJSONString(dataMap));
            claims.put("businessNo",old.getBusinessNo());
            claims.put("timestamp", new Date().getTime());
            String sign = HttpclientInterface.sign(old.getDigestKey(),claims);
            claims.put("sign",sign);

            log.info("新增积分接口请求,请求路径:{},参数：{}",url, JSONObject.toJSONString(claims));
            returnStr = HttpclientInterface.httpPost(url, claims);
            log.info("新增积分接口请求,返回结果：{}", returnStr);
        }
        return returnStr;
    }

}
