package cn.eeepay.framework.model.activity;

import java.util.Date;

/**
 * 适用访问部分名单
 */
public class RangeName {

    private Long id;
    private String actDetCode;//活动明细编号
    private String actCode;//关联活动表
    private String leaguerNo;//用户编号
    private Date createTime;//创建时间
    private String operator;//操作人

    private String mobilePhone;//手机号
    private String businessNo;//业务组
    private String businessName;//业务组名称
    private String actName;//活动名称
    private String originUserNo;//来源用户
    private String leaguerName;//会员名称


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

    public String getLeaguerNo() {
        return leaguerNo;
    }

    public void setLeaguerNo(String leaguerNo) {
        this.leaguerNo = leaguerNo;
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

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
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

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public String getOriginUserNo() {
        return originUserNo;
    }

    public void setOriginUserNo(String originUserNo) {
        this.originUserNo = originUserNo;
    }

    public String getLeaguerName() {
        return leaguerName;
    }

    public void setLeaguerName(String leaguerName) {
        this.leaguerName = leaguerName;
    }
}
