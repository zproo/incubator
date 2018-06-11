package indi.zproo.zrpc.registry.zookeeper;

/**
 * @author: zproo
 * @create: 2018-06-07 16:08
 **/
public interface Constant {

	// 接口中变量默认都是 public static final的。
	/**
	 * zookeepper 中会话过期时间
	 */
	int ZK_SESSION_TIMEOUT = 5000;

	/**
	 * zkclient 需要的连接超时时间
	 */
	int ZK_CONNECTION_TIMEOUT = 1000;

	String ZK_REGISTRY_PATH = "/registry";
}
