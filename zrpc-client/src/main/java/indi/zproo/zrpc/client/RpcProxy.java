package indi.zproo.zrpc.client;

import indi.zproo.zrpc.common.bean.RpcRequest;
import indi.zproo.zrpc.common.bean.RpcResponse;
import indi.zproo.zrpc.common.utils.StringUtil;
import indi.zproo.zrpc.registry.ServiceDiscovery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * rpc 代理（用于创建 rpc 服务代理）
 *
 * @author: 千昭
 * @create: 2018-06-26 17:16
 **/
public class RpcProxy {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcProxy.class);

    // 服务地址
    private String serviceAddress;
    // 服务发现
    private ServiceDiscovery serviceDiscovery;

    public RpcProxy(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public RpcProxy(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    /**
     * 创建 rpc 服务代理
     *
     * @param interfaceClass 服务的接口名
     * @param <T>
     * @return rpc 服务代理
     */
    public <T> T create(final Class<?> interfaceClass) {
        return create(interfaceClass, "");
    }

    /**
     * 创建 rpc 服务代理
     *
     * @param interfaceClass 服务的接口名
     * @param serviceVersion 服务版本
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T create(final Class<?> interfaceClass, final String serviceVersion) {

        // 创建动态代理对象
        return (T)Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[] {interfaceClass},
            new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                    // 创建 rpc 请求对象并设置请求属性
                    RpcRequest request = new RpcRequest();
                    request.setRequestId(UUID.randomUUID().toString());
                    request.setInterfaceName(method.getDeclaringClass().getName());
                    request.setMethodName(method.getName());
                    request.setParameters(args);
                    request.setParameterTypes(method.getParameterTypes());
                    request.setServiceVersion(serviceVersion);
                    // 获取 rpc 服务地址（zookeeper discover）
                    if (serviceDiscovery != null) {
                        String serviceName = interfaceClass.getName();
                        if (StringUtil.isNotEmpty(serviceVersion)) {
                            serviceName += "-" + serviceVersion;
                        }
                        serviceAddress = serviceDiscovery.discover(serviceName);
                        LOGGER.debug("discover service: {} => {}", serviceName, serviceAddress);
                    }

                    if (StringUtil.isEmpty(serviceAddress)) {
                        throw new RuntimeException("server address is empty");
                    }
                    // 从 rpc 服务地址中解析主机名和端口号 host:port
                    String[] array = StringUtil.split(serviceAddress, ":");
                    String host = array[0];
                    int port = Integer.parseInt(array[1]);
                    // 创建 rpc 客户端对象并发送 rpc 请求
                    RpcClient rpcClient = new RpcClient(host, port);
                    RpcResponse rpcResponse = rpcClient.send(request);

                    if (rpcResponse == null) {
                        throw new RuntimeException("response is null");
                    }
                    // 返回 rpc 响应结果
                    if (rpcResponse.hasException()) {
                        throw rpcResponse.getException();
                    } else {
                        return rpcResponse.getResult();
                    }
                }
            });
    }

}


















