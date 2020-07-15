package cn.eeepay.framework.util;

import org.springframework.stereotype.Component;
@Component
public class Constants {

	/*阿里云存储boss附件临时bucket*/
	public static final String ALIYUN_OSS_TEMP_TUCKET="boss-temp";

	/*阿里云存储boss附件bucket*/
	public static final String ALIYUN_OSS_ATTCH_TUCKET="agent-attch";
	public static final String ALIYUN_OSS_SIGN_TUCKET="sign-img";

	//数据库，从库
	public static final String DATA_SOURCE_SLAVE = "slave";
	//数据库，主库
	public static final String DATA_SOURCE_MASTER = "master";

}
