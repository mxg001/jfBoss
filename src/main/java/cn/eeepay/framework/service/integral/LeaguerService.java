package cn.eeepay.framework.service.integral;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.integral.LeaguerInfo;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/7/10/010.
 */
public interface LeaguerService {


    List<LeaguerInfo> getSelectPageList(LeaguerInfo info, Page<LeaguerInfo> page);

    int getSelectPageListCount(LeaguerInfo info);

    /***
     *
     * @param id
     * @param businessNo
     * @param msg
     * @param showSensitive 是否显示敏感信息 1显示 0不显示
     * @return
     */
    LeaguerInfo getLeaguerInfo(Long id, String businessNo, Map<String, Object> msg,int showSensitive);

    void importDetail(LeaguerInfo info, HttpServletResponse response) throws Exception;

    LeaguerInfo checkLeaguerInfo(String strNo,String businessNo);

    Map<String, Object> importDiscount(MultipartFile file,String businessNo)  throws Exception;

    void leaguerImportJob();

    List<LeaguerInfo> findLeaguerInfoByUserNo(String key,int limit);

}
