package cn.eeepay.framework.model.activity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 活动明细奖励配置
 * 对应表 vip_activity_reward
 *
 */
public class ActivityReward {

    private Long id;
    private String actRewardNo;//活动奖励权益编号 AR 开头
    private String linkCode;//关联编码（活动明细编码，或者是抽奖奖项编码）
    private BigDecimal standardAmount;//达标值（交易奖励类型不填）
    private String rewardType;//奖励类型 关联数据字典  REWARD_TYPE,未中奖99
    private String calcType;//值类型 计算类型 1 数值
    private String awardNum;//奖励数量（奖励模板）
    private String awardTemplateFormula;//计算公式
    private Integer expireDay;//有效期
    private Integer status;//状态 1 正常
    private String isDelete;//是否删除 0 未删除 1 删除
    private Date createTime;//创建时间

    private String goodName;//物品名称
    private String rewardName;//奖励类型

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActRewardNo() {
        return actRewardNo;
    }

    public void setActRewardNo(String actRewardNo) {
        this.actRewardNo = actRewardNo;
    }

    public String getLinkCode() {
        return linkCode;
    }

    public void setLinkCode(String linkCode) {
        this.linkCode = linkCode;
    }

    public BigDecimal getStandardAmount() {
        return standardAmount;
    }

    public void setStandardAmount(BigDecimal standardAmount) {
        this.standardAmount = standardAmount;
    }

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

    public String getCalcType() {
        return calcType;
    }

    public void setCalcType(String calcType) {
        this.calcType = calcType;
    }

    public String getAwardNum() {
        return awardNum;
    }

    public void setAwardNum(String awardNum) {
        this.awardNum = awardNum;
    }

    public String getAwardTemplateFormula() {
        return awardTemplateFormula;
    }

    public void setAwardTemplateFormula(String awardTemplateFormula) {
        this.awardTemplateFormula = awardTemplateFormula;
    }

    public Integer getExpireDay() {
        return expireDay;
    }

    public void setExpireDay(Integer expireDay) {
        this.expireDay = expireDay;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

}
