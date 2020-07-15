package cn.eeepay.framework.model.integralMall;

import java.util.Date;

/**
 * 物品管理
 * 对应表 vip_goods_config
 */
public class GoodsManagement {

    private Long id;
    private String goodsCode;//物品编码
    private String goodsName;//物品名称
    private String goodsType;//'物品类型 1、鼓励金、2 抵用金、3满溢金'
    private String businessNo;//业务组
    private String originId;//券原始id
    private Date createTime;//创建时间
    private String operator;//创建操作人
    private String goodsStatus;//状态 1正常
    private String isDelete;//逻辑删除控制 是否删除0 未删除 1 删除
    private String businessName;//业务组名称

    private Integer pageFirst;//每页开始第几行
    private Integer pageSize;//每页大小
    private String businessNoFilter;//权限数据过滤

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(String goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getPageFirst() {
        return pageFirst;
    }

    public void setPageFirst(Integer pageFirst) {
        this.pageFirst = pageFirst;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getBusinessNoFilter() {
        return businessNoFilter;
    }

    public void setBusinessNoFilter(String businessNoFilter) {
        this.businessNoFilter = businessNoFilter;
    }
}
