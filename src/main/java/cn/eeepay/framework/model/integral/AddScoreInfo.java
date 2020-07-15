package cn.eeepay.framework.model.integral;

/**
 * Created by Administrator on 2019/7/16/016.
 * @author  liuks
 *  赠送积分实体
 */
public class AddScoreInfo {

    private String leaguerNo;//会员编号
    private String leaguerName;//会员名称
    private Integer accScore;//积分数量
    private Integer rewardEffeDay;//有效期

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

    public Integer getAccScore() {
        return accScore;
    }

    public void setAccScore(Integer accScore) {
        this.accScore = accScore;
    }

    public Integer getRewardEffeDay() {
        return rewardEffeDay;
    }

    public void setRewardEffeDay(Integer rewardEffeDay) {
        this.rewardEffeDay = rewardEffeDay;
    }
}

