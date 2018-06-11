package indi.zproo.zrpc.common.utils;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 序列化工具（基于 protostuff 实现）
 *
 * @author: zproo
 * @create: 2018-06-11 17:44
 **/
public class SerializationUtil {

	/**
	 * 使用多线程、高并发的 ConcurrentHashMap,作为schema缓存
	 */
	private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();

	/**
	 * 使用缓存
	 */
	private static Objenesis objenesis = new ObjenesisStd(true);

	/**
	 * 避免类被实例化
	 */
	private SerializationUtil() {
	}

	/**
	 * 序列化（对象 -> 字节数组）
	 */
	public static <T> byte[] serialize(T obj) {
		Class<T> cls = (Class<T>) obj.getClass();
		// Re-use (manage) this buffer to avoid allocating on every serialization
		LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
		// ser
		final byte[] protostuff;
		try {
			// The getSchema method is also thread-safe
			Schema<T> schema = getSchema(cls);
			protostuff = ProtostuffIOUtil.toByteArray(obj, schema, buffer);
			return protostuff;
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		} finally {
			buffer.clear();
		}
	}

	/**
	 * 反序列化（字节数组 -> 对象）
	 */
	public static <T> T deserialize(byte[] data, Class<T> cls) {
		try {
			// deser
			T objParsed = objenesis.newInstance(cls);
			Schema<T> schema = getSchema(cls);
			ProtostuffIOUtil.mergeFrom(data, objParsed, schema);
			return objParsed;
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	/**
	 * 使用缓存获取 scheme
	 */
	private static <T> Schema<T> getSchema(Class<T> cls) {

		Schema<T> schema = (Schema<T>) cachedSchema.get(cls);
		if (schema == null) {
			schema = RuntimeSchema.getSchema(cls);
			cachedSchema.put(cls, schema);
		}
		return schema;
	}

}
