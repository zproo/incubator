package indi.zproo.zrpc.common.bean;

/**
 * @author: zproo
 * @create: 2018-06-11 17:36
 **/
public class RpcResponse {
	private String requestId;
	private Exception exception;
	private Object result;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}
