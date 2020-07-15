package cn.eeepay.framework.model.integral;

import java.util.Date;

/**
 * @author ：quanhz
 * @date ：Created in 2019/11/27 17:19
 */
public class BusinessVipLevelInfo {
    private Long id;
    private String rewardName;//权益名称
    private String businessNo;//业务组编号
    private Integer level;//等级
    private Integer isSelect;//是否选中 0 未选中 1 选中
    private Date createTime;//创建时间
    private String rewardCode;//权益标识
    private String businessNoFilter;//权限数据过滤


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(Integer isSelect) {
        this.isSelect = isSelect;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRewardCode() {
        return rewardCode;
    }

    public void setRewardCode(String rewardCode) {
        this.rewardCode = rewardCode;
    }


    public String getBusinessNoFilter() {
        return businessNoFilter;
    }

    public void setBusinessNoFilter(String businessNoFilter) {
        this.businessNoFilter = businessNoFilter;
    }
}
