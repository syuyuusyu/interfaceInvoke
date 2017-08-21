package ind.syu.webServiceInvoke.invoke;

import java.util.List;

import ind.syu.webServiceInvoke.integratedServ.InvokeInfo;
import ind.syu.webServiceInvoke.util.DocumentUtil;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Element;

public class InvokeBaseBean extends InvokeBase{
	private static Logger log = Logger.getLogger(InvokeBaseBean.class);
	private String nameSpace;
	private String rootXpath;
	private List<String> replaceStr;
	
	private InvokeInfo invokeInfo;
	
	public InvokeBaseBean(){}

	@Override
	protected Element initRootElement() {
		return DocumentUtil.getRoot(responseXml, nameSpace, rootXpath);
	}
	@Override
	protected void afterCall(){
		responseXml = StringEscapeUtils.unescapeHtml(responseXml);
		for (String str : replaceStr) {
			responseXml=StringUtils.replace(responseXml, str, "");
		}
	}
	
	
	
	public InvokeInfo getInvokeInfo() {
		return invokeInfo;
	}

	public void setInvokeInfo(InvokeInfo invokeInfo) {
		log.info("初始化接口调用信息--"+invokeInfo.getDesc());
		this.invokeInfo = invokeInfo;
		
		this.interfaceCode=this.invokeInfo.getInterfaceCode();
		this.placeholder=this.invokeInfo.getPlaceholderStr().split(",");
		this.servCode=this.invokeInfo.getServCode();
		this.desc=this.invokeInfo.getDesc();
		this.nameSpace=this.invokeInfo.getNameSpace();
		this.rootXpath=this.invokeInfo.getRootXpath();
		this.requestXml=this.invokeInfo.getRequestXml();
		this.url=this.invokeInfo.getUrl();
		this.replaceStr=this.invokeInfo.getReplaceStr();
		
	}


	
}
