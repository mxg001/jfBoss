package cn.eeepay.framework.model.activity;

import java.math.BigDecimal;

public class PolicyDetail {

    private Long id;
    private BigDecimal awardNum;
    private Integer dayNums;
    private BigDecimal awardOdds;
    private String awardCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAwardNum() {
        return awardNum;
    }

    public void setAwardNum(BigDecimal awardNum) {
        this.awardNum = awardNum;
    }

    public Integer getDayNums() {
        return dayNums;
    }

    public void setDayNums(Integer dayNums) {
        this.dayNums = dayNums;
    }

    public BigDecimal getAwardOdds() {
        return awardOdds;
    }

    public void setAwardOdds(BigDecimal awardOdds) {
        this.awardOdds = awardOdds;
    }

    public String getAwardCode() {
        return awardCode;
    }

    public void setAwardCode(String awardCode) {
        this.awardCode = awardCode;
    }
}
