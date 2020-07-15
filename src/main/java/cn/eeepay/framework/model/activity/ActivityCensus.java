package cn.eeepay.framework.model.activity;

import java.util.Date;

/**
 * 活动统计
 * 对应表 vip_activity_census
 */
public class ActivityCensus {

    private Long id;
    private Date countDate;//汇总日期
    private Date countDateBegin;
    private Date countDateEnd;

    private String actDetCode;//活动明细编号
    private String actCode;//活动编码
    private Integer ipCount;//独立访问 同一个ip 15分钟内算一次
    private Integer requestNum;//访问量
    private Integer countActNum;//参与人次
    private Integer countUser;//用户数
    private Date createTime;//创建时间
    private String actName;//活动名称

    private Integer pageFirst;//每页开始第几行
    private Integer pageSize;//每页大小
    private String businessNoFilter;//权限数据过滤

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCountDate() {
        return countDate;
    }

    public void setCountDate(Date countDate) {
        this.countDate = countDate;
    }

    public Date getCountDateBegin() {
        return countDateBegin;
    }

    public void setCountDateBegin(Date countDateBegin) {
        this.countDateBegin = countDateBegin;
    }

    public Date getCountDateEnd() {
        return countDateEnd;
    }

    public void setCountDateEnd(Date countDateEnd) {
        this.countDateEnd = countDateEnd;
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

    public Integer getIpCount() {
        return ipCount;
    }

    public void setIpCount(Integer ipCount) {
        this.ipCount = ipCount;
    }

    public Integer getRequestNum() {
        return requestNum;
    }

    public void setRequestNum(Integer requestNum) {
        this.requestNum = requestNum;
    }

    public Integer getCountActNum() {
        return countActNum;
    }

    public void setCountActNum(Integer countActNum) {
        this.countActNum = countActNum;
    }

    public Integer getCountUser() {
        return countUser;
    }

    public void setCountUser(Integer countUser) {
        this.countUser = countUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
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
