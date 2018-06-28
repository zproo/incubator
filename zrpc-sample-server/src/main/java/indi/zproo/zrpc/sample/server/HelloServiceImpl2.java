package indi.zproo.zrpc.sample.server;

import indi.zproo.zrpc.sample.api.HelloService;
import indi.zproo.zrpc.sample.api.Person;
import indi.zproo.zrpc.server.RpcService;

/**
 * @author: zproo
 * @create: 2018-06-13 16:59
 **/
@RpcService(value = HelloService.class, version = "sample.hello2")
public class HelloServiceImpl2 implements HelloService {

	@Override
	public String hello(String name) {
		return "你好! " + name;
	}


	@Override
	public String hello(Person person) {
		return "你好! " + person.getFirstName() + " " + person.getLastName();
	}
}
