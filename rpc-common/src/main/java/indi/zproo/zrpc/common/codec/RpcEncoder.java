package indi.zproo.zrpc.common.codec;

import indi.zproo.zrpc.common.utils.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * RPC 编码器
 *
 * @author: zproo
 * @create: 2018-06-11 17:42
 **/
public class RpcEncoder extends MessageToByteEncoder {

	private Class<?> genericClass;

	public RpcEncoder(Class<?> genericClass) {
		this.genericClass = genericClass;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
		if (genericClass.isInstance(in)) {
			byte[] data = SerializationUtil.serialize(in);
			out.writeInt(data.length);
			out.writeBytes(data);
		}
	}

}
