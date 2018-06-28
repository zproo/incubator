package indi.zproo.zrpc.sample.client;

import indi.zproo.zrpc.client.RpcProxy;
import indi.zproo.zrpc.sample.api.HelloService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HelloClient {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext(
            "spring.xml");
        RpcProxy rpcProxy = classPathXmlApplicationContext.getBean(RpcProxy.class);

        HelloService helloService = rpcProxy.create(HelloService.class);
        String result = helloService.hello("world");
        System.out.println(result);

        //HelloService helloService2 = rpcProxy.create(HelloService.class, "sample.hello2");
        //String result2 = helloService2.hello("世界");
        //System.out.println(result2);

        //System.exit(0);
    }
}
