package cn.eeepay.framework.serviceImpl.integral;

import cn.eeepay.framework.dao.integral.LeaguerDao;
import cn.eeepay.framework.dao.integral.LeaguerImportDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.integral.*;
import cn.eeepay.framework.model.sys.UserLoginInfo;
import cn.eeepay.framework.service.integral.LeaguerService;
import cn.eeepay.framework.service.sys.SysConfigService;
import cn.eeepay.framework.util.CellUtil;
import cn.eeepay.framework.util.HttpclientInterface;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2019/7/10/010.
 * @author liuks
 * 会员service实现类
 */
@Service("leaguerService")
public class LeaguerServiceImpl implements LeaguerService {

    private static final Logger log = LoggerFactory.getLogger(LeaguerServiceImpl.class);

    @Resource
    private LeaguerDao leaguerDao;
    @Resource
    private LeaguerImportDao leaguerImportDao;
    @Resource
    private SysConfigService sysConfigService;


    @Override
    public List<LeaguerInfo> getSelectPageList(LeaguerInfo info, Page<LeaguerInfo> page) {
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setPageSize(page.getPageSize());
        info.setPageFirst(page.getFirst());
        info.setBusinessNoFilter(principal.getBusinessNos());
        List<LeaguerInfo> list=leaguerDao.getSelectPageList(info);

        if(list!=null && list.size()>0){
            for (LeaguerInfo leaguerInfo : list) {
                if(!org.springframework.util.StringUtils.isEmpty(leaguerInfo.getMobilePhone())){
                    leaguerInfo.setMobilePhone(StringUtil.sensitiveInformationHandle(leaguerInfo.getMobilePhone(),0));
                }

            }
        }
        page.setResult(list);
        return list;
    }

    @Override
    public int getSelectPageListCount(LeaguerInfo info) {
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setBusinessNoFilter(principal.getBusinessNos());
        int num=leaguerDao.getSelectPageListCount(info);
        return num;
    }

