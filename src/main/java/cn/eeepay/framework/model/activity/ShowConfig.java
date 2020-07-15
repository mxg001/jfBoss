package cn.eeepay.framework.model.activity;

import java.util.Date;

/**
 * 活动展示实体
 * 对应表 vip_lottery_show_config
 */
public class ShowConfig {

    private Integer id;
    private String configData;//配置值
    private String actCode;//活动编码
    private Date lastUpdateTime;
    private Date createTime;
    private String operater;

    private WinningList winningList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConfigData() {
        return configData;
    }

    public void setConfigData(String configData) {
        this.configData = configData;
    }

    public String getActCode() {
        return actCode;
    }

    public void setActCode(String actCode) {
        this.actCode = actCode;
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

    public String getOperater() {
        return operater;
    }

    public void setOperater(String operater) {
        this.operater = operater;
    }

    public WinningList getWinningList() {
        return winningList;
    }

    public void setWinningList(WinningList winningList) {
        this.winningList = winningList;
    }
}
