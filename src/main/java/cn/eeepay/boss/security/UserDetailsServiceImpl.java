package cn.eeepay.boss.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import cn.eeepay.framework.model.sys.MenuInfo;
import cn.eeepay.framework.model.sys.UserBusinessNo;
import cn.eeepay.framework.model.sys.UserInfo;
import cn.eeepay.framework.model.sys.UserLoginInfo;
import cn.eeepay.framework.service.sys.MenuService;
import cn.eeepay.framework.service.sys.UserGrantService;
import cn.eeepay.framework.service.sys.UserService;
import cn.eeepay.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


/**
 * 用户详细信息类
 *
 * 负责以{@link UserDetails}方式提供用户信息
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 */
public class UserDetailsServiceImpl implements UserDetailsService {

	@Resource
	public UserService userService;
	@Resource
	public UserGrantService userGrantService;
	@Resource
	private HttpServletRequest request;
	@Resource
	private MenuService menuService;
	
	@Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
//    	String ip = request.getRemoteAddr();
//    	 String key = "blocked:"+ip;
//        if (userService.isBlocked(key)) {
//            throw new RuntimeException("blocked");
//        }
        
    	UserLoginInfo userInfo = null;
//    	UserInfo user = new UserInfo();
//    	user.setUserName(userName);
//		Page<UserInfo> page = new Page<>(1, 1);
//		UserInfo shiroUser = page.getResult().get(0);
		UserInfo shiroUser = userService.selectUserByUserName(userName);

    	try{
	    	if (shiroUser != null) {
	    		Integer uId = shiroUser.getId();
	    		Set<GrantedAuthority> authorities= new HashSet<GrantedAuthority>();
    			List<MenuInfo> userRigths = userGrantService.getUserAllMenu(uId);//用户拥有的所有菜单ID
        		for (MenuInfo sr : userRigths) {
        			if(sr != null)
//        				authorities.add(new SimpleGrantedAuthority(sr.getId().toString()));
        				authorities.add(new SimpleGrantedAuthority(sr.getMenuCode()));
				}
        		
	        	userInfo = new UserLoginInfo(shiroUser.getUserName(), shiroUser.getPassword(), authorities);
	        	userInfo.setId(shiroUser.getId());
	        	userInfo.setRealName(shiroUser.getRealName());
	        	userInfo.setTelNo(shiroUser.getTelNo());
	        	userInfo.setEmail(shiroUser.getEmail());
	        	userInfo.setStatus(shiroUser.getStatus());
	        	userInfo.setTheme(shiroUser.getTheme());
				userInfo.setBusinessNos(initList(userService.getUserBusinessNoList(userName)));
			}
	    } catch (Exception e) {
			e.printStackTrace();
		}
    	
        return userInfo;
    }

    private  String initList(List<UserBusinessNo> list){
    	if(list!=null&&list.size()>0){
    		List<String> newList=new ArrayList<String>();
    		for(UserBusinessNo item:list){
    			if(item!=null&& StringUtils.isNotBlank(item.getBusinessNos())){
    				if("-1".equals(item.getBusinessNos())){
    					return "-1";
					}else{
						String[] strs=item.getBusinessNos().split(",");
						if(strs!=null&&strs.length>0){
							for(String str:strs){
								if(!newList.contains(str)){
									newList.add(str);
								}
							}
						}
					}
				}
			}
			return  StringUtil.listToString(newList,",",true);
		}
		return null;
	}
}
