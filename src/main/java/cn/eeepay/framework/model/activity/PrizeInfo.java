package cn.eeepay.framework.model.activity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 抽奖奖项配置表
 * 对应表 vip_lottery_award_config
 */
public class PrizeInfo {

    private Long id;
    private String actDetCode;//活动明细编号
    private String actCode;//活动编码
    private String awardCode;//奖品编号 AR 开头
    private String awardName;//奖品名称
    private String awardDesc;//奖品说明
    private Integer seqNo;//序列号
    private Integer awardNum;//奖品数量
    private Integer awardGrantNum;//已经抽取奖品数量
    private BigDecimal awardOdds;//中奖概率
    private Integer dayNums;//每日最多下发（总数）
    private Integer dayMax;//每天最多中奖（单个用户）
    private Integer dayAwardGrant;//每日已经抽取奖品数量
    private String openStatus;//是否打开 0否,1打开
    private String awardImg;//奖品图片
    private Date createTime;//创建时间
    private String isDelete;//是否删除 0 未删除 1 删除
    private String awardNotice;//中奖提示语

    private ActivityReward prize;//奖品,目前是只支持单个

    private Integer numSurplus;//总数剩余
    private Integer dayNumSurplus;//单日剩余

    private Integer openStatusInt;

    private String isShot;//0 未中奖,1中奖

    private String oriAwardCode;//关联模板原奖品编号

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

    public String getAwardCode() {
        return awardCode;
    }

    public void setAwardCode(String awardCode) {
        this.awardCode = awardCode;
    }

    public String getAwardName() {
        return awardName;
    }

    public void setAwardName(String awardName) {
        this.awardName = awardName;
    }

    public String getAwardDesc() {
        return awardDesc;
    }

    public void setAwardDesc(String awardDesc) {
        this.awardDesc = awardDesc;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public Integer getAwardNum() {
        return awardNum;
    }

    public void setAwardNum(Integer awardNum) {
        this.awardNum = awardNum;
    }

    public Integer getAwardGrantNum() {
        return awardGrantNum;
    }

    public void setAwardGrantNum(Integer awardGrantNum) {
        this.awardGrantNum = awardGrantNum;
    }

    public BigDecimal getAwardOdds() {
        return awardOdds;
    }

    public void setAwardOdds(BigDecimal awardOdds) {
        this.awardOdds = awardOdds;
    }

    public Integer getDayNums() {
        return dayNums;
    }

    public void setDayNums(Integer dayNums) {
        this.dayNums = dayNums;
    }

    public Integer getDayMax() {
        return dayMax;
    }

    public void setDayMax(Integer dayMax) {
        this.dayMax = dayMax;
    }

    public Integer getDayAwardGrant() {
        return dayAwardGrant;
    }

    public void setDayAwardGrant(Integer dayAwardGrant) {
        this.dayAwardGrant = dayAwardGrant;
    }

    public String getOpenStatus() {
        return openStatus;
    }

    public void setOpenStatus(String openStatus) {
        this.openStatus = openStatus;
    }

    public String getAwardImg() {
        return awardImg;
    }

    public void setAwardImg(String awardImg) {
        this.awardImg = awardImg;
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

    public ActivityReward getPrize() {
        return prize;
    }

    public void setPrize(ActivityReward prize) {
        this.prize = prize;
    }

    public String getAwardNotice() {
        return awardNotice;
    }

    public void setAwardNotice(String awardNotice) {
        this.awardNotice = awardNotice;
    }

    public Integer getNumSurplus() {
        return numSurplus;
    }

    public void setNumSurplus(Integer numSurplus) {
        this.numSurplus = numSurplus;
    }

    public Integer getDayNumSurplus() {
        return dayNumSurplus;
    }

    public void setDayNumSurplus(Integer dayNumSurplus) {
        this.dayNumSurplus = dayNumSurplus;
    }

    public Integer getOpenStatusInt() {
        return openStatusInt;
    }

    public void setOpenStatusInt(Integer openStatusInt) {
        this.openStatusInt = openStatusInt;
    }

    public String getIsShot() {
        return isShot;
    }

    public void setIsShot(String isShot) {
        this.isShot = isShot;
    }

    public String getOriAwardCode() {
        return oriAwardCode;
    }

    public void setOriAwardCode(String oriAwardCode) {
        this.oriAwardCode = oriAwardCode;
    }
}
