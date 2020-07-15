package cn.eeepay.framework.model.integral;

import java.util.Date;

/**
 * 会员导入模板实体
 * 对应表 vip_leaguer_import
 */
public class LeaguerImport {

    private Integer id;
    private String businessNo;//业务组编号
    private String mobilePhone;//用户手机号
    private String originUserNo;//来源用户编号
    private Integer failNum;//失败次数
    private String operator;//操作人
    private Date createTime;//创建时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
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

    public Integer getFailNum() {
        return failNum;
    }

    public void setFailNum(Integer failNum) {
        this.failNum = failNum;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
