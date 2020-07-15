package cn.eeepay.framework.model.integral;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2019/7/11/011.
 * @author  liuks
 * 积分出入账流水表
 *
 */
public class ScoreAccountInfo {

    private Long id;
    private String accountSerialNo;//'出入账流水号'
    private String leaguerNo;//会员编号
    private String businessNo;//业务组编号
    private String serviceOrderNo;//'权益流水号'
    private BigDecimal accScore;//'记账积分'
    private BigDecimal beforeScore;//'记账前积分'
    private BigDecimal afterScore;//'记账后积分'
    private String accType;//积分出入账类型 1 入账 2 出账
    private String accStatus;//记账状态 1 初始化 2 成功  3 失败
    private String scoreBatchNo;//积分批次号
    private String fromType;//权益来源类型 1 任务 2 游戏 3 兑换
    private String fromSystem;//'权益来源系统'
    private String fromDesc;//'权益来源描述'
    private Date lastUpdateTime;//'最后更新时间'

    private Date createTime;//'创建时间'
    private Date createTimeBegin;
    private Date createTimeEnd;

    private Integer pageFirst;//每页开始第几行
    private Integer pageSize;//每页大小
    private String businessNoFilter;//权限数据过滤

    private String leaguerName;//会员名称
    private String mobilePhone;//会员手机号
    private String originUserNo;//来源用户编号
    private String businessName;//业务组名称

    private String operater;//操作人

    private Date scoreBatchDate;//积分批次号日期
    private String scoreBatchDateStr;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountSerialNo() {
        return accountSerialNo;
    }

    public void setAccountSerialNo(String accountSerialNo) {
        this.accountSerialNo = accountSerialNo;
    }

    public String getLeaguerNo() {
        return leaguerNo;
    }

    public void setLeaguerNo(String leaguerNo) {
        this.leaguerNo = leaguerNo;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getServiceOrderNo() {
        return serviceOrderNo;
    }

    public void setServiceOrderNo(String serviceOrderNo) {
        this.serviceOrderNo = serviceOrderNo;
    }

    public BigDecimal getAccScore() {
        return accScore;
    }

    public void setAccScore(BigDecimal accScore) {
        this.accScore = accScore;
    }

    public BigDecimal getBeforeScore() {
        return beforeScore;
    }

    public void setBeforeScore(BigDecimal beforeScore) {
        this.beforeScore = beforeScore;
    }

    public BigDecimal getAfterScore() {
        return afterScore;
    }

    public void setAfterScore(BigDecimal afterScore) {
        this.afterScore = afterScore;
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public String getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(String accStatus) {
        this.accStatus = accStatus;
    }

    public String getScoreBatchNo() {
        return scoreBatchNo;
    }

    public void setScoreBatchNo(String scoreBatchNo) {
        this.scoreBatchNo = scoreBatchNo;
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

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getOperater() {
        return operater;
    }

    public void setOperater(String operater) {
        this.operater = operater;
    }

    public Date getScoreBatchDate() {
        return scoreBatchDate;
    }

    public void setScoreBatchDate(Date scoreBatchDate) {
        this.scoreBatchDate = scoreBatchDate;
    }

    public String getScoreBatchDateStr() {
        return scoreBatchDateStr;
    }

    public void setScoreBatchDateStr(String scoreBatchDateStr) {
        this.scoreBatchDateStr = scoreBatchDateStr;
    }
}


