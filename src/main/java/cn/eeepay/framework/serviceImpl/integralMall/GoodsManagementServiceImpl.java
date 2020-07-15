package cn.eeepay.framework.serviceImpl.integralMall;

import cn.eeepay.framework.dao.integralMall.GoodsManagementDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.integralMall.GoodsManagement;
import cn.eeepay.framework.model.sys.UserLoginInfo;
import cn.eeepay.framework.service.integralMall.GoodsManagementService;
import cn.eeepay.framework.service.sys.RandomNumService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("goodsManagementService")
public class GoodsManagementServiceImpl implements GoodsManagementService {

    @Resource
    private GoodsManagementDao goodsManagementDao;

    @Resource
    private RandomNumService randomNumService;

    @Override
    public List<GoodsManagement> getSelectPageList(GoodsManagement info, Page<GoodsManagement> page) {
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setBusinessNoFilter(principal.getBusinessNos());
        return goodsManagementDao.getSelectPageList(info,page);
    }

    @Transactional(value = "transactionManager")
    @Override
    public int addGood(GoodsManagement info) {
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setOperator(principal.getUsername());
        info.setGoodsCode(randomNumService.getRandomNumberByData("G","vipScore",5));
        return goodsManagementDao.addGood(info);
    }

    @Transactional(value = "transactionManager")
    @Override
    public int deleteGood(Long id,String businessNo) {
        return goodsManagementDao.deleteGood(id,businessNo);
    }

    @Transactional(value = "transactionManager")
    @Override
    public int updateGood(GoodsManagement info) {
        return goodsManagementDao.updateGood(info);
    }

    @Override
    public List<GoodsManagement> getAllGoodsType(GoodsManagement info) {
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setBusinessNoFilter(principal.getBusinessNos());
        return goodsManagementDao.getAllGoodsType(info);
    }

    @Override
    public List<GoodsManagement> getGoodsList(String goodsType, String businessNo) {
        GoodsManagement info=new GoodsManagement();
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setBusinessNoFilter(principal.getBusinessNos());
        info.setGoodsType(goodsType);
        info.setBusinessNo(businessNo);
        return goodsManagementDao.getGoodsList(info);
    }

    @Override
    public GoodsManagement getGoodsByCode(String goodsCode) {
        return goodsManagementDao.getGoodsByCode(goodsCode);
    }

    @Override
    public GoodsManagement getGoodInfoById(Long id, String businessNo) {
        return goodsManagementDao.getGoodInfoById(id,businessNo);
    }
}
