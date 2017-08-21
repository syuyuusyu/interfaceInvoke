package ind.syu.webServiceInvoke.invoke;

import ind.syu.webServiceInvoke.event.InvokeCompleteEvent;
import ind.syu.webServiceInvoke.integratedServ.HttpClientCall;
import ind.syu.webServiceInvoke.integratedServ.ThreadResultData;
import ind.syu.webServiceInvoke.rmi.invoke.NullRootElementException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Element;



public abstract class InvokeBase implements Runnable{
	private static Logger log = Logger.getLogger(InvokeBase.class);
	protected  String[] placeholder;
	protected  String servCode;
	protected  String desc;	
	protected String requestXml;
	protected String url;
	protected String interfaceCode;
	protected String responseXml;
	
	protected String[] params;
	protected Date d=new Date();


	public void setParams(String[] params) {
		this.params = params;
	}

	
	protected String time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(d);
	protected String MsgId="sscp"+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(d);
	protected Element rootElement;
	protected ThreadResultData resultData;
	
	protected final List<InvokeCompleteEvent> events=new ArrayList<InvokeCompleteEvent>();
	
	public String getInterfaceCode() {
		return interfaceCode;
	}

	public InvokeBase(){}
	public InvokeBase(String[] params){
		this.params=params;
	}
	
	public void addEvent(InvokeCompleteEvent e){
		events.add(e);
	}
	
	public ThreadResultData getResultData() {
		return resultData;
	}

	public void setResultData(ThreadResultData resultData) {
		this.resultData = resultData;
	}

	@Override
	public void run(){
		invoke();
		try {
			fireEvent();
			resultData.addResult(interfaceCode,this.getRootElement());
		} catch (NullRootElementException e) {
			e.printStackTrace();
		}
		
	}
	
	private void fireEvent() throws NullRootElementException{
		for (int i = 0; i < events.size(); i++) {
			final Element e=this.getRootElement();
			final int index=i;
			resultData.getFixedThreadPool().execute(new Runnable() {				
				@Override
				//并发调用子流程
				public void run() {
					events.get(index).exec(e);					
				}
			});	
		}
	}

	
	public final String invoke(){
		replaceHead();
		replaceParam();
		saveLog();
		beforeCall();
		System.out.println("调用接口:"+interfaceCode);
		this.responseXml=HttpClientCall.invoke(url, requestXml);
		afterCall();
		return this.responseXml;
	}
	
	public String getResponseXml() {
		return responseXml;
	}


	protected void replaceHead(){		
		Pattern patternT = Pattern.compile("(?<=<Time>)(.+?)(?=</Time>)");
		Matcher matcherT = patternT.matcher(requestXml);
		while(matcherT.find()){
			requestXml=StringUtils.replace(requestXml, matcherT.group(), time);
		}
		Pattern patternM = Pattern.compile("(?<=<MsgId>)(.+?)(?=</MsgId>)");
		Matcher matcherM = patternM.matcher(requestXml);
		while(matcherM.find()){
			requestXml=StringUtils.replace(requestXml, matcherM.group(), MsgId);
		}
	}
	
	protected void replaceParam(){
		for (int i = 0; i < placeholder.length; i++) {
			requestXml=StringUtils.replace(requestXml, placeholder[i], params[i]);
		}
	}
	
	protected void afterCall(){}
	protected void beforeCall(){}
	protected final void saveLog(){
	
	}
	
	protected abstract Element initRootElement();
	
	public Element getRootElement() throws NullRootElementException{
		if(this.rootElement==null){
			this.rootElement=this.initRootElement();
		}
		if(this.rootElement==null){
			log.error(responseXml);
			throw new NullRootElementException(this.interfaceCode+"返回报文回去根元素异常");
		}
		return this.rootElement;
	}
	
	public String getInfo(String xpath){
		Element e=null;
		try {
			e = (Element) getRootElement().selectSingleNode(xpath);
		} catch (NullRootElementException e1) {
			e1.printStackTrace();
		}
		return e.getTextTrim();
	}
	
}
