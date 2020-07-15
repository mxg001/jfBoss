package cn.eeepay.framework.serviceImpl.sys;

import cn.eeepay.framework.dao.sys.RoleDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.sys.RightInfo;
import cn.eeepay.framework.model.sys.RoleInfo;
import cn.eeepay.framework.model.sys.UserInfo;
import cn.eeepay.framework.model.sys.UserLoginInfo;
import cn.eeepay.framework.service.sys.RoleService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("roleService")
@Transactional
public class RoleServiceImpl implements RoleService {

	@Resource
	private RoleDao roleDao;
	
	@Override
	public Page<RoleInfo> selectRoleByCondition(RoleInfo role, Page<RoleInfo> page) {
		roleDao.selectRoleByCondition(role, page);
		return page;
	}

	@Override
	public int insertRole(RoleInfo role) {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		role.setCreateOperator(principal.getId().toString());
		return roleDao.saveRole(role);
	}

	@Override
	public int updateRole(RoleInfo role) {
		// TODO Auto-generated method stub
		return roleDao.updateRole(role);
	}

	@Override
	public List<RoleInfo> getAllRoles() {
		// TODO Auto-generated method stub
		return roleDao.getAllRoles();
	}
	
	@Override
	public int deleteRoles(Integer roleId) {
		roleDao.delUserRoles(roleId);
		roleDao.delRoleRights(roleId);
		int num=roleDao.delRole(roleId);
		return num;
	}
	
	@Override
	public List<UserInfo> getUsersByRole(Integer id) {
		return roleDao.getUsersByRole(id);
	}

	@Override
	public List<RightInfo> getRightsByRole(Integer id) {
		return roleDao.getRightsByRole(id);
	}

}
