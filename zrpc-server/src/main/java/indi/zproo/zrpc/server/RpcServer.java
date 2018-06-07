package indi.zproo.zrpc.server;

import indi.zproo.zrpc.registry.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * @author: 张健(千昭)
 * @create: 2018-06-07 14:36
 **/
public class RpcServer implements ApplicationContextAware, InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

	private String serverAddress;
	private ServiceRegistry serviceRegistry;

	public RpcServer(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public RpcServer(String serverAddress, ServiceRegistry serviceRegistry) {
		this.serverAddress = serverAddress;
		this.serviceRegistry = serviceRegistry;
	}

	/**
	 * spring容器初始化后，获得ApplicationContext对象
	 *
	 * @param applicationContext
	 * @throws BeansException
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

		// todo: 扫描带有 @RPCService 注解的类并初始化 handlerMap 对象
	}

	/**
	 * spring初始化bean之前做的操作
	 *
	 * @throws Exception
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		// todo: 启动rpc服务器 & 注册rpc服务
	}
}
