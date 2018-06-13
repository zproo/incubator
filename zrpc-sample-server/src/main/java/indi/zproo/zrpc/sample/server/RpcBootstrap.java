package indi.zproo.zrpc.sample.server;

import indi.zproo.zrpc.server.RpcServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author: zproo
 * @create: 2018-06-08 11:40
 **/
public class RpcBootstrap {
	private static final Logger logger = LoggerFactory.getLogger(RpcBootstrap.class);

	public static void main(String[] args) {
		logger.debug("start server!!");
		new ClassPathXmlApplicationContext("spring.xml");
	}
}
