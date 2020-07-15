package cn.eeepay.framework.dao.integral;

import cn.eeepay.framework.model.integral.LeaguerImport;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 会员导入
 */
public interface LeaguerImportDao {

    @Select(
            "select * from vip_leaguer_import where mobile_phone=#{mobilePhone} AND business_no=#{businessNo} limit 1 "
    )
    LeaguerImport getLeaguerImport(@Param("mobilePhone")String mobilePhone, @Param("businessNo")String businessNo);

    @Insert(
            "<script> INSERT INTO vip_leaguer_import" +
                    " (business_no,mobile_phone,origin_user_no,operator,create_time) " +
                    " VALUES "+
                    "<foreach  collection=\"list\" item=\"info\" index=\"index\" separator=\",\" > " +
                    "(#{info.businessNo},#{info.mobilePhone},#{info.originUserNo},#{info.operator},NOW())"+
                    " </foreach ></script>"
    )
    int insertLeaguerImportList(@Param("list") List<LeaguerImport> list);

    @Select("select * from vip_leaguer_import where fail_num<3  ORDER BY create_time asc LIMIT 20000 ")
    List<LeaguerImport> getLeaguerImportList();

    @Delete(
            " delete from vip_leaguer_import where id=#{id}"
    )
    int deleteLeaguerImport(@Param("id")int id);

    @Delete(
            " delete from vip_leaguer_import where fail_num >= 3"
    )
    int deleteLeaguerImportByNum();

    @Update(
            "update vip_leaguer_import set fail_num=fail_num+1 where id=#{id} "
    )
    int updateLeaguerImport(@Param("id")int id);
}
