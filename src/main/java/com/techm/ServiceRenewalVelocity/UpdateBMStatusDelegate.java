package com.techm.ServiceRenewalVelocity;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdateBMStatusDelegate implements JavaDelegate{
	
	private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
	LOGGER.info("updating BM status from UpdateBMStatusDelegate started....");
	
	JsonObject requestObject = new JsonObject();
	
	String subscriberId =(String) execution.getVariable("subscriber_id");
	Gson gson = new Gson();
	Boolean status = (Boolean) execution.getVariable("isPaymentSucess");
	
	String url= "https://demo-vnext-om.tmbmarble.com/product-inventory-business-service/v1/product-inventory/product/"+subscriberId;
//	String url= "https://demo-vnext-om.tmbmarble.com/product-inventory-business-service/v1/product-inventory/product/"+subscriberId;
	LOGGER.info("URL for BM Update status " + url);
	
	if(status==Boolean.TRUE){
		LOGGER.info("updating BM status as Active..");
		String date =ZonedDateTime.now( ZoneOffset.UTC ).plusMonths(1).format( DateTimeFormatter.ISO_INSTANT );
		requestObject.addProperty("status", "ACTIVE");
		requestObject.addProperty("contractEndDate",date);
		
	}
	else{
		LOGGER.info("updating BM status as suspended..");
		requestObject.addProperty("status", "SUSPENDED");
	}
	
//	String bmResponse =HttpConnection.httpConnection(url, "PATCH", gson.toJson(requestObject), null, null, null, "application/json");
	OkHttpClient client = new OkHttpClient().newBuilder()
	  .build();
	MediaType mediaType = MediaType.parse("application/json");
	RequestBody body = RequestBody.create(mediaType, requestObject.toString());
	Request request = new Request.Builder()
	  .url(url)
	  .method("PATCH", body)
	  .addHeader("Content-Type", "application/json")
	  .build();
	Response response = client.newCall(request).execute();
    LOGGER.info("BM status Update response: "+ response);
	
	LOGGER.info("updating BM status from UpdateBMStatusDelegate completed....");
	}
	
//	public static void main(String[] args) {
//		String subscriberId = "SUB1";
//		String url= "https://demo-vnext-om.tmbmarble.com/product-inventory-business-service/v1/product-inventory/product/"+subscriberId;
//		String date =ZonedDateTime.now( ZoneOffset.UTC ).plusMonths(1).format( DateTimeFormatter.ISO_INSTANT );
//		
//		
//		System.out.println("URL- "+ url);
//		System.out.println("Date - "+ date);
//		
//		JsonObject json = new JsonObject();
//		json.addProperty("status","ACTIVE");
//		json.addProperty("contractEndDate",date);
//		
//		String objJson = json.toString();
//		
//		System.out.println("objJson " + objJson);
//			
//		try {
//			OkHttpClient client = new OkHttpClient().newBuilder()
//					  .build();
//					MediaType mediaType = MediaType.parse("application/json");
//					RequestBody body = RequestBody.create(mediaType, objJson);
//					System.out.println("Body " + body);
//					Request request = new Request.Builder()
//					  .url(url)
//					  .method("PATCH", body)
//					  .addHeader("Content-Type", "application/json")
//					  .build();
//					Response response = client.newCall(request).execute();
//				    System.out.println("BM status Update response: "+ response);
//					
//				    System.out.println("updating BM status from UpdateBMStatusDelegate completed....");
//		} catch (Exception e) {
//			System.out.println(e.toString());
//		}
//		
//	}
}
