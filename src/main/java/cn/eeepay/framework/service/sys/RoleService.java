package cn.eeepay.framework.service.sys;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.sys.RightInfo;
import cn.eeepay.framework.model.sys.RoleInfo;
import cn.eeepay.framework.model.sys.UserInfo;

import java.util.List;

public interface RoleService {
	//查询角色
	Page<RoleInfo> selectRoleByCondition(RoleInfo role, Page<RoleInfo> page);
	//新增角色
	int insertRole(RoleInfo role);
	//修改角色
	int updateRole(RoleInfo role);
	//删除角色
	int deleteRoles(Integer roleId);
	
	//角色已授权的用户
	List<UserInfo> getUsersByRole(Integer id);
	//角色已授权的权限
	List<RightInfo> getRightsByRole(Integer id);
	
	List<RoleInfo> getAllRoles();

}