    @Override
    public LeaguerInfo getLeaguerInfo(Long id, String businessNo, Map<String, Object> msg ,int showSensitive) {
        LeaguerInfo info=leaguerDao.getLeaguerInfo(id,businessNo);
        if(info!=null){
            info.setLeaguerGrowList(leaguerDao.getLeaguerGrowList(info.getLeaguerNo()));
            //积分列表
            List<LeaguerScore> list=leaguerDao.getLeaguerScoreList(info.getLeaguerNo());
            if(list!=null&&list.size()>0){
                LeaguerScore item=list.get(0);
                if(item!=null){
                    //当前时间所出月份1号 00:00:00.000
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.DAY_OF_MONTH,1);
                    cal.set(Calendar.HOUR_OF_DAY,0);
                    cal.set(Calendar.MINUTE,0);
                    cal.set(Calendar.SECOND,0);
                    cal.set(Calendar.MILLISECOND,0);
                    Date monthDate=cal.getTime();
                    item.setScoreBatchMonth(leaguerDao.getScoreBatch(monthDate,info.getLeaguerNo()));
                }
                info.setLeaguerScoreList(list);
            }
            if(1 != showSensitive){
                info.setIdCardNo(StringUtil.sensitiveInformationHandle(info.getIdCardNo(),1));
                info.setMobilePhone(StringUtil.sensitiveInformationHandle(info.getMobilePhone(),0));
            }
            msg.put("status",true);
            msg.put("info",info);
            return info;
        }
        msg.put("status",false);
        msg.put("msg","获取会员详情失败!");
        return info;
    }

    @Override
    public void importDetail(LeaguerInfo info, HttpServletResponse response) throws Exception{
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setBusinessNoFilter(principal.getBusinessNos());
        List<LeaguerInfo> list=leaguerDao.getSelectPageList(info);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd") ;

        String fileName = "会员查询列表"+sdf.format(new Date())+".xlsx" ;
        String fileNameFormat = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("leaguerNo",null);
            maps.put("leaguerName",null);
            maps.put("businessName",null);
            maps.put("originUserNo",null);
            maps.put("mobilePhone",null);
            maps.put("mLevelName",null);
            maps.put("vipLevelName",null);
            maps.put("growUpBalance",null);
            maps.put("scoreBalance",null);
            maps.put("growDate", null);
            maps.put("createTime", null);
            data.add(maps);
        }else{

            for (LeaguerInfo item : list) {
                item.setMobilePhone(StringUtil.sensitiveInformationHandle(item.getMobilePhone(),0));
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("leaguerNo",item.getLeaguerNo());
                maps.put("leaguerName",item.getLeaguerName());
                maps.put("businessName",item.getBusinessName());
                maps.put("originUserNo",item.getOriginUserNo());
                maps.put("mobilePhone",item.getMobilePhone());
                maps.put("mLevelName",item.getmLevelName());
                maps.put("vipLevelName",item.getVipLevelName());
                maps.put("growUpBalance",item.getGrowUpBalance()==null?"":item.getGrowUpBalance().toString());
                maps.put("scoreBalance",item.getScoreBalance()==null?"":item.getScoreBalance().toString());
                maps.put("growDate", item.getGrowDate()==null?"":sdf2.format(item.getGrowDate()));
                maps.put("createTime", item.getCreateTime()==null?"":sdf1.format(item.getCreateTime()));

                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"leaguerNo","leaguerName","businessName","originUserNo","mobilePhone",
                "mLevelName","vipLevelName","growUpBalance","scoreBalance","growDate","createTime"
        };
        String[] colsName = new String[]{"会员编号","会员名称","业务组","用户编号","用户手机号",
                "M等级","会员等级","会员成长值","会员积分","会员时间","创建时间"
        };
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            e.printStackTrace();
            log.error("导出会员列表异常!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }

    }

    @Override
    public LeaguerInfo checkLeaguerInfo(String strNo,String businessNo) {
        if(StringUtils.isNotBlank(strNo)){
            UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<LeaguerInfo> list=leaguerDao.getCheckLeaguerInfo(strNo,principal.getBusinessNos(),businessNo);
            if(list!=null&&list.size()>0){
                return list.get(0);
            }
        }
        return null;
    }

    @Override
    public Map<String, Object> importDiscount(MultipartFile file,String businessNo)  throws Exception {

        Map<String, Object> msg = new HashMap<>();
        Workbook wb = WorkbookFactory.create(file.getInputStream());
        //读取第一个sheet
        Sheet sheet = wb.getSheetAt(0);
        // 遍历所有单元格，读取单元格
        int row_num = sheet.getLastRowNum();
        List<LeaguerImport> addList=new ArrayList<LeaguerImport>();
        Map<String,String> checkMap=new HashMap<String,String>();//校验
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BusinessInfo businessInfo=leaguerDao.getBusinessInfoByJurisdiction(businessNo,principal.getBusinessNos());
        if(businessInfo==null){
            msg.put("status", false);
            msg.put("msg","业务组不能为空!");
            return msg;
        }

        if(row_num>50000){
            msg.put("result", false);
            msg.put("msg", "批量导入,最大条数50000条!");
            return msg;
        }

        for (int i = 1; i <= row_num; i++) {
            Row row = sheet.getRow(i);
            String mobilePhone1 = CellUtil.getCellValue(row.getCell(0));//用户手机号
            String originUserNo = CellUtil.getCellValue(row.getCell(1));//来源用户编号

            if(mobilePhone1==null||"".equals(mobilePhone1)){
                msg.put("status", false);
                msg.put("msg","导入失败,第"+(i+1)+"行,用户手机号为空!");
                return msg;
            }
            String mobilePhone=mobilePhone1.split("\\.")[0];

            if(mobilePhone.length()!=11){
                msg.put("status", false);
                msg.put("msg","导入失败,第"+(i+1)+"行,用户手机号格式不对!");
                return msg;
            }

            if(checkMap.get(mobilePhone)!=null){
                msg.put("status", false);
                msg.put("msg", "导入失败,第"+(i+1)+"行,该业务组:"+businessNo+"下,用户手机号:"+mobilePhone+"重复!");
                return msg;
            }else{
                checkMap.put(mobilePhone,"1");
            }

            LeaguerInfo leaInfo = leaguerDao.getLeaguerByPhone(mobilePhone,businessNo);
            if(leaInfo!=null){
                msg.put("status", false);
                msg.put("msg", "导入失败,第"+(i+1)+"行,该业务组:"+businessNo+"下,用户手机号:"+mobilePhone+"已存在!");
                return msg;
            }
            LeaguerImport importInfo=leaguerImportDao.getLeaguerImport(mobilePhone,businessNo);
            if(importInfo!=null){
                msg.put("status", false);
                msg.put("msg", "导入失败,第"+(i+1)+"行,该业务组:"+businessNo+"下,用户手机号:"+mobilePhone+"已导入过!");
                return msg;
            }
            LeaguerImport addInfo=new LeaguerImport();
            addInfo.setBusinessNo(businessNo);
            addInfo.setMobilePhone(mobilePhone);
            addInfo.setOriginUserNo(originUserNo);
            addInfo.setOperator(principal.getUsername());
            addList.add(addInfo);
        }

        int num=0;
        List<LeaguerImport> addListSub=new ArrayList<LeaguerImport>();
        for(int j=0;j<addList.size();j++){
            addListSub.add(addList.get(j));
            if(j>=1&&j%10000==0){
                num=num+ leaguerImportDao.insertLeaguerImportList(addListSub);
                addListSub.clear();
            }
        }
        if(addListSub.size()>0){
            num=num+ leaguerImportDao.insertLeaguerImportList(addListSub);
        }
        msg.put("status", true);
        msg.put("msg", "导入成功,总共"+addList.size()+"条数据,成功导入"+num+"条");
        return msg;
    }

    /**
     * 定时执行导入数据同步
     */
    @Override
    public void leaguerImportJob() {
        List<LeaguerImport> list=leaguerImportDao.getLeaguerImportList();
        if(list!=null&&list.size()>0){
            Map<String,String> map=new HashMap<String,String>();//防止重复查询很多次
            for(LeaguerImport item:list){
                if(map.get(item.getBusinessNo())==null){
                    BusinessInfo businessInfo=leaguerDao.getBusinessInfoByNo(item.getBusinessNo());
                    map.put(item.getBusinessNo(),businessInfo.getDigestKey());
                }
                LeaguerInfo leaInfo = leaguerDao.getLeaguerByPhone(item.getMobilePhone(),item.getBusinessNo());
                if(leaInfo!=null){
                   log.info("该业务组:"+item.getBusinessNo()+"下,用户手机号:"+item.getMobilePhone()+"已导入过");
                   leaguerImportDao.deleteLeaguerImport(item.getId());
                }else{
                    String reStr=insertVipInfoFace(item,map.get(item.getBusinessNo()));
                    //调用接口
                    if(reStr!=null){
                        JSONObject json = JSONObject.parseObject(reStr);
                        if(json!=null){
                            if("200".equals(json.getString("code"))){
                                leaguerImportDao.deleteLeaguerImport(item.getId());
                            }else{
                                leaguerImportDao.updateLeaguerImport(item.getId());
                                log.info("该业务组:"+item.getBusinessNo()+"下,用户手机号:"+item.getMobilePhone()+"注册失败,错误信息:"+json.getString("msg"));
                            }
                        }else{
                            leaguerImportDao.updateLeaguerImport(item.getId());
                            log.info("该业务组:"+item.getBusinessNo()+"下,用户手机号:"+item.getMobilePhone()+"注册失败,接口异常!");
                        }
                    }else{
                        leaguerImportDao.updateLeaguerImport(item.getId());
                        log.info("该业务组:"+item.getBusinessNo()+"下,用户手机号:"+item.getMobilePhone()+"注册失败,接口异常!");
                    }
                }
            }
        }

        //清除大于等3的数据
        leaguerImportDao.deleteLeaguerImportByNum();
    }

    @Override
    public List<LeaguerInfo> findLeaguerInfoByUserNo(String key,int limit) {
        return leaguerDao.findLeaguerInfoByUserNo(key,limit);
    }

    /**
     * 会员注册接口
     * @param info
     * @return
     */
    private String insertVipInfoFace(LeaguerImport info,String digestKey){
        String returnStr=null;
        String baseUrl = sysConfigService.getByKey("JF_SERVICE_URL");
        if(StringUtils.isNotBlank(baseUrl)){
            String url=baseUrl+"/vipinfo/insertVipInfo";
            Map<String,Object> dataMap = new HashMap<String,Object>();
            dataMap.put("mobilePhone",info.getMobilePhone());
            dataMap.put("teamId",info.getBusinessNo());

            Map<String,Object> claims = new TreeMap<>();
            claims.put("businessNo",info.getBusinessNo());
            claims.put("data", JSONObject.toJSONString(dataMap));
            claims.put("timestamp", new Date().getTime());
            String sign = HttpclientInterface.sign(digestKey,claims);
            claims.put("sign",sign);

            log.info("会员注册接口请求,请求路径:{},参数：{}",url, JSONObject.toJSONString(claims));
            returnStr = HttpclientInterface.httpPost(url, claims);
            log.info("会员注册接口请求,返回结果：{}", returnStr);
        }
        return returnStr;
    }
}
