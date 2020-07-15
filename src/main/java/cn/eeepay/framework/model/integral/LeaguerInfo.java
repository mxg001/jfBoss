package cn.eeepay.framework.model.integral;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2019/7/10/010.
 * @author liuks
 * 会员实体，对应表vip_leaguer_info
 */
public class LeaguerInfo {

    private Long id;
    private String leaguerNo;//会员编号
    private String leaguerName;//会员名称
    private String businessNo;//业务组编号
    private String originUserNo;//来源用户编号
    private String mobilePhone;//用户手机号
    private BigDecimal mBalance;//M值余额
    private Integer mLevel;//m值等级
    private BigDecimal growUpBalance;//会员成长值余额
    private Integer growUpLevel;//会员成长值等级
    private Date growUpEffe;//会员成长值有效期
    private BigDecimal growUpRadix;//'会员成长值过期基数'
    private String  status;//1 正常 2 冻结
    private Date lastUpdateTime;//'最后更新时间

    private Date createTime;//创建时间
    private Date createTimeBegin;
    private Date createTimeEnd;


    private String idCardNo;//身份证号
    private Date birthday;//生日

    private Date growDate;//会员等级更新日期
    private Date growDateBegin;
    private Date growDateEnd;

    private Integer pageFirst;//每页开始第几行
    private Integer pageSize;//每页大小
    private String businessNoFilter;//权限数据过滤


    private String mLevelName;//m值等级名称
    private String vipLevelName;//会员等级名称

    private BigDecimal scoreBalance;//会员可用积分
    private String businessName;//业务组名称

    private List<LeaguerGrow> leaguerGrowList;//会员成长记录

    private List<LeaguerScore> leaguerScoreList;//积分余额,目前只有一条

    private String digestKey;//业务组上面的验签

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLeaguerNo() {
        return leaguerNo;
    }

    public void setLeaguerNo(String leaguerNo) {
        this.leaguerNo = leaguerNo;
    }

    public String getLeaguerName() {
        return leaguerName;
    }

    public void setLeaguerName(String leaguerName) {
        this.leaguerName = leaguerName;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getOriginUserNo() {
        return originUserNo;
    }

    public void setOriginUserNo(String originUserNo) {
        this.originUserNo = originUserNo;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public BigDecimal getmBalance() {
        return mBalance;
    }

    public void setmBalance(BigDecimal mBalance) {
        this.mBalance = mBalance;
    }

    public Integer getmLevel() {
        return mLevel;
    }

    public void setmLevel(Integer mLevel) {
        this.mLevel = mLevel;
    }

    public BigDecimal getGrowUpBalance() {
        return growUpBalance;
    }

    public void setGrowUpBalance(BigDecimal growUpBalance) {
        this.growUpBalance = growUpBalance;
    }

    public Integer getGrowUpLevel() {
        return growUpLevel;
    }

    public void setGrowUpLevel(Integer growUpLevel) {
        this.growUpLevel = growUpLevel;
    }

    public Date getGrowUpEffe() {
        return growUpEffe;
    }

    public void setGrowUpEffe(Date growUpEffe) {
        this.growUpEffe = growUpEffe;
    }

    public BigDecimal getGrowUpRadix() {
        return growUpRadix;
    }

    public void setGrowUpRadix(BigDecimal growUpRadix) {
        this.growUpRadix = growUpRadix;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public Date getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(Date createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public Date getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getGrowDate() {
        return growDate;
    }

    public void setGrowDate(Date growDate) {
        this.growDate = growDate;
    }

    public Date getGrowDateBegin() {
        return growDateBegin;
    }

    public void setGrowDateBegin(Date growDateBegin) {
        this.growDateBegin = growDateBegin;
    }

    public Date getGrowDateEnd() {
        return growDateEnd;
    }

    public void setGrowDateEnd(Date growDateEnd) {
        this.growDateEnd = growDateEnd;
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

    public String getmLevelName() {
        return mLevelName;
    }

    public void setmLevelName(String mLevelName) {
        this.mLevelName = mLevelName;
    }

    public String getVipLevelName() {
        return vipLevelName;
    }

    public void setVipLevelName(String vipLevelName) {
        this.vipLevelName = vipLevelName;
    }

    public BigDecimal getScoreBalance() {
        return scoreBalance;
    }

    public void setScoreBalance(BigDecimal scoreBalance) {
        this.scoreBalance = scoreBalance;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public List<LeaguerGrow> getLeaguerGrowList() {
        return leaguerGrowList;
    }

    public void setLeaguerGrowList(List<LeaguerGrow> leaguerGrowList) {
        this.leaguerGrowList = leaguerGrowList;
    }

    public List<LeaguerScore> getLeaguerScoreList() {
        return leaguerScoreList;
    }

    public void setLeaguerScoreList(List<LeaguerScore> leaguerScoreList) {
        this.leaguerScoreList = leaguerScoreList;
    }

    public String getDigestKey() {
        return digestKey;
    }

    public void setDigestKey(String digestKey) {
        this.digestKey = digestKey;
    }
}
