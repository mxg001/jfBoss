package cn.eeepay.framework.model.integral;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2019/7/2/002.
 * @author liuks
 * 业务组实体 对应表vip_business_info
 */
public class BusinessInfo {

    private Long id;
    private String businessNo;//业务组编号
    private String businessName;//业务组名称
    private String digestKey;//验签秘钥

    private String levelDesc;//等级说明
    private Integer levelEffeDate;//成长值有效期天数
    private BigDecimal levelNum;//成长值固定值
    private BigDecimal levelRate;//成长值比例
    private String scoreDesc;//积分说明

    private String ipWhiteList;//ip白名单
    private Integer ipCheckSwitch;//ip 校验开关 0 关闭 1 打开
    private Integer status;//业务组状态 1 正常

    private Date lastUpdateTime;//最后更新时间
    private Date createTime;//创建时间

    private List<LevelInfo> growLevelInfoList;//会员等级列表
    private List<LevelInfo> mLevelInfoList;//M值等级列表

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

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getDigestKey() {
        return digestKey;
    }

    public void setDigestKey(String digestKey) {
        this.digestKey = digestKey;
    }

    public String getLevelDesc() {
        return levelDesc;
    }

    public void setLevelDesc(String levelDesc) {
        this.levelDesc = levelDesc;
    }

    public Integer getLevelEffeDate() {
        return levelEffeDate;
    }

    public void setLevelEffeDate(Integer levelEffeDate) {
        this.levelEffeDate = levelEffeDate;
    }

    public BigDecimal getLevelNum() {
        return levelNum;
    }

    public void setLevelNum(BigDecimal levelNum) {
        this.levelNum = levelNum;
    }

    public BigDecimal getLevelRate() {
        return levelRate;
    }

    public void setLevelRate(BigDecimal levelRate) {
        this.levelRate = levelRate;
    }

    public String getScoreDesc() {
        return scoreDesc;
    }

    public void setScoreDesc(String scoreDesc) {
        this.scoreDesc = scoreDesc;
    }

    public String getIpWhiteList() {
        return ipWhiteList;
    }

    public void setIpWhiteList(String ipWhiteList) {
        this.ipWhiteList = ipWhiteList;
    }

    public Integer getIpCheckSwitch() {
        return ipCheckSwitch;
    }

    public void setIpCheckSwitch(Integer ipCheckSwitch) {
        this.ipCheckSwitch = ipCheckSwitch;
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

    public List<LevelInfo> getGrowLevelInfoList() {
        return growLevelInfoList;
    }

    public void setGrowLevelInfoList(List<LevelInfo> growLevelInfoList) {
        this.growLevelInfoList = growLevelInfoList;
    }

    public List<LevelInfo> getmLevelInfoList() {
        return mLevelInfoList;
    }

    public void setmLevelInfoList(List<LevelInfo> mLevelInfoList) {
        this.mLevelInfoList = mLevelInfoList;
    }
}
