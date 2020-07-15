package cn.eeepay.framework.model.integral;

import java.util.Date;

/**
 * Created by Administrator on 2019/7/4/004.
 * @author liuks
 * 任务类型实体
 * 对应表 vip_task_type_config
 */
public class TaskType {

    private Long id;
    private String taskTypeNo;//任务类型编号
    private String taskTypeName;//任务类型名称
    private Date lastUpdateTime;//最后更新时间
    private Date createTime;//创建时间


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskTypeNo() {
        return taskTypeNo;
    }

    public void setTaskTypeNo(String taskTypeNo) {
        this.taskTypeNo = taskTypeNo;
    }

    public String getTaskTypeName() {
        return taskTypeName;
    }

    public void setTaskTypeName(String taskTypeName) {
        this.taskTypeName = taskTypeName;
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
}
