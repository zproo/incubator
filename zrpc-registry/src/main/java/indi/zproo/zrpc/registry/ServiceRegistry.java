package indi.zproo.zrpc.registry;

/**
 * 服务注册接口
 *
 * @author: zproo
 * @create: 2018-06-07 15:24
 **/
public interface ServiceRegistry {

	/**
	 * 注册服务名称 & 服务地址
	 */
	void register(String serviceName, String serviceAddress);
}
