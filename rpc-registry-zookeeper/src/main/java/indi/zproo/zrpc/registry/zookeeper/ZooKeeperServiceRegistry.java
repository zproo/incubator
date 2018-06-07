package indi.zproo.zrpc.registry.zookeeper;


import indi.zproo.zrpc.registry.ServiceRegistry;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基于zookeeper的服务注册功能实现
 *
 * @author: zproo
 * @create: 2018-06-07 15:52
 **/
public class ZooKeeperServiceRegistry implements ServiceRegistry {

	private static final Logger logger = LoggerFactory.getLogger(ZooKeeperServiceRegistry.class);

	private final ZkClient zkClient;

	public ZooKeeperServiceRegistry(String zkAddress) {
		zkClient = new ZkClient(zkAddress, Constant.ZK_SESSION_TIMEOUT, Constant.ZK_CONNECTION_TIMEOUT);
		logger.debug("connect zookeepper");
	}

//	todo: 第一步：实现服务注册 service registry function
	@Override
	public void register(String serviceName, String serviceAddress) {


	}
}
