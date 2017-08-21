package ind.syu.webServiceInvoke.rmi.invoke;

public class NoSuchInvokerException extends Exception{

	/**
	 * rmi调用接口没有对应类
	 */
	private static final long serialVersionUID = -2193757688786387447L;

	public NoSuchInvokerException(String msg){
		super(msg);
	}
}
