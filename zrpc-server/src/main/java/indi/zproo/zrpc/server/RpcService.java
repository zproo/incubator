package indi.zproo.zrpc.server;

/**
 * Rpc 服务注解（标注在服务实现类上）
 *
 * @author: 张健(千昭)
 * @create: 2018-06-07 14:28
 **/

public @interface RpcService {
	/**
	 * 服务接口类
	 */
	Class<?> value();

	/**
	 * 服务版本号
	 */
	String version() default "";
}
