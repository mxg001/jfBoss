package cn.eeepay.framework.model.activity;

import java.util.Date;

/**
 * 中奖记录
 * 对应表 vip_lottery_record
 */
public class WinningRecord {

    private Long id;
    private String lotteryNo;//订单号
    private String lotteryCode;//中奖码
    private String actDetCode;//活动明细编号
    private String awardCode;//关联奖项
    private String actCode;//关联活动
    private String lotteryStatus;//中奖状态 1 已下发 0 未下发
    private String leaguerNo;//会员编号
    private String awardName;//奖品名称
    private String awardDesc;//奖品说明
    private String awardImg;//奖品图片
    private Integer expireDate;//有效期 天数
    private String ip;
    private Date createTime;//创建时间
    private Date createTimeBegin;
    private Date createTimeEnd;

    private String originUserNo;//来源用户编号
    private String mobilePhone;//用户手机号

    private String actName;//活动名称
    private String leaguerName;//会员名称


    private Integer pageFirst;//每页开始第几行
    private Integer pageSize;//每页大小
    private String businessNoFilter;//权限数据过滤

    public String getLeaguerName() {
        return leaguerName;
    }

    public void setLeaguerName(String leaguerName) {
        this.leaguerName = leaguerName;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
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

    public String getOriginUserNo() {
        return originUserNo;
    }

    public void setOriginUserNo(String originUserNo) {
        this.originUserNo = originUserNo;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLotteryNo() {
        return lotteryNo;
    }

    public void setLotteryNo(String lotteryNo) {
        this.lotteryNo = lotteryNo;
    }

    public String getLotteryCode() {
        return lotteryCode;
    }

    public void setLotteryCode(String lotteryCode) {
        this.lotteryCode = lotteryCode;
    }

    public String getActDetCode() {
        return actDetCode;
    }

    public void setActDetCode(String actDetCode) {
        this.actDetCode = actDetCode;
    }

    public String getAwardCode() {
        return awardCode;
    }

    public void setAwardCode(String awardCode) {
        this.awardCode = awardCode;
    }

    public String getActCode() {
        return actCode;
    }

    public void setActCode(String actCode) {
        this.actCode = actCode;
    }

    public String getLotteryStatus() {
        return lotteryStatus;
    }

    public void setLotteryStatus(String lotteryStatus) {
        this.lotteryStatus = lotteryStatus;
    }

    public String getLeaguerNo() {
        return leaguerNo;
    }

    public void setLeaguerNo(String leaguerNo) {
        this.leaguerNo = leaguerNo;
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

    public String getAwardImg() {
        return awardImg;
    }

    public void setAwardImg(String awardImg) {
        this.awardImg = awardImg;
    }

    public Integer getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Integer expireDate) {
        this.expireDate = expireDate;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
