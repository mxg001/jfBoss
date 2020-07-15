package cn.eeepay.framework.model.activity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 激活活动内容配置
 * 对应表 vip_activity_detail
 */
public class ActivityDetail {

    private Long id;
    private String actDetCode;//活动明细编号AD 开头
    private String actCode;//活动编码(关联活动表)
    private String activeName;//活动内容标题 开头
    private String activeDesc;//活动说明
    private String expendType;//消耗类型 1 积分消耗 2 次数消耗
    private BigDecimal expendNum;//消耗数量
    private String actDetStatus;//明细执行状态，0：未执行，1：执行中，2执行完成
    private String activeType;//激活活动内容类型 1 奖励赠送 2 达标奖励 3 交易返现 4 抽奖
    private String awardMethod;//奖励方式 1 多选1 2 多选多 3 交易返现 4 手续费返现
    private Date createTime;//创建时间
    private String  isDelete;//是否删除 0 未删除 1 删除
    private String notice;//提示语
    private BigDecimal amountRangeMin;//消耗数量
    private Integer expendNumber;

    //会员活动礼遇季新增
    private String expiryDateType;     // 有效期类型 天：day；周：week；月：month；年：year；activity：活动周期；
    private String expiryNum;          //有效期值  （如果为空生成抽奖次数时 默认当天）
    private Integer allNum;             //总次数
    private Integer policyType;         //策略类型 1  概率随机   2   区间随机
    private BigDecimal sectionBegin;    //区间开始值
    private BigDecimal sectionEnd;      //区间结束值
    private Integer isLinkBehind;       //是否关联后置条件  1  关联  0  不关联    默认 0
    private Integer standardType;       //达标条件类型  1 且     2  或
    private BigDecimal blackReward;     //黑名单奖励金额

    //vo
    private List<PolicyDetail> policyList;   //如策略类型为1，则有该值
    private BigDecimal userTransAmount;
    private Integer userTransPersons;
    private Integer memberAwardNum;
    private Integer transAmountNum;

    private BigDecimal tradAmountUpdateValue;
    private Integer memberUpdateValue;
    private BigDecimal inviteFriendUpdateValue;

    private Integer openStatus;


    private List<ActivityReward> actReward;//内容奖品


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActDetCode() {
        return actDetCode;
    }

    public void setActDetCode(String actDetCode) {
        this.actDetCode = actDetCode;
    }

    public String getActCode() {
        return actCode;
    }

    public void setActCode(String actCode) {
        this.actCode = actCode;
    }

    public String getActiveName() {
        return activeName;
    }

    public void setActiveName(String activeName) {
        this.activeName = activeName;
    }

    public String getActiveDesc() {
        return activeDesc;
    }

    public void setActiveDesc(String activeDesc) {
        this.activeDesc = activeDesc;
    }

    public String getExpendType() {
        return expendType;
    }

    public void setExpendType(String expendType) {
        this.expendType = expendType;
    }

    public BigDecimal getExpendNum() {
        return expendNum;
    }

    public void setExpendNum(BigDecimal expendNum) {
        this.expendNum = expendNum;
    }

    public String getActDetStatus() {
        return actDetStatus;
    }

    public void setActDetStatus(String actDetStatus) {
        this.actDetStatus = actDetStatus;
    }

    public String getActiveType() {
        return activeType;
    }

    public void setActiveType(String activeType) {
        this.activeType = activeType;
    }

    public String getAwardMethod() {
        return awardMethod;
    }

    public void setAwardMethod(String awardMethod) {
        this.awardMethod = awardMethod;
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

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public Integer getExpendNumber() {
        return expendNumber;
    }

    public void setExpendNumber(Integer expendNumber) {
        this.expendNumber = expendNumber;
    }

    public List<ActivityReward> getActReward() {
        return actReward;
    }

    public void setActReward(List<ActivityReward> actReward) {
        this.actReward = actReward;
    }

    public BigDecimal getAmountRangeMin() {
        return amountRangeMin;
    }

    public void setAmountRangeMin(BigDecimal amountRangeMin) {
        this.amountRangeMin = amountRangeMin;
    }

    public String getExpiryDateType() {
        return expiryDateType;
    }

    public void setExpiryDateType(String expiryDateType) {
        this.expiryDateType = expiryDateType;
    }

    public String getExpiryNum() {
        return expiryNum;
    }

    public void setExpiryNum(String expiryNum) {
        this.expiryNum = expiryNum;
    }

    public Integer getAllNum() {
        return allNum;
    }

    public void setAllNum(Integer allNum) {
        this.allNum = allNum;
    }

    public Integer getPolicyType() {
        return policyType;
    }

    public void setPolicyType(Integer policyType) {
        this.policyType = policyType;
    }

    public BigDecimal getSectionBegin() {
        return sectionBegin;
    }

    public void setSectionBegin(BigDecimal sectionBegin) {
        this.sectionBegin = sectionBegin;
    }

    public BigDecimal getSectionEnd() {
        return sectionEnd;
    }

    public void setSectionEnd(BigDecimal sectionEnd) {
        this.sectionEnd = sectionEnd;
    }

    public Integer getIsLinkBehind() {
        return isLinkBehind;
    }

    public void setIsLinkBehind(Integer isLinkBehind) {
        this.isLinkBehind = isLinkBehind;
    }

    public Integer getStandardType() {
        return standardType;
    }

    public void setStandardType(Integer standardType) {
        this.standardType = standardType;
    }

    public BigDecimal getBlackReward() {
        return blackReward;
    }

    public void setBlackReward(BigDecimal blackReward) {
        this.blackReward = blackReward;
    }

    public List<PolicyDetail> getPolicyList() {
        return policyList;
    }

    public void setPolicyList(List<PolicyDetail> policyList) {
        this.policyList = policyList;
    }

    public BigDecimal getUserTransAmount() {
        return userTransAmount;
    }

    public void setUserTransAmount(BigDecimal userTransAmount) {
        this.userTransAmount = userTransAmount;
    }

    public Integer getUserTransPersons() {
        return userTransPersons;
    }

    public void setUserTransPersons(Integer userTransPersons) {
        this.userTransPersons = userTransPersons;
    }

    public Integer getMemberAwardNum() {
        return memberAwardNum;
    }

    public void setMemberAwardNum(Integer memberAwardNum) {
        this.memberAwardNum = memberAwardNum;
    }

    public Integer getTransAmountNum() {
        return transAmountNum;
    }

    public void setTransAmountNum(Integer transAmountNum) {
        this.transAmountNum = transAmountNum;
    }

    public BigDecimal getTradAmountUpdateValue() {
        return tradAmountUpdateValue;
    }

    public void setTradAmountUpdateValue(BigDecimal tradAmountUpdateValue) {
        this.tradAmountUpdateValue = tradAmountUpdateValue;
    }

    public Integer getMemberUpdateValue() {
        return memberUpdateValue;
    }

    public void setMemberUpdateValue(Integer memberUpdateValue) {
        this.memberUpdateValue = memberUpdateValue;
    }

    public BigDecimal getInviteFriendUpdateValue() {
        return inviteFriendUpdateValue;
    }

    public void setInviteFriendUpdateValue(BigDecimal inviteFriendUpdateValue) {
        this.inviteFriendUpdateValue = inviteFriendUpdateValue;
    }

    public Integer getOpenStatus() {
        return openStatus;
    }

    public void setOpenStatus(Integer openStatus) {
        this.openStatus = openStatus;
    }
}
