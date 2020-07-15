package cn.eeepay.framework.serviceImpl.activity;

import cn.eeepay.framework.dao.activity.ActivityCensusDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.activity.ActivityCensus;
import cn.eeepay.framework.model.sys.UserLoginInfo;
import cn.eeepay.framework.service.activity.ActivityCensusService;
import cn.eeepay.framework.util.ListDataExcelExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("activityCensusService")
public class ActivityCensusServiceImpl implements ActivityCensusService {

    private static final Logger log = LoggerFactory.getLogger(ActivityCensusServiceImpl.class);

    @Resource
    private ActivityCensusDao  activityCensusDao;

    @Override
    public List<ActivityCensus> getSelectPageList(ActivityCensus info, Page<ActivityCensus> page) {
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setBusinessNoFilter(principal.getBusinessNos());
        return activityCensusDao.getSelectPageList(info,page);
    }

    @Override
    public void importDetail(ActivityCensus info, HttpServletResponse response) throws Exception{

        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setBusinessNoFilter(principal.getBusinessNos());
        List<ActivityCensus> list=activityCensusDao.importDetail(info);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd") ;

        String fileName = "活动数据统计"+sdf.format(new Date())+".xlsx" ;
        String fileNameFormat = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("countDate", null);
            maps.put("actName",null);
            maps.put("ipCount",null);
            maps.put("requestNum",null);
            maps.put("countActNum",null);
            maps.put("countUser",null);
            data.add(maps);
        }else{

            for (ActivityCensus item : list) {
                Map<String, String> maps = new HashMap<String, String>();

                maps.put("countDate", item.getCountDate()==null?"":sdf2.format(item.getCountDate()));
                maps.put("actName",item.getActName());
                maps.put("ipCount",item.getIpCount()==null?"":item.getIpCount().toString());
                maps.put("requestNum",item.getRequestNum()==null?"":item.getRequestNum().toString());
                maps.put("countActNum",item.getCountActNum()==null?"":item.getCountActNum().toString());
                maps.put("countUser",item.getCountUser()==null?"":item.getCountUser().toString());
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"countDate","actName","ipCount","requestNum","countActNum","countUser"};
        String[] colsName = new String[]{"时间","活动名称","独立访问","访问量","参与人次","参与会员"};
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            e.printStackTrace();
            log.error("导出活动数据统计异常!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }
}
