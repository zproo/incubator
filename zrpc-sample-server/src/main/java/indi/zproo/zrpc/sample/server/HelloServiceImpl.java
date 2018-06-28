package indi.zproo.zrpc.sample.server;

import indi.zproo.zrpc.sample.api.HelloService;
import indi.zproo.zrpc.sample.api.Person;
import indi.zproo.zrpc.server.RpcService;

/**
 * @author: zproo
 * @create: 2018-06-07 15:04
 **/
@RpcService(HelloService.class)
public class HelloServiceImpl implements HelloService {

	@Override
	public String hello(String name) {
		return "Hello!" + name;
	}

	@Override
	public String hello(Person person) {
		return "Hello!" + person.getFirstName() + person.getLastName();
	}
}
