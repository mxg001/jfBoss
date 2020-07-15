package cn.eeepay.framework.dao.sys;

import cn.eeepay.framework.model.sys.SeqData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Administrator on 2019/7/3/003.
 */
public interface SequenceVipScoreDao {

    @Insert(
            "INSERT INTO vip_boss_unique_seq (create_time) VALUES (NOW())"
    )
    @Options(useGeneratedKeys = true, keyProperty = "seqData.id")
    Long getSequence(@Param("seqData") SeqData seqData);
}
