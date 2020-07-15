package cn.eeepay.framework.service.integralMall;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.integralMall.GoodsManagement;

import java.util.List;

public interface GoodsManagementService {

    List<GoodsManagement> getSelectPageList(GoodsManagement info, Page<GoodsManagement> page);

    int addGood(GoodsManagement info);

    int deleteGood(Long id,String businessNo);

    int updateGood(GoodsManagement info);

    List<GoodsManagement> getAllGoodsType(GoodsManagement info);

    List<GoodsManagement> getGoodsList(String goodsType, String businessNo);

    GoodsManagement getGoodsByCode(String goodsCode);

    GoodsManagement getGoodInfoById(Long id, String businessNo);
}
