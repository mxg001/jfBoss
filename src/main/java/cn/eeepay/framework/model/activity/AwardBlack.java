package cn.eeepay.framework.model.activity;

import java.util.Date;

/**
 * 奖项黑名单
 */
public class AwardBlack {

    private Long id;
    private String awardCode;//奖品编码
    private String leaguerNo;//会员编号
    private Date createTime;//创建时间
    private String operator;//创建人
    private String leaguerName;//会员名称
    private String originUserNo;//用户编号

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAwardCode() {
        return awardCode;
    }

    public void setAwardCode(String awardCode) {
        this.awardCode = awardCode;
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

    public String getLeaguerName() {
        return leaguerName;
    }

    public void setLeaguerName(String leaguerName) {
        this.leaguerName = leaguerName;
    }

    public String getOriginUserNo() {
        return originUserNo;
    }

    public void setOriginUserNo(String originUserNo) {
        this.originUserNo = originUserNo;
    }
}
