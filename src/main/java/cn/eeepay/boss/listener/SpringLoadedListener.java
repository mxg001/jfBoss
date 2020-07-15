package cn.eeepay.boss.listener;

import cn.eeepay.framework.service.sys.ConfigLoadedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import cn.eeepay.boss.quartz.QuartzManager;

import javax.annotation.Resource;

/**
 * spring容器加载完后启动的监听器
 * 目的： 服务器重新启动，需要将出款服务定时任务重新启动
 * @author Administrator
 *
 */
public class SpringLoadedListener  implements ApplicationListener<ContextRefreshedEvent>{
	@Autowired
	private QuartzManager quartzManager;
	@Resource
	private ConfigLoadedService configLoadedService;

	private final Logger log = LoggerFactory.getLogger(SpringLoadedListener.class);

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			quartzManager.startJobs();
			configLoadedService.initConfig();
		} catch (Exception e) {
			log.error("spring容器初始化定时任务出现异常", e);
		}
	}

}
