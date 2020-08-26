package com.techm.Helper;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.techm.ServiceRenewalVelocity.LoggerDelegate;

public class HttpConnection {
	private final static Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());
	public static String httpConnection(String url,String method,String inputData,String username,String password,String token,String contentType) throws IOException{
		LOGGER.info("from HttpConnection started for URL: "+url);
		URL requestUrl;
		OutputStream output =null;
		DataInputStream input = null;
		try {
			requestUrl = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
			connection.setRequestMethod(method);
			
			connection.setRequestProperty("Content-Type", contentType);
			if(username!=null && password!=null){
				String encodeString =Base64.getEncoder().encodeToString((username+":"+password).getBytes(StandardCharsets.UTF_8));
				connection.setRequestProperty("Authorization", "Basic "+encodeString);
			}
			if(token!= null){
				connection.setRequestProperty("Authorization", "Bearer "+token);
			}
			connection.setDoOutput(true);
			
			if(inputData != null){
				output=connection.getOutputStream();
				output.write(inputData.getBytes("UTF-8"));
				output.flush();
				output.close();
			}
		if(connection.getResponseCode()==200 || connection.getResponseCode()==201 )
			input = new DataInputStream(connection.getInputStream());
		else throw new RuntimeException(streamToString(connection.getErrorStream()));
		
		}catch(ConnectException ex){
			ex.printStackTrace();
		}
		catch (IOException e) {
			
			e.printStackTrace();
		}
		
		LOGGER.info("from HttpConnection completed for URL: "+url);
		return streamToString(input);
		
	}
	
private static String streamToString(InputStream stream)throws IOException{
	BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
	String data = null;
	StringBuffer buffer = new StringBuffer();
	
	while((data = bufferedReader.readLine())!= null){
		buffer.append(data);
	}
	return buffer.toString();
	
}


public static String convertToXml(Object source, Class... type) {
    String result;
    StringWriter sw = new StringWriter();
    try {
        JAXBContext carContext = JAXBContext.newInstance(type);
        Marshaller carMarshaller = carContext.createMarshaller();
        carMarshaller.marshal(source, sw);
        result = sw.toString();
    } catch (JAXBException e) {
        throw new RuntimeException(e);
    }

    return result;
}
}
