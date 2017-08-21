package ind.syu.webServiceInvoke.integratedServ;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class HttpClientCall {
	
	
	public static String invoke(String url,String requestXml){
		return invoke(url, requestXml, "");
	}
	
	public static String invoke(String url,String requestXml,String action){

        PostMethod postMethod = new PostMethod(url);
        byte[] b=null;
		try {
			b = requestXml.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        InputStream is = new ByteArrayInputStream(b, 0, b.length);

        RequestEntity re = new InputStreamRequestEntity(is, b.length,
                "text/xml");
                //application/soap+xml; charset=utf-8
        postMethod.setRequestEntity(re);
        //TODO
        postMethod.setRequestHeader("soapAction",action);
        postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
        HttpClient httpClient = new HttpClient();
        String soapResponseData=null;
        try {
            int statusCode = httpClient.executeMethod(postMethod);
            if(statusCode == 200) {
                soapResponseData = postMethod.getResponseBodyAsString();
            }
            else {
            	System.out.println("接口响应错误:代码"+statusCode);
            	soapResponseData=postMethod.getResponseBodyAsString();
            	System.out.println(soapResponseData);
            }		
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			postMethod.releaseConnection();
		}
		return soapResponseData;
	}

}
