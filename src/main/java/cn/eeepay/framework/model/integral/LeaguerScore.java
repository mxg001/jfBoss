package cn.eeepay.framework.model.integral;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2019/7/10/010.
 * @author liuks
 * 会员积分列表 对应表 vip_leaguer_score
 */
public class LeaguerScore {

    private Long id;
    private String leaguerNo;//会员编号
    private BigDecimal scoreBalance;//会员可用积分
    private BigDecimal scoreUsedBalance;//已使用积分
    private BigDecimal scoreExpBalance;//过期积分
    private BigDecimal scoreCancelBalance;//作废积分
    private BigDecimal scoreTotal;//积分总额（累加值不减少）

    private Date createTime;

    private BigDecimal scoreBatchMonth;//本期批次将过期积分

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

    public BigDecimal getScoreBalance() {
        return scoreBalance;
    }

    public void setScoreBalance(BigDecimal scoreBalance) {
        this.scoreBalance = scoreBalance;
    }

    public BigDecimal getScoreUsedBalance() {
        return scoreUsedBalance;
    }

    public void setScoreUsedBalance(BigDecimal scoreUsedBalance) {
        this.scoreUsedBalance = scoreUsedBalance;
    }

    public BigDecimal getScoreExpBalance() {
        return scoreExpBalance;
    }

    public void setScoreExpBalance(BigDecimal scoreExpBalance) {
        this.scoreExpBalance = scoreExpBalance;
    }

    public BigDecimal getScoreCancelBalance() {
        return scoreCancelBalance;
    }

    public void setScoreCancelBalance(BigDecimal scoreCancelBalance) {
        this.scoreCancelBalance = scoreCancelBalance;
    }

    public BigDecimal getScoreTotal() {
        return scoreTotal;
    }

    public void setScoreTotal(BigDecimal scoreTotal) {
        this.scoreTotal = scoreTotal;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getScoreBatchMonth() {
        return scoreBatchMonth;
    }

    public void setScoreBatchMonth(BigDecimal scoreBatchMonth) {
        this.scoreBatchMonth = scoreBatchMonth;
    }
}
