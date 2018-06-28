package indi.zproo.zrpc.client;

import indi.zproo.zrpc.common.bean.RpcRequest;
import indi.zproo.zrpc.common.bean.RpcResponse;
import indi.zproo.zrpc.common.codec.RpcDecoder;
import indi.zproo.zrpc.common.codec.RpcEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author: 千昭
 * @create: 2018-06-26 20:43
 **/
public class RpcClient extends SimpleChannelInboundHandler<RpcResponse> {

    private final String host;

    private final int port;

    private RpcResponse response;

    public RpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public RpcResponse send(RpcRequest request) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(host, port))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new RpcEncoder(RpcRequest.class));
                        pipeline.addLast(new RpcDecoder(RpcResponse.class));
                        pipeline.addLast(RpcClient.this);
                    }
                })
                .option(ChannelOption.TCP_NODELAY, true);
            // 连接 rpc 服务器
            ChannelFuture future = bootstrap.connect(host, port).sync();
            //写入 rpc 请求数据并关闭连接
            Channel channel = future.channel();
            channel.writeAndFlush(request).sync();
            channel.closeFuture().sync();
            // 返回 rpc 响应对象
            return response;
        } finally {
            group.shutdownGracefully();
        }
    }

    /**
     * 接收消息后的客户端逻辑
     *
     * @param channelHandlerContext
     * @param rpcResponse
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) throws Exception {
        this.response = rpcResponse;
    }
}
