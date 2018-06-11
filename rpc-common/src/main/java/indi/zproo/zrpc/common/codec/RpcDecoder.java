package indi.zproo.zrpc.common.codec;

import indi.zproo.zrpc.common.utils.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author: zproo
 * @create: 2018-06-11 20:44
 **/
public class RpcDecoder extends ByteToMessageDecoder {

	private Class<?> genericClass;

	public RpcDecoder(Class<?> genericClass) {
		this.genericClass = genericClass;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

		if (in.readableBytes()<4) {
			return;
		}
		// todo: ? in.markReaderIndex();
		in.markReaderIndex();
		int dataLength = in.readInt();
		if (in.readableBytes() < dataLength) {
			in.resetReaderIndex();
			return;
		}
		byte[] data = new byte[dataLength];
		in.readBytes(data);
		out.add(SerializationUtil.deserialize(data, genericClass));
	}
}
