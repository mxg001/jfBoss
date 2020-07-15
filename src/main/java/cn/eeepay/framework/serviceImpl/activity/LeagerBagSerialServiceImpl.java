package cn.eeepay.framework.serviceImpl.activity;

import cn.eeepay.framework.dao.activity.LeagerBagSerialDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.activity.LeagerBagSerial;
import cn.eeepay.framework.model.integral.BusinessFlow;
import cn.eeepay.framework.model.sys.UserLoginInfo;
import cn.eeepay.framework.service.activity.LeagerBagSerialService;
import cn.eeepay.framework.service.activity.LeaguerBlacklistService;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class LeagerBagSerialServiceImpl implements LeagerBagSerialService {

    Logger log = LoggerFactory.getLogger(LeaguerBlacklistServiceImpl.class);

    @Resource
    private LeagerBagSerialDao leagerBagSerialDao;

    @Override
    public List<LeagerBagSerial> findListByPage(LeagerBagSerial info, Page<LeagerBagSerial> page) {
        return leagerBagSerialDao.findListByPage(info,page);
    }

    @Override
    public void importLeagerBagSerial(LeagerBagSerial info, HttpServletResponse response) throws Exception {
        List<LeagerBagSerial> list=leagerBagSerialDao.findList(info);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "全民活动出入账列表"+sdf.format(new Date())+".xlsx" ;
        String fileNameFormat = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        Map<String,String> accTypeMap = new HashMap<>();
        accTypeMap.put("1","返现");
        accTypeMap.put("2","提现");
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("id",null);
            maps.put("accType",null);
            maps.put("actName",null);
            maps.put("accAmount",null);
            maps.put("leaguerNo",null);
            maps.put("originUserNo",null);
            maps.put("leaguerName",null);
            maps.put("mobilePhone",null);
            maps.put("createTime",null);
            data.add(maps);
        }else{
            for (LeagerBagSerial item : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("id",item.getId().toString());
                maps.put("accType",accTypeMap.get(item.getAccType()));
                maps.put("actName",item.getActName());
                maps.put("accAmount",item.getAccAmount() == null ? "0.00" : item.getAccAmount().toString());
                maps.put("leaguerNo",item.getLeaguerNo());
                maps.put("originUserNo",item.getOriginUserNo());
                maps.put("leaguerName",item.getLeaguerName());
                maps.put("mobilePhone",item.getMobilePhone());
                maps.put("createTime", item.getCreateTime()==null?"":sdf1.format(item.getCreateTime()));

                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"id","accType","actName","accAmount",
                "leaguerNo","originUserNo","leaguerName","mobilePhone","createTime"
        };
        String[] colsName = new String[]{"序号","出入类型","活动名称","金额","会员编号",
                "用户编号","用户名称","手机号","处理时间"
        };
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            e.printStackTrace();
            log.error("导出全民活动出入账列表异常!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }

}
