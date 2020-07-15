package cn.eeepay.boss.system;

import org.springframework.stereotype.Repository;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据业务隔离校验注解
 * 需要前端传送 businessNo 参数过滤校验
 * 只做页面请求参数值与用户权限对比校验,统一方法返回,不能屏蔽篡改现象,
 * 针对单业务组数据处理
 * 
 *  注: 因 页面传送的businessNo不可信赖，
 * 针对篡改 businessNo ,在业务处理方法中务必 增加 where businessNo 条件过滤
 * 就算篡改过，返回的处理结果也是失败
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repository
public @interface DataAuthority {
}
