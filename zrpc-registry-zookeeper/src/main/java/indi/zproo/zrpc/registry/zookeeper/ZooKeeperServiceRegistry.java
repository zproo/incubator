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
		logger.debug("connect zookeeper");
	}

	/**
	 * 第一步：实现服务注册 service registry function
	 */
	@Override
	public void register(String serviceName, String serviceAddress) {

		// 创建 registry 节点（持久）
		String registryPath = Constant.ZK_REGISTRY_PATH;
		if (!zkClient.exists(registryPath)) {
			zkClient.createPersistent(registryPath);
			logger.debug("create registry node: {}", registryPath);
		}

		// 创建 service 节点（持久）
		String servicePath = registryPath + "/" + serviceName;
		if (!zkClient.exists(servicePath)) {
			zkClient.createPersistent(servicePath);
			logger.debug("create service node: {}", servicePath);
		}

		// 创建 address 节点（临时）
		String addressPath = servicePath + "/address-";
		// 第二个参数是存储的数据： rpc 服务地址
		String addressNode = zkClient.createEphemeralSequential(addressPath, serviceAddress);
		logger.debug("create address node: {}", addressNode);
	}
}










