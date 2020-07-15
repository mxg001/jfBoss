package cn.eeepay.framework.serviceImpl.integral;

import cn.eeepay.framework.dao.integral.PopupDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.integral.PopupConfig;
import cn.eeepay.framework.service.integral.PopupService;
import cn.eeepay.framework.util.CommonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PopupServiceImpl implements PopupService {

    @Resource
    private PopupDao popupDao;

    @Override
    public int add(PopupConfig popupConfig) {
        return popupDao.add(popupConfig);
    }

    @Override
    public int edit(PopupConfig popupConfig) {
        return popupDao.edit(popupConfig);
    }

    @Override
    public List<PopupConfig> selectWithPage(Page<PopupConfig> page) {
        return popupDao.selectWithPage(page);
    }

    @Override
    public int delete(String id) {
        return popupDao.delete(id);
    }

    @Override
    public PopupConfig selectById(int id) {
        PopupConfig info = popupDao.selectById(id);
        if(info!=null){
            info.setPopImgUrl(CommonUtil.getImgUrlAgent(info.getPopImg()));
        }
        return info;
    }
}
