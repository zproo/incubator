package indi.zproo.zrpc.server;

import indi.zproo.zrpc.common.bean.RpcRequest;
import indi.zproo.zrpc.common.bean.RpcResponse;
import indi.zproo.zrpc.common.codec.RpcDecoder;
import indi.zproo.zrpc.common.codec.RpcEncoder;
import indi.zproo.zrpc.common.utils.StringUtil;
import indi.zproo.zrpc.registry.ServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;


/**
 * @author: 张健(千昭)
 * @create: 2018-06-07 14:36
 **/
public class RpcServer implements ApplicationContextAware, InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

	private String serviceAddress;
	private ServiceRegistry serviceRegistry;

	/**
	 * 存放 服务名 & 服务对象 之间的映射关系
	 */
	private Map<String, Object> handlerMap = new HashMap<>();

	public RpcServer(String serviceAddress) {
		this.serviceAddress = serviceAddress;
	}

	public RpcServer(String serviceAddress, ServiceRegistry serviceRegistry) {
		this.serviceAddress = serviceAddress;
		this.serviceRegistry = serviceRegistry;
	}

	/**
	 * spring 容器初始化后，获得 spring 的 ApplicationContext 对象
	 *
	 * @param applicationContext
	 * @throws BeansException
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

		// 扫描带有 @RPCService 注解的类并初始化 handlerMap 对象
		Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
		if (MapUtils.isNotEmpty(serviceBeanMap)) {
			for (Object serviceBean : serviceBeanMap.values()) {
				RpcService rpcService = serviceBean.getClass().getAnnotation(RpcService.class);
				// 获取 服务的实现类 类名
				String serviceName = rpcService.value().getName();
				String serviceVersion = rpcService.version();
				if (StringUtil.isNotEmpty(serviceVersion)) {
					serviceName += "-" + serviceVersion;
				}
				handlerMap.put(serviceName, serviceBean);
			}
		}
	}

	/**
	 * spring初始化bean之前做的操作
	 *
	 * @throws Exception
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		// 启动rpc服务器 & 注册rpc服务
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			// 创建并初始化 Netty 服务端 bootstrap 对象
			// netty quick start: https://netty.io/wiki/user-guide-for-4.x.html#wiki-h2-4
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeLine = ch.pipeline();
							// todo: 实现协议 编码 解码
							pipeLine.addLast(new RpcDecoder(RpcRequest.class)); // 解码 rpc 请求
							pipeLine.addLast(new RpcEncoder(RpcResponse.class)); // 编码 rpc 响应
							pipeLine.addLast(new RpcServerHandler(handlerMap)); // 处理 rpc 请求
						}
					})
					.option(ChannelOption.SO_BACKLOG, 1024)
					.childOption(ChannelOption.SO_KEEPALIVE, true);
			// 获取 rpc 服务器的 IP 地址与端口号
			String[] addressArray = StringUtil.split(serviceAddress, ":");
			String ip = addressArray[0];
			int port = Integer.parseInt(addressArray[1]);
			// 启动 rpc 服务器
			ChannelFuture future = bootstrap.bind(ip, port).sync();
			// 注册 rpc 服务
			if (serviceRegistry != null) {
				for (String interfaceName : handlerMap.keySet()) {  // 服务名：服务接口实现类类名
					serviceRegistry.register(interfaceName, serviceAddress);
					logger.debug("register service: {} => {}", interfaceName, serviceAddress);
				}
			}
			logger.debug("server started on port {}", port);
			// 关闭 rpc 服务
			future.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}

	}
}
