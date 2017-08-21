package ind.syu.webServiceInvoke.rmi.invoke;


import org.dom4j.Element;

public interface RmiInvoke {
	
	Element invoke(String className,String[] params)throws NoSuchInvokerException,NullRootElementException;

}
