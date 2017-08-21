package ind.syu.webServiceInvoke.exception;

public class InvokeTimeOutException extends Exception {
	
	/**
	 * 并发调用接口超时异常
	 */
	private static final long serialVersionUID = 4331629456691366618L;

	public InvokeTimeOutException(String msg){
		super(msg);
	}
}
