package cn.eeepay.framework.model.activity;

/**
 * 中奖名单显示
 *  存储在 vip_sys_config 表中 key 为 PRIZE_DISPLAY
 */
public class WinningList {

    private String maxPrizeNos;//大奖编号 逗号间隔
    private String maxPrizeCount;//大奖数量

    private String prizeNos;//小奖
    private String prizeCount;//小奖数量

    private String minPrizeNos;//最小奖编号 逗号间隔
    private String minPrizeCount;//最小奖数量

    private String count;//总数量

    public String getMaxPrizeNos() {
        return maxPrizeNos;
    }

    public void setMaxPrizeNos(String maxPrizeNos) {
        this.maxPrizeNos = maxPrizeNos;
    }

    public String getMaxPrizeCount() {
        return maxPrizeCount;
    }

    public void setMaxPrizeCount(String maxPrizeCount) {
        this.maxPrizeCount = maxPrizeCount;
    }

    public String getPrizeNos() {
        return prizeNos;
    }

    public void setPrizeNos(String prizeNos) {
        this.prizeNos = prizeNos;
    }

    public String getPrizeCount() {
        return prizeCount;
    }

    public void setPrizeCount(String prizeCount) {
        this.prizeCount = prizeCount;
    }

    public String getMinPrizeNos() {
        return minPrizeNos;
    }

    public void setMinPrizeNos(String minPrizeNos) {
        this.minPrizeNos = minPrizeNos;
    }

    public String getMinPrizeCount() {
        return minPrizeCount;
    }

    public void setMinPrizeCount(String minPrizeCount) {
        this.minPrizeCount = minPrizeCount;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
