package cn.eeepay.framework.model.integral;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2019/7/3/003.
 * @author liuks
 * 任务实体 对于表 vip_business_task_config
 */
public class BusinessTask {

    private Long id;
    private String taskNo;//任务编号
    private String taskName;//任务名称
    private String taskDesc;//任务简介
    private String taskType;//任务类型编号
    private String taskTypeName;//任务类型名称

    private String taskParams;//任务请求参数

    private Integer restrictNum;//限制次数
    private Integer restrictFrequency;//限制频率 次/分
    private String frequencyType;//'频率类型 1 分钟   2  小时  3  日  4  周  5 月
    private Integer status;//状态 1 打开  0 关闭

    private Date lastUpdateTime;//最后更新时间
    private Date createTime;//创建时间
    private String creater;//创建人
    private String taskOverview;//任务简介
    private String grantType;//流水下发方式 0 自动下发  1 手动下发
    private String imgUrl;//任务图标


    private List<TaskRewardConfig> taskRewardConfigList;

    private String businessNo;//业务组

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskTypeName() {
        return taskTypeName;
    }

    public void setTaskTypeName(String taskTypeName) {
        this.taskTypeName = taskTypeName;
    }

    public String getTaskParams() {
        return taskParams;
    }

    public void setTaskParams(String taskParams) {
        this.taskParams = taskParams;
    }

    public Integer getRestrictNum() {
        return restrictNum;
    }

    public void setRestrictNum(Integer restrictNum) {
        this.restrictNum = restrictNum;
    }

    public Integer getRestrictFrequency() {
        return restrictFrequency;
    }

    public void setRestrictFrequency(Integer restrictFrequency) {
        this.restrictFrequency = restrictFrequency;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public List<TaskRewardConfig> getTaskRewardConfigList() {
        return taskRewardConfigList;
    }

    public void setTaskRewardConfigList(List<TaskRewardConfig> taskRewardConfigList) {
        this.taskRewardConfigList = taskRewardConfigList;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }


    public String getTaskOverview() {
        return taskOverview;
    }

    public void setTaskOverview(String taskOverview) {
        this.taskOverview = taskOverview;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getFrequencyType() {
        return frequencyType;
    }

    public void setFrequencyType(String frequencyType) {
        this.frequencyType = frequencyType;
    }
}
