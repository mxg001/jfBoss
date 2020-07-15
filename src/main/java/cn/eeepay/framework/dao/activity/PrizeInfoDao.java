package cn.eeepay.framework.dao.activity;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.activity.AwardBlack;
import cn.eeepay.framework.model.activity.PrizeInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface PrizeInfoDao {

    @Select(
            "select *,award_num-award_grant_num AS numSurplus, day_nums-day_award_grant AS dayNumSurplus " +
                    " from vip_lottery_award_config where act_code=#{actCode} and is_delete='0' "
    )
    List<PrizeInfo> getPrizeInfoList(@Param("actCode") String actCode);

    @Select(
            " select *,award_num-award_grant_num AS numSurplus, day_nums-day_award_grant AS dayNumSurplus " +
                    " from vip_lottery_award_config where award_code=#{awardCode} and is_delete='0' "
    )
    PrizeInfo getPrizeInfoByCode(@Param("awardCode") String awardCode);

    @Select(
            "select *,award_num-award_grant_num AS numSurplus, day_nums-day_award_grant AS dayNumSurplus " +
                    " from vip_lottery_award_config where id=#{id} and is_delete='0' "
    )
    PrizeInfo getPrizeInfoById(@Param("id") Long id);

    @Insert(
            " INSERT INTO vip_lottery_award_config " +
                    " (act_det_code,act_code,award_code,award_name,award_desc,seq_no," +
                    "  award_num,award_grant_num,award_odds,day_nums,day_max,day_award_grant,open_status,award_img, " +
                    "  create_time,is_delete,award_notice,is_shot) " +
                    " VALUES " +
                    " (#{info.actDetCode},#{info.actCode},#{info.awardCode},#{info.awardName},#{info.awardDesc},#{info.seqNo}," +
                    "  #{info.awardNum},0,#{info.awardOdds},#{info.dayNums},#{info.dayMax},0,'0',#{info.awardImg}," +
                    "  now(),'0',#{info.awardNotice},#{info.isShot}) "
    )
    int addLuckPrizeTemplate(@Param("info") PrizeInfo info);

    @Insert(
            " INSERT INTO vip_lottery_award_config " +
                    " (act_det_code,act_code,award_code,award_name,award_desc,seq_no," +
                    "  award_num,award_grant_num,award_odds,day_nums,day_max,day_award_grant,open_status,award_img, " +
                    "  create_time,is_delete,award_notice,is_shot) " +
                    " VALUES " +
                    " (#{info.actDetCode},#{info.actCode},#{info.awardCode},#{info.awardName},#{info.awardDesc},#{info.seqNo}," +
                    "  #{info.awardNum},#{info.awardGrantNum},#{info.awardOdds},#{info.dayNums},#{info.dayMax},#{info.dayAwardGrant},#{info.openStatus},#{info.awardImg}," +
                    "  now(),'0',#{info.awardNotice},#{info.isShot}) "
    )
    int addLuckPrizeTemplate1(@Param("info") PrizeInfo info);

    @UpdateProvider(type = PrizeInfoDao.SqlProvider.class, method = "updateLuckPrize")
    int updateLuckPrize(@Param("info") PrizeInfo info);

    /**
     * 开启/关闭奖项状态
     */
    @Update(
            "update vip_lottery_award_config set open_status=#{status} where id=#{id} "
    )
    int closeOpenPrize(@Param("id") Long id, @Param("status") int status);

    @Update(
            " update vip_lottery_award_config set is_delete='1' where id=#{id} and is_delete='0' "
    )
    int deletePrizeInfo(@Param("id") Long id);


    @SelectProvider(type = PrizeInfoDao.SqlProvider.class, method = "sumPrize")
    @ResultType(BigDecimal.class)
    BigDecimal checkPrize(@Param("info") PrizeInfo prize);

    @Insert(
            " INSERT INTO vip_lottery_award_config " +
                    " (act_det_code,act_code,award_code,ori_award_code,award_name,award_desc, " +
                    "  seq_no,award_num,award_grant_num,award_odds,day_nums, " +
                    "  day_max,day_award_grant,open_status,award_img, " +
                    "  create_time,is_delete,award_notice,is_shot) " +
                    " VALUES " +
                    " (#{newInfo.actDetCode},#{newInfo.actCode},#{newInfo.awardCode},#{newInfo.oriAwardCode},#{oldInfo.awardName},#{oldInfo.awardDesc}, " +
                    "  #{oldInfo.seqNo},#{oldInfo.awardNum},#{oldInfo.awardGrantNum},#{oldInfo.awardOdds},#{oldInfo.dayNums}, " +
                    "  #{oldInfo.dayMax},#{oldInfo.dayAwardGrant},#{oldInfo.openStatus},#{oldInfo.awardImg}, " +
                    "  now(),'0',#{oldInfo.awardNotice},#{oldInfo.isShot})"
    )
    int insertCopyPrInfo(@Param("newInfo") PrizeInfo newInfo, @Param("oldInfo") PrizeInfo oldInfo);

    @SelectProvider(type = PrizeInfoDao.SqlProvider.class, method = "getAwardBlackList")
    @ResultType(AwardBlack.class)
    List<AwardBlack> getAwardBlackList(@Param("info") AwardBlack info, @Param("page") Page<AwardBlack> page);

    @Insert(
            " INSERT INTO vip_award_black(award_code,leaguer_no,create_time,operator) " +
                    " VALUES " +
                    " (#{info.awardCode},#{info.leaguerNo},now(),#{info.operator}) "
    )
    int addAwardBlack(@Param("info") AwardBlack info);

    @Delete(
            " delete from vip_award_black where id=#{info.id} "
    )
    int deleteAwardBlack(@Param("info") AwardBlack info);

    //奖项次数归0
    @Update(
            "update vip_lottery_award_config set day_award_grant=0 where is_delete='0' "
    )
    int prizeTimesReset();

    @Select("select * from vip_lottery_award_config where act_code = #{actCode} and act_det_code = #{actDetCode} and is_delete = 0")
    @ResultType(PrizeInfo.class)
    List<PrizeInfo> getLuckPrizeByActCodeAndActDetCode(@Param("actCode") String actCode, @Param("actDetCode") String actDetCode);

    class SqlProvider {
        public String getAwardBlackList(final Map<String, Object> param) {
            AwardBlack info = (AwardBlack) param.get("info");
            StringBuffer sb = new StringBuffer();
            sb.append(" select bla.*, lea.leaguer_name,lea.origin_user_no ");
            sb.append("   from vip_award_black bla ");
            sb.append("     LEFT JOIN vip_leaguer_info lea ON lea.leaguer_no=bla.leaguer_no ");
            sb.append(" where 1=1  ");
            if (StringUtils.isNotBlank(info.getAwardCode())) {
                sb.append(" AND bla.award_code=#{info.awardCode} ");
            }
            if (StringUtils.isNotBlank(info.getLeaguerNo())) {
                sb.append(" AND bla.leaguer_no=#{info.leaguerNo} ");
            }
            if (StringUtils.isNotBlank(info.getOriginUserNo())) {
                sb.append(" AND lea.origin_user_no=#{info.originUserNo} ");
            }
            return sb.toString();
        }

        public String updateLuckPrize(final Map<String, Object> param) {
            PrizeInfo info = (PrizeInfo) param.get("info");
            StringBuffer sb = new StringBuffer();
            sb.append("update vip_lottery_award_config set ");
            sb.append("  award_name=#{info.awardName},award_desc=#{info.awardDesc},seq_no=#{info.seqNo}, ");
            if (StringUtils.isNotBlank(info.getAwardImg())) {
                sb.append(" award_img=#{info.awardImg}, ");
            }
            sb.append("  award_num=#{info.awardNum},award_odds=#{info.awardOdds},day_nums=#{info.dayNums},day_max=#{info.dayMax}, ");
            sb.append("  award_notice =#{info.awardNotice},is_shot =#{info.isShot} ");
            sb.append(" where id=#{info.id} ");
            return sb.toString();
        }

        public String sumPrize(final Map<String, Object> param) {
            final PrizeInfo info = (PrizeInfo) param.get("info");
            return new SQL() {{
                SELECT("sum(award_odds) probTotal");
                FROM("vip_lottery_award_config");
                WHERE("is_delete ='0' ");
                if (info.getId() != null) {
                    WHERE("id!=#{info.id}");
                }
                if (StringUtils.isNotBlank(info.getActCode())) {
                    WHERE(" act_code = #{info.actCode} ");
                }
                if (StringUtils.isNotBlank(info.getActDetCode())) {
                    WHERE(" act_det_code = #{info.actDetCode} ");
                }
            }}.toString();
        }
    }
}
