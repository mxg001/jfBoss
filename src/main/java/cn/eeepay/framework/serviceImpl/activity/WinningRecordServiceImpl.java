package cn.eeepay.framework.serviceImpl.activity;

import cn.eeepay.framework.dao.activity.WinningRecordDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.activity.WinningRecord;
import cn.eeepay.framework.model.sys.UserLoginInfo;
import cn.eeepay.framework.service.activity.WinningRecordService;
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

@Service("winningRecordService")
public class WinningRecordServiceImpl implements WinningRecordService {

    private static final Logger log = LoggerFactory.getLogger(WinningRecordServiceImpl.class);

    @Resource
    private WinningRecordDao winningRecordDao;

    @Override
    public List<WinningRecord> getSelectPageList(WinningRecord info, Page<WinningRecord> page) {
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setBusinessNoFilter(principal.getBusinessNos());
        List<WinningRecord> list =winningRecordDao.getSelectPageList(info,page);
        if(page.getResult()!=null&&page.getResult().size()>0){
            for(WinningRecord item:page.getResult()){
                item.setMobilePhone(StringUtil.sensitiveInformationHandle(item.getMobilePhone(),0));
            }
        }
        return list;
    }

    @Override
    public int getWinningRecordCount(WinningRecord info) {
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setBusinessNoFilter(principal.getBusinessNos());
        return winningRecordDao.getWinningRecordCount(info);
    }

    @Override
    public void importDetail(WinningRecord info, HttpServletResponse response) throws Exception{
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setBusinessNoFilter(principal.getBusinessNos());
        List<WinningRecord> list=winningRecordDao.importDetail(info);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;

        String fileName = "抽奖记录"+sdf.format(new Date())+".xlsx" ;
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
            Map<String, String> lotteryStatusMap=new HashMap<String, String>();//状态
            lotteryStatusMap.put("1","已中奖");
            lotteryStatusMap.put("2","已发奖");
            lotteryStatusMap.put("3","未中奖");

            for (WinningRecord item : list) {
                item.setMobilePhone(StringUtil.sensitiveInformationHandle(item.getMobilePhone(),0));

                Map<String, String> maps = new HashMap<String, String>();
                maps.put("id",item.getId()==null?"":item.getId().toString());
                maps.put("lotteryNo",item.getLeaguerNo());
                maps.put("lotteryCode",item.getLotteryCode());
                maps.put("awardDesc",item.getAwardDesc());
                maps.put("awardName",item.getAwardName());
                maps.put("lotteryStatus",lotteryStatusMap.get(item.getLotteryStatus()));
                maps.put("actName",item.getActName());
                maps.put("leaguerNo",item.getLeaguerNo());
                maps.put("originUserNo",item.getOriginUserNo());
                maps.put("leaguerName",item.getLeaguerName());
                maps.put("mobilePhone",item.getMobilePhone());
                maps.put("createTime", item.getCreateTime()==null?"":sdf1.format(item.getCreateTime()));
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"id","lotteryNo","lotteryCode","awardDesc","awardName","lotteryStatus",
                "actName","leaguerNo","originUserNo","leaguerName","mobilePhone","createTime"
        };
        String[] colsName = new String[]{"序号","编号","奖品码","奖项说明","奖品名称","状态",
                "活动名称","会员编号","用户编号","会员名称","手机号","抽奖时间"
        };
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            e.printStackTrace();
            log.error("导出抽奖记录异常!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }

    }
}
