package indi.zproo.zrpc.server;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Rpc 服务注解（标注在服务实现类上）
 *
 * @author: 张健(千昭)
 * @create: 2018-06-07 14:28
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
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
