package indi.zproo.zrpc.common.bean;

/**
 * 封装rpc请求
 *
 * @author: zproo
 * @create: 2018-06-07 20:42
 **/
public class RpcRequest {
	/**
	 * 请求id
	 */
	private String requestId;

	/**
	 * 服务接口名
	 */
	private String interfaceName;

	/**
	 * 服务版本
	 */
	private String serviceVersion;

	/**
	 * 请求服务的方法名
	 */
	private String methodName;

	/**
	 *
	 */
	private Class<?>[] parameterTypes;

	/**
	 *
	 */
	private Object[] parameters;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getServiceVersion() {
		return serviceVersion;
	}

	public void setServiceVersion(String serviceVersion) {
		this.serviceVersion = serviceVersion;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}
}
