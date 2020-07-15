package cn.eeepay.framework.model.integral;

import java.util.Date;

/**
 * Created by Administrator on 2019/7/4/004.
 * @author  liuks
 * 任务权益表 对应表vip_task_reward_config
 */
public class TaskRewardConfig {

    private Long id;
    private String businessNo;//业务组编号 不关联业务组默认 0
    private String taskNo;//任务编号
    private String rewardNo;//权益编号TR
    private String rewardName;//权益名称
    private String rewardType;//权益类型 1 会员积分 2 会员等级 3 M值
    private String calcType;//权益计算类型1 单笔 2 公式  3 模板
    private String rewardTemplate;//计算模板
    private String rewardTemplateFormula;//计算公式 比如 {0}+500
    private Integer rewardEffeDay;//有效期天数

    private Date createTime;//创建时间
    private String creater;//创建人

    private String rewardNoBack;//原权益号

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getRewardNo() {
        return rewardNo;
    }

    public void setRewardNo(String rewardNo) {
        this.rewardNo = rewardNo;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

    public String getCalcType() {
        return calcType;
    }

    public void setCalcType(String calcType) {
        this.calcType = calcType;
    }

    public String getRewardTemplate() {
        return rewardTemplate;
    }

    public void setRewardTemplate(String rewardTemplate) {
        this.rewardTemplate = rewardTemplate;
    }

    public String getRewardTemplateFormula() {
        return rewardTemplateFormula;
    }

    public void setRewardTemplateFormula(String rewardTemplateFormula) {
        this.rewardTemplateFormula = rewardTemplateFormula;
    }

    public Integer getRewardEffeDay() {
        return rewardEffeDay;
    }

    public void setRewardEffeDay(Integer rewardEffeDay) {
        this.rewardEffeDay = rewardEffeDay;
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

    public String getRewardNoBack() {
        return rewardNoBack;
    }

    public void setRewardNoBack(String rewardNoBack) {
        this.rewardNoBack = rewardNoBack;
    }
}
