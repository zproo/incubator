package indi.zproo.zrpc.sample.server.sample.api;

/**
 * @author: 张健(千昭)
 * @create: 2018-06-07 14:22
 **/
public interface HelloService {
	String hello(String name);

	String hello(Person person);
}
