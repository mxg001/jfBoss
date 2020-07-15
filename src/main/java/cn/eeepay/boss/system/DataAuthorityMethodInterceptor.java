package cn.eeepay.boss.system;


import cn.eeepay.framework.model.sys.UserLoginInfo;
import cn.eeepay.framework.util.StringUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2019/7/9/009.
 * @author  liuks
 */
public class DataAuthorityMethodInterceptor implements MethodInterceptor {


    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();
        if(method.isAnnotationPresent(DataAuthority.class)){
            ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String businessNo=request.getParameter("businessNo");
            UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Map<String, Object> msg=new HashMap<String,Object>();
            if(!StringUtil.checkBusinessNo(principal.getBusinessNos(),businessNo)) {
                msg.put("dataStatus", "dataError");
                return msg;
            }
        }
        return methodInvocation.proceed();
    }
}
