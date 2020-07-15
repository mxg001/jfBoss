package cn.eeepay.framework.model.integral;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2019/7/16/016.
 * @author  liuks
 * 积分列表的 统计字段
 */
public class ScoreAccountCount {

    private int total;//总条数
    private BigDecimal addScoreNum;//总新增积分
    private BigDecimal usedScoreNum;//总使用积分
    private BigDecimal expScoreNum;//总过期积分
    private BigDecimal cancelScoreNum;//总作废积分


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public BigDecimal getAddScoreNum() {
        return addScoreNum;
    }

    public void setAddScoreNum(BigDecimal addScoreNum) {
        this.addScoreNum = addScoreNum;
    }

    public BigDecimal getUsedScoreNum() {
        return usedScoreNum;
    }

    public void setUsedScoreNum(BigDecimal usedScoreNum) {
        this.usedScoreNum = usedScoreNum;
    }

    public BigDecimal getExpScoreNum() {
        return expScoreNum;
    }

    public void setExpScoreNum(BigDecimal expScoreNum) {
        this.expScoreNum = expScoreNum;
    }

    public BigDecimal getCancelScoreNum() {
        return cancelScoreNum;
    }

    public void setCancelScoreNum(BigDecimal cancelScoreNum) {
        this.cancelScoreNum = cancelScoreNum;
    }
}
