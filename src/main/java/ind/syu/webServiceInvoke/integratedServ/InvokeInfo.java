package ind.syu.webServiceInvoke.integratedServ;

import java.util.List;

public class InvokeInfo {
	private String requestXml;
	private List<String> replaceStr;
	private String rootXpath;
	private String nameSpace;
	private String url;
	private String servCode;
	private String desc;
	private String placeholderStr;
	private String interfaceCode;
	public String getRequestXml() {
		return requestXml;
	}
	public void setRequestXml(String requestXml) {
		this.requestXml = requestXml;
	}
	public List<String> getReplaceStr() {
		return replaceStr;
	}
	public void setReplaceStr(List<String> replaceStr) {
		this.replaceStr = replaceStr;
	}
	public String getRootXpath() {
		return rootXpath;
	}
	public void setRootXpath(String rootXpath) {
		this.rootXpath = rootXpath;
	}
	public String getNameSpace() {
		return nameSpace;
	}
	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getServCode() {
		return servCode;
	}
	public void setServCode(String servCode) {
		this.servCode = servCode;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getPlaceholderStr() {
		return placeholderStr;
	}
	public void setPlaceholderStr(String placeholderStr) {
		this.placeholderStr = placeholderStr;
	}
	public String getInterfaceCode() {
		return interfaceCode;
	}
	public void setInterfaceCode(String interfaceCode) {
		this.interfaceCode = interfaceCode;
	}
	
	
}
