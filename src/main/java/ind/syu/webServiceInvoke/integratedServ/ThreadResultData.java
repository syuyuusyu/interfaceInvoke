package ind.syu.webServiceInvoke.integratedServ;

import ind.syu.webServiceInvoke.exception.InvokeTimeOutException;
import ind.syu.webServiceInvoke.invoke.InvokeBase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Element;
/**
 * 
 * @author syu
 * 并发调用时用来接收结果的结果集
 */
public  class ThreadResultData {
	private static Logger log = Logger.getLogger(ThreadResultData.class);
	private Map<String, Element> resultMap=new HashMap<String, Element>();
	private Map<String, Object> someThingMap=new HashMap<String, Object>();
	private List<InvokeBase> invoker=new ArrayList<InvokeBase>();
	private int count=0;
	private int current=0;
	private static ExecutorService fixedThreadPool = Executors.newCachedThreadPool();
	public ExecutorService getFixedThreadPool() {
		return fixedThreadPool;
	}


	private Long timeOut;
	private int threadPoolCapacity;
	
	public ThreadResultData(){
		this.timeOut=10000L;
		threadPoolCapacity=100;
	}
	
	

	public Long getTimeOut() {
		return timeOut;
	}



	public void setTimeOut(Long timeOut) {
		this.timeOut = timeOut;
	}



	public int getThreadPoolCapacity() {
		return threadPoolCapacity;
	}

	public void setThreadPoolCapacity(int threadPoolCapacity) {
		this.threadPoolCapacity = threadPoolCapacity;
	}

	public void addInvoker(InvokeBase invoker){
		this.invoker.add(invoker);
		invoker.setResultData(this);
		this.increaseCount();
		fixedThreadPool.execute(invoker);
	}
	

	
	
	public void addResult(String interfaceCode,Element e){
		increaseCurrent();
		resultMap.put(interfaceCode, e);
	}
	
	private synchronized void increaseCurrent(){
		this.current++;
	}
	
	private synchronized void increaseCount(){
		this.count++;
	}
	
	private synchronized int getCurrent(){
		return this.current;
	}
	
	public synchronized int getCount(){
		return this.count;
	}

	
	public Element getElement(String interfaceCode){		
		return resultMap.get(interfaceCode);
	}
	
	public String getInfo(String interfaceCode,String xpath){
		Element e=getElement(interfaceCode);
		return (e.selectSingleNode(xpath).getText()).trim();
	}
	
	public void waitForResult() throws InvokeTimeOutException{
		long currentTime=System.currentTimeMillis();
		while (true) {
			if (this.getCurrent()==this.getCount()) {
				break;
			}
			if(System.currentTimeMillis()-currentTime>timeOut){
				log.error(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTime));
				List<String> errorResult=new ArrayList<String>();
				for (int i = 0; i < invoker.size(); i++) {
					log.error("**"+invoker.get(i).getInterfaceCode()+"***");
					log.error(invoker.get(i).getResponseXml());
					if(invoker.get(i).getResponseXml()==null){
						errorResult.add(invoker.get(i).getInterfaceCode());
						log.error("*************接口调用超时**********");
						log.error("*************"+invoker.get(i).getInterfaceCode()+"********");
					}
				}
				String error="";
				for (int i = 0; i < errorResult.size(); i++) {
					error=errorResult.get(i)+" "+error;
				}
				if(!StringUtils.isEmpty(error)){
					throw new InvokeTimeOutException("调用接口"+error+"超时");
				}else{
					return;
				}			
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void putSometing(String key,Object value){
		this.someThingMap.put(key, value);
	}
	
	public Object getSomething(String key){
		return this.someThingMap.get(key);
	}
	
}
