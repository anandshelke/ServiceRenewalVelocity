package com.techm.ServiceRenewalVelocity;

import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class UpdateOCSCancellationDelegate implements JavaDelegate{
	
	private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		
		LOGGER.info("Requesting OCS for service cancellation. UpdateOCSCancellationDelegate started.....");
		
		String subscriberId = (String) execution.getVariable("ocs_subscriber_id");

//		Setting the subscriber id to what OCS provided in case BM and OCS are not in sync
//		subscriberId = "0-1-5-386"; OR "0-1-5-416"
		
		String offers = "2";
		String url = "http://10.7.23.86:8080/rsgateway/data/v3/subscriber/" + subscriberId + "/offers/"+offers;
		LOGGER.info("OCS URL for cancellation of service "+ url);
		
		
		OkHttpClient client = new OkHttpClient().newBuilder()
		  .build();
		MediaType mediaType = MediaType.parse("text/plain");
		RequestBody body = RequestBody.create(mediaType, "");
		Request request = new Request.Builder()
//		  .url("http://10.7.23.86:8080/rsgateway/data/v3/subscriber/0-1-5-416/offers/2")
		  .url(url)
		  .method("DELETE", body)
		  .build();
		Response ocsResponse = client.newCall(request).execute();
		
//		String url="http://10.7.23.86:8080/rsgateway/data/v3/subscriber/0-1-5-416/offers/2";
//		String ocsResponse =HttpConnection.httpConnection(url, "DELETE", null, null, null, null, "application/xml");
		LOGGER.info("OCS cancellation Response Information: "+ ocsResponse.body().string());
		LOGGER.info("Cancellation of service in OCS completed. UpdateOCSCancellationDelegate completed.....");
		
	}

	
//	public static void main(String[] args) {
//
//
////		Setting the subscriber id to what OCS provided in case BM and OCS are not in sync
//		String subscriberId = "0-1-5-386";
//		String offers = "2";
//		String url = "http://10.7.23.86:8080/rsgateway/data/v3/subscriber/" + subscriberId + "/offers/"+offers;
//		System.out.println("OCS URL for cancellation of service "+ url);
//		
//		try {
//			OkHttpClient client = new OkHttpClient().newBuilder()
//					  .build();
//		MediaType mediaType = MediaType.parse("text/plain");
//		RequestBody body = RequestBody.create(mediaType, "");
//		Request request = new Request.Builder()
////					  .url("http://10.7.23.86:8080/rsgateway/data/v3/subscriber/0-1-5-416/offers/2")
//		  .url(url)
//		  .method("DELETE", body)
//		  .build();
//		Response ocsResponse = client.newCall(request).execute();
//		
////					String url="http://10.7.23.86:8080/rsgateway/data/v3/subscriber/0-1-5-416/offers/2";
////					String ocsResponse =HttpConnection.httpConnection(url, "DELETE", null, null, null, null, "application/xml");
//		System.out.println("OCS cancellation Response Information: "+ ocsResponse.body().string());
//
//		System.out.println("Cancellation of service in OCS completed. UpdateOCSCancellationDelegate completed.....");
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		
//	}
}
