package cn.eeepay.framework.model.integral;

import java.util.Date;

/**
 * Created by Administrator on 2019/7/10/010.
 * @author  liuks
 * 会员成长记录 vip_leaguer_grow_info
 */
public class LeaguerGrow {

    private Long id;
    private String leaguer_no;//会员编号
    private Integer beforeLevel;//成长前等级
    private Integer afterLevel;//成长后等级

    private String beforeLevelName;
    private String afterLevelName;

    private Date createTime;//创建时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLeaguer_no() {
        return leaguer_no;
    }

    public void setLeaguer_no(String leaguer_no) {
        this.leaguer_no = leaguer_no;
    }

    public Integer getBeforeLevel() {
        return beforeLevel;
    }

    public void setBeforeLevel(Integer beforeLevel) {
        this.beforeLevel = beforeLevel;
    }

    public Integer getAfterLevel() {
        return afterLevel;
    }

    public void setAfterLevel(Integer afterLevel) {
        this.afterLevel = afterLevel;
    }

    public String getBeforeLevelName() {
        return beforeLevelName;
    }

    public void setBeforeLevelName(String beforeLevelName) {
        this.beforeLevelName = beforeLevelName;
    }

    public String getAfterLevelName() {
        return afterLevelName;
    }

    public void setAfterLevelName(String afterLevelName) {
        this.afterLevelName = afterLevelName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
