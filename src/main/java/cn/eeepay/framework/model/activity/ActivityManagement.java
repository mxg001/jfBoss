package cn.eeepay.framework.model.activity;

import java.util.Date;
import java.util.List;

public class ActivityManagement {

    private Long id;
    private String businessNo;//业务组编号 默认模板 此字段标记 0
    private String actCode;//活动编码(活动唯一)
    private String  oriActCode;//关联活动编码(模板活动编码)
    private String actName;//活动名称
    private String actDesc;//活动说明
    private Integer seqNo;//序列号
    private String awardDesc;//奖项说明
    private String actRule;//活动规则(抽奖的活动介绍)
    private String actLink;//网页链接
    private String openStatus;//是否打开0 否 1 是
    private String showStatus;//是否首页显示
    private Date actBeginTime;//活动开始时间
    private Date actEndTime;//活动结束时间
    private String actRangeType;//活动范围 0全部 1部分
    private String actType;//活动类型 1：用户激活，2：积分抽奖
    private Date createTime;//创建时间
    private String  isDelete;//是否删除 0 未删除 1 删除
    private String  actImg;//活动图片

    private Integer pageFirst;//每页开始第几行
    private Integer pageSize;//每页大小
    private String businessNoFilter;//权限数据过滤

    private List<PrizeInfo> prizeInfoList;//抽奖奖项配置
    private ActivityDetail luckDrawpDetail;//抽奖详情

    private List<ActivityDetail> actiDetailList;//激活活动内容列表

    private Integer openStatusInt;//是否打开0 否 1 是
    private Integer showStatusInt;//是否首页显示

    private Date withdrawalTimeEnd;     //提现截止时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getActCode() {
        return actCode;
    }

    public void setActCode(String actCode) {
        this.actCode = actCode;
    }

    public String getOriActCode() {
        return oriActCode;
    }

    public void setOriActCode(String oriActCode) {
        this.oriActCode = oriActCode;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public String getActDesc() {
        return actDesc;
    }

    public void setActDesc(String actDesc) {
        this.actDesc = actDesc;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public String getAwardDesc() {
        return awardDesc;
    }

    public void setAwardDesc(String awardDesc) {
        this.awardDesc = awardDesc;
    }

    public String getActRule() {
        return actRule;
    }

    public void setActRule(String actRule) {
        this.actRule = actRule;
    }

    public String getActLink() {
        return actLink;
    }

    public void setActLink(String actLink) {
        this.actLink = actLink;
    }

    public String getOpenStatus() {
        return openStatus;
    }

    public void setOpenStatus(String openStatus) {
        this.openStatus = openStatus;
    }

    public String getShowStatus() {
        return showStatus;
    }

    public void setShowStatus(String showStatus) {
        this.showStatus = showStatus;
    }

    public Date getActBeginTime() {
        return actBeginTime;
    }

    public void setActBeginTime(Date actBeginTime) {
        this.actBeginTime = actBeginTime;
    }

    public Date getActEndTime() {
        return actEndTime;
    }

    public void setActEndTime(Date actEndTime) {
        this.actEndTime = actEndTime;
    }

    public String getActRangeType() {
        return actRangeType;
    }

    public void setActRangeType(String actRangeType) {
        this.actRangeType = actRangeType;
    }

    public String getActType() {
        return actType;
    }

    public void setActType(String actType) {
        this.actType = actType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public List<PrizeInfo> getPrizeInfoList() {
        return prizeInfoList;
    }

    public void setPrizeInfoList(List<PrizeInfo> prizeInfoList) {
        this.prizeInfoList = prizeInfoList;
    }

    public ActivityDetail getLuckDrawpDetail() {
        return luckDrawpDetail;
    }

    public void setLuckDrawpDetail(ActivityDetail luckDrawpDetail) {
        this.luckDrawpDetail = luckDrawpDetail;
    }

    public List<ActivityDetail> getActiDetailList() {
        return actiDetailList;
    }

    public void setActiDetailList(List<ActivityDetail> actiDetailList) {
        this.actiDetailList = actiDetailList;
    }

    public String getActImg() {
        return actImg;
    }

    public void setActImg(String actImg) {
        this.actImg = actImg;
    }

    public Integer getOpenStatusInt() {
        return openStatusInt;
    }

    public void setOpenStatusInt(Integer openStatusInt) {
        this.openStatusInt = openStatusInt;
    }

    public Integer getShowStatusInt() {
        return showStatusInt;
    }

    public void setShowStatusInt(Integer showStatusInt) {
        this.showStatusInt = showStatusInt;
    }

    public Date getWithdrawalTimeEnd() {
        return withdrawalTimeEnd;
    }

    public void setWithdrawalTimeEnd(Date withdrawalTimeEnd) {
        this.withdrawalTimeEnd = withdrawalTimeEnd;
    }
}
