package cn.eeepay.framework.model.activity;

import java.util.Date;

/**
 * 达标条件表
 */
public class ConditionForStandard {

    private Long id;
    private String actDetCode;
    private String behavior;
    private String num_range_min;
    private String num_range_max;
    private Integer times;
    private Date createTime;
    private String extend;
    private String remark;


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

    public String getBehavior() {
        return behavior;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }

    public String getNum_range_min() {
        return num_range_min;
    }

    public void setNum_range_min(String num_range_min) {
        this.num_range_min = num_range_min;
    }

    public String getNum_range_max() {
        return num_range_max;
    }

    public void setNum_range_max(String num_range_max) {
        this.num_range_max = num_range_max;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
