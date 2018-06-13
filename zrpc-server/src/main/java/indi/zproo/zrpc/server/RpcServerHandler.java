package indi.zproo.zrpc.server;

import indi.zproo.zrpc.common.bean.RpcRequest;
import indi.zproo.zrpc.common.bean.RpcResponse;
import indi.zproo.zrpc.common.utils.StringUtil;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 服务端处理器（用于处理RPC请求）
 * writing a netty server,
 * we should start with a handler implementation, which handles I/O events generated by Netty.
 * https://netty.io/wiki/user-guide-for-4.x.html#wiki-h2-4
 *
 * @author: 张健(千昭)
 * @create: 2018-06-07 14:36
 **/
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

	private static final Logger logger = LoggerFactory.getLogger(RpcServerHandler.class);

	private final Map<String, Object> handlerMap;

	public RpcServerHandler(Map<String, Object> handlerMap) {
		this.handlerMap = handlerMap;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request)  {
		// todo: 创建并初始化 RPC 响应对象
		RpcResponse response = new RpcResponse();
		response.setRequestId(request.getRequestId());
		try {
			response.setResult(handle(request));
		} catch (Exception e) {
			logger.error("handle result failure", e);
			response.setException(e);
		}

		// 写入 rpc 响应对象并自动关闭连接
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	private Object handle(RpcRequest request) throws Exception {
		// 获取提供服务的对象
		String serviceName = request.getInterfaceName();
		String serviceVersion = request.getServiceVersion();
		if (StringUtil.isNotEmpty(serviceVersion)) {
			serviceName += "-" + serviceVersion;
		}
		// 去 handlerMap 中查找对应的服务对象
		Object serviceBean = handlerMap.get(serviceName);

		// 获取反射调用需要的参数
		Class<?> serviceClass = serviceBean.getClass();
		String methodName = request.getMethodName();
		Class<?>[] parameterTypes = request.getParameterTypes();
		Object[] parameters = request.getParameters();

		// 执行 java 反射调用
		Method method = serviceClass.getMethod(methodName, parameterTypes);
		method.setAccessible(true);
		return method.invoke(serviceBean, parameters);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("server caught exception", cause);
		ctx.close();
	}
}
