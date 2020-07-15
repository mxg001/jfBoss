package cn.eeepay.framework.model.integral;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2019/7/15/015.
 * @author liuks
 * 业务流水
 * 对应表  主表vip_leaguer_task_order   子表vip_task_reward_info
 */
public class BusinessFlow {

    private Long id;
    private String rewardOrderNo;//权益流水号
    private String leaguerNo;//用户编号
    private String taskOrderNo;//任务订单编号
    private String serviceNo;//任务编号
    private String serviceName;//任务名称
    private String rewardNo;//权益编号
    private String rewardType;//权益类型 1 积分 2 会员等级 3 M值 4鼓励金 5抵用金 6满溢金 7现金
    private String rewardStatus;//权益发放状态 0 未发放  1 已发放
    private String calcType;//计算模板 1 单笔 2 公式  3 模板
    private BigDecimal originNum;//触发值（比如基础增长 交易金额）
    private BigDecimal rewardNum;//权益数量
    private String rewardTemplate;//权益模板（ 固定数量  公式  模板）
    private String fromType;//权益来源类型 1 任务 2 游戏 3 兑换
    private String fromSystem;//权益来源系统
    private String fromDesc;//权益来源描述
    private Integer notifySure;//是否通知 0 否  1 是
    private Integer notifyStatus;//通知状态 0 未通知 1 通知成功 2 通知失败
    private Date notifyTime;//通知时间
    private String notifyData;//通知数据 （url+data）
    private Integer notifyNum;//通知次数
    private Date lastUpdateTime;//最后更新时间
    private Date createTime;//创建时间
    private Date createTimeBegin;
    private Date createTimeEnd;

    private Integer pageFirst;//每页开始第几行
    private Integer pageSize;//每页大小
    private String businessNoFilter;//权限数据过滤

    private String leaguerName;//会员名称
    private String mobilePhone;//会员手机号
    private String originUserNo;//来源用户编号
    private String businessNo;//业务组编号
    private String businessName;
    private String rewardName;//权益名称
    private String  originOrderNo;//任务原始订单号 关联业务系统

    private String digestKey;//业务组验签加密

    private String calcTypeName;//类型 物品名称

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRewardOrderNo() {
        return rewardOrderNo;
    }

    public void setRewardOrderNo(String rewardOrderNo) {
        this.rewardOrderNo = rewardOrderNo;
    }

    public String getLeaguerNo() {
        return leaguerNo;
    }

    public void setLeaguerNo(String leaguerNo) {
        this.leaguerNo = leaguerNo;
    }

    public String getTaskOrderNo() {
        return taskOrderNo;
    }

    public void setTaskOrderNo(String taskOrderNo) {
        this.taskOrderNo = taskOrderNo;
    }

    public String getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(String serviceNo) {
        this.serviceNo = serviceNo;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getRewardNo() {
        return rewardNo;
    }

    public void setRewardNo(String rewardNo) {
        this.rewardNo = rewardNo;
    }

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

    public String getRewardStatus() {
        return rewardStatus;
    }

    public void setRewardStatus(String rewardStatus) {
        this.rewardStatus = rewardStatus;
    }

    public String getCalcType() {
        return calcType;
    }

    public void setCalcType(String calcType) {
        this.calcType = calcType;
    }

    public BigDecimal getOriginNum() {
        return originNum;
    }

    public void setOriginNum(BigDecimal originNum) {
        this.originNum = originNum;
    }

    public BigDecimal getRewardNum() {
        return rewardNum;
    }

    public void setRewardNum(BigDecimal rewardNum) {
        this.rewardNum = rewardNum;
    }

    public String getRewardTemplate() {
        return rewardTemplate;
    }

    public void setRewardTemplate(String rewardTemplate) {
        this.rewardTemplate = rewardTemplate;
    }

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }

    public String getFromSystem() {
        return fromSystem;
    }

    public void setFromSystem(String fromSystem) {
        this.fromSystem = fromSystem;
    }

    public String getFromDesc() {
        return fromDesc;
    }

    public void setFromDesc(String fromDesc) {
        this.fromDesc = fromDesc;
    }

    public Integer getNotifySure() {
        return notifySure;
    }

    public void setNotifySure(Integer notifySure) {
        this.notifySure = notifySure;
    }

    public Integer getNotifyStatus() {
        return notifyStatus;
    }

    public void setNotifyStatus(Integer notifyStatus) {
        this.notifyStatus = notifyStatus;
    }

    public Date getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(Date notifyTime) {
        this.notifyTime = notifyTime;
    }

    public String getNotifyData() {
        return notifyData;
    }

    public void setNotifyData(String notifyData) {
        this.notifyData = notifyData;
    }

    public Integer getNotifyNum() {
        return notifyNum;
    }

    public void setNotifyNum(Integer notifyNum) {
        this.notifyNum = notifyNum;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(Date createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public Date getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
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

    public String getLeaguerName() {
        return leaguerName;
    }

    public void setLeaguerName(String leaguerName) {
        this.leaguerName = leaguerName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getOriginUserNo() {
        return originUserNo;
    }

    public void setOriginUserNo(String originUserNo) {
        this.originUserNo = originUserNo;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public String getOriginOrderNo() {
        return originOrderNo;
    }

    public void setOriginOrderNo(String originOrderNo) {
        this.originOrderNo = originOrderNo;
    }

    public String getDigestKey() {
        return digestKey;
    }

    public void setDigestKey(String digestKey) {
        this.digestKey = digestKey;
    }

    public String getCalcTypeName() {
        return calcTypeName;
    }

    public void setCalcTypeName(String calcTypeName) {
        this.calcTypeName = calcTypeName;
    }
}
