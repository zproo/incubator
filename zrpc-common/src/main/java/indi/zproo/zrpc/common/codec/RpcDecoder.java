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

		// 检查是否至少有4个字节可读
		if (in.readableBytes()<4) {
			return;
		}
		// 标记读指针位置，以便可以回滚指针
		in.markReaderIndex();
		int dataLength = in.readInt();
		if (in.readableBytes() < dataLength) {
			// 如果读取的包长度不够 dataLength，回滚指针不做处理，等待下个包再解析
			in.resetReaderIndex();
			return;
		}
		byte[] data = new byte[dataLength];
		in.readBytes(data);
		out.add(SerializationUtil.deserialize(data, genericClass));
	}
}
