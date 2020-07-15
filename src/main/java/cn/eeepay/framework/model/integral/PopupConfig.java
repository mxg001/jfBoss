package cn.eeepay.framework.model.integral;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class PopupConfig {
    private Integer id;
    private String serviceNo;
    private String serviceDetNo;
    private String businessNo;
    private String status;
    private String popContent;
    private String popTimes;
    private Date popStartTime;
    private Date popEndTime;
    private String popImg;
    private String popUrl;
    private String popPosition;
    private String order;
    private Date createTime;

    //关联字段
    private String businessName;
    private String actName;
    private String popImgUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(String serviceNo) {
        this.serviceNo = serviceNo;
    }

    public String getServiceDetNo() {
        return serviceDetNo;
    }

    public void setServiceDetNo(String serviceDetNo) {
        this.serviceDetNo = serviceDetNo;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPopContent() {
        return popContent;
    }

    public void setPopContent(String popContent) {
        this.popContent = popContent;
    }

    public String getPopTimes() {
        return popTimes;
    }

    public void setPopTimes(String popTimes) {
        this.popTimes = popTimes;
    }

    public Date getPopStartTime() {
        return popStartTime;
    }

    public void setPopStartTime(Date popStartTime) {
        this.popStartTime = popStartTime;
    }

    public Date getPopEndTime() {
        return popEndTime;
    }

    public void setPopEndTime(Date popEndTime) {
        this.popEndTime = popEndTime;
    }

    public String getPopImg() {
        return popImg;
    }

    public void setPopImg(String popImg) {
        this.popImg = popImg;
    }

    public String getPopUrl() {
        return popUrl;
    }

    public void setPopUrl(String popUrl) {
        this.popUrl = popUrl;
    }

    public String getPopPosition() {
        return popPosition;
    }

    public void setPopPosition(String popPosition) {
        this.popPosition = popPosition;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public String getPopImgUrl() {
        return popImgUrl;
    }

    public void setPopImgUrl(String popImgUrl) {
        this.popImgUrl = popImgUrl;
    }
}
