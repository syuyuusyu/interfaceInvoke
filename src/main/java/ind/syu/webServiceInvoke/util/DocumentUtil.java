package ind.syu.webServiceInvoke.util;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.xpath.DefaultXPath;

public class DocumentUtil {
	
	public static Element getRoot(Document doc,String nameSpace,String xPath){
		Map<String, String> ns = new HashMap<String, String>();
		ns.put("ns",nameSpace);
		XPath x=new DefaultXPath(xPath);
		x.setNamespaceURIs(ns);
		return (Element) x.selectSingleNode(doc);
	}
	
	public static Element getRoot(String xml,String nameSpace,String xPath){
		Document doc=null;
		try {
			doc=DocumentHelper.parseText(xml);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		XPath x=new DefaultXPath(xPath);
		if (nameSpace!=null) {
			Map<String, String> ns = new HashMap<String, String>();
			ns.put("ns",nameSpace);
			x.setNamespaceURIs(ns);			
		}
		return (Element) x.selectSingleNode(doc);
	}

}
