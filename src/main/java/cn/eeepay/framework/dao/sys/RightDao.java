package cn.eeepay.framework.dao.sys;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.model.sys.MenuInfo;
import cn.eeepay.framework.model.sys.RightInfo;
import cn.eeepay.framework.model.sys.RoleInfo;
import cn.eeepay.framework.model.sys.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;

public interface RightDao {

	@SelectProvider(type=SqlProvider.class, method="selectRightByCondition")
	@ResultType(RightInfo.class)
	List<RightInfo> selecRightByCondition(@Param("right") RightInfo right, Page<RightInfo> page);
	
	@Select("select 1 from vip_boss_shiro_right where EXISTS (select 1 from vip_boss_shiro_right r where r.right_code=#{rightCode}) limit 1")
	@ResultType(Integer.class)
	Integer checkCodeUnique(@Param("rightCode")String rightCode);
	
	@Insert("insert into vip_boss_shiro_right (right_code,right_name,right_comment,business_nos) values (#{right.rightCode},#{right.rightName},"
			+ "#{right.rightComment},#{right.businessNos})")
	int saveRight(@Param("right")RightInfo right);

	@Update("update vip_boss_shiro_right set right_code=#{right.rightCode},right_name=#{right.rightName},right_comment=#{right.rightComment}, " +
			" business_nos=#{right.businessNos} "+
			" where id=#{right.id}")
	int updateRight(@Param("right")RightInfo right);
	
	@Delete("DELETE FROM vip_boss_user_right where right_id=#{rightId}")
	void delUsers(Integer rightId);

	@Delete("DELETE FROM boss_role_right where right_id=#{rightId}")
	void delRoles(Integer rightId);

	@Delete("DELETE FROM vip_boss_shiro_right where id=#{rightId}")
	int delRight(Integer rightId);
	
	@Select("SELECT r.* FROM boss_role_right rr,boss_shiro_role r WHERE r.id=rr.role_id AND rr.right_id=#{rightId}")
	@ResultType(RoleInfo.class)
	List<RoleInfo> getRoleByRight(Integer rightId);
	
	@Select("SELECT u.* FROM vip_boss_user_right ur,vip_boss_shiro_user u WHERE u.id=ur.user_id AND ur.right_id=#{rightId}")
	@ResultType(UserInfo.class)
	List<UserInfo> getUserByRight(Integer rightId);

	@Select("select * from vip_boss_shiro_right")
	@ResultType(RightInfo.class)
	List<RightInfo> getAllRights();

	@Select("select m.* from vip_boss_sys_menu m,vip_boss_right_menu rm where rm.right_id=#{id} and m.id=rm.menu_id")
	@ResultType(MenuInfo.class)
	List<MenuInfo> getMenuByRight(Integer id);
	
	class SqlProvider{
		public String selectRightByCondition(Map<String, Object> param){
			final RightInfo right = (RightInfo)param.get("right");
			return new SQL(){
				{
					SELECT("*");
					FROM("vip_boss_shiro_right");
					if(right!= null){
						if(StringUtils.isNotBlank(right.getRightName())){
							right.setRightName(right.getRightName()+"%");
							WHERE("right_name like #{right.rightName}");
						}
						if(StringUtils.isNotBlank(right.getRightCode())){
							right.setRightCode(right.getRightCode()+"%");
							WHERE("right_code like #{right.rightCode}");
						}
					}
				}
			}.toString();
		}
	}

}
