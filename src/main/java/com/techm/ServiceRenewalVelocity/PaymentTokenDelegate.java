package com.techm.ServiceRenewalVelocity;
 
import java.io.IOException;
import java.util.logging.Logger;
 
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.json.JSONArray;
import org.json.JSONObject;
 
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
 
import com.techm.Helper.HttpConnection;
 
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
 
 
public class PaymentTokenDelegate implements JavaDelegate {

	
              private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());
   
              @Override
              public void execute(DelegateExecution execution) throws Exception {
                  // TODO Auto-generated method stub
            	  String l_subscriberId = (String) execution.getVariable("subscriberId");
                           
            	  try {      
                      OkHttpClient client = new OkHttpClient().newBuilder()
                                           .build();
                  		Request request = new Request.Builder()
						.url("https://poc2.my.salesforce.com/services/apexrest/tmpayment/8013t000001RyzXAAS")
						.method("GET", null)
						.addHeader("Authorization", "Bearer 00D3t000000zjMk!AR4AQPBfYKVsqSv84fttHtiCGdo60mazD7LPljuc6USK18ltonlSBWoIAb.D_.Csn._iveTYNbWTyqx3IqCRDI9lZt6qemCz")
						.addHeader("Cookie", "BrowserId=mQlqhszWEeqJisXhOHpLNQ")
						.build();
                  		
                  		Response response = client.newCall(request).execute();
                  		String res = response.body().string();
                  		LOGGER.info("Response from API " + res);
                  		
		                 //Removing leading and trailing double quotes and also replacing \
		                 if (res.length() >= 2 && res.charAt(0) == '"' && res.charAt(res.length() - 1) == '"')
		                 {
		                     res = res.substring(1, res.length() - 1);
		                     res = res.replace("\\","");
		                 }
		                 
		                 JSONObject rootObj = new JSONObject(res);
		                 JSONArray paymentResponseArray = rootObj.getJSONArray("payment");       
		                 JSONObject paymentResponseJsonObject = paymentResponseArray.getJSONObject(0);
		                 System.out.println("paymentResponseJsonObject " + paymentResponseJsonObject);
	                        
		                 String l_payment_method = paymentResponseJsonObject.get("Type__c").toString();
		                 String l_payment_token = paymentResponseJsonObject.get("Token__c").toString();
	                
		                 LOGGER.info("Payment Method " + l_payment_method + " and token is " + l_payment_token);                      
	                     //Setting token to the token received from Velocity
	                     execution.setVariable("payment_method", l_payment_method);
	                     execution.setVariable("payment_token", l_payment_token);
	                     
	                     String x = (String) execution.getVariable("l_payment_method");
	                     LOGGER.info("The gateway checks for " + x);

	                     LOGGER.info("Received Payment Method for subscriber ID: "+ l_subscriberId +" is "+ l_payment_method +" and TOKEN will be: "+ l_payment_token);
                         LOGGER.info("Payment token delegate completed..");
                  
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
                   catch(Exception ex){
                             ex.printStackTrace();
                   }
                           
              }
             
//              public static void main(String[] args) {
//                  JsonParser parser = new JsonParser();
//                  Gson gson = new Gson();
//                  System.out.println("MAIN payment token delegate started..");
//                  try {      
//                       OkHttpClient client = new OkHttpClient().newBuilder()
//                                            .build();
//                   		Request request = new Request.Builder()
//						.url("https://poc2.my.salesforce.com/services/apexrest/tmpayment/8013t000001RyzXAAS")
//						.method("GET", null)
//						.addHeader("Authorization", "Bearer 00D3t000000zjMk!AR4AQPBfYKVsqSv84fttHtiCGdo60mazD7LPljuc6USK18ltonlSBWoIAb.D_.Csn._iveTYNbWTyqx3IqCRDI9lZt6qemCz")
//						.addHeader("Cookie", "BrowserId=mQlqhszWEeqJisXhOHpLNQ")
//						.build();
//                  Response response = client.newCall(request).execute();
//                  String res = response.body().string();
//                  System.out.println("Response from API " + res);
////                JSONObject rootObj = new JSONObject("{\"payment\":[{\"attributes\":{\"type\":\"TM_Payment_Detail__c\",\"url\":\"/services/data/v49.0/sobjects/TM_Payment_Detail__c/a773t000000fzyMAAQ\"},\"Token__c\":\"RE20072908044300387\",\"Order_Id__c\":\"8013t000001RyzXAAS\",\"Recurring__c\":true,\"Type__c\":\"Credit Card\",\"Id\":\"a773t000000fzyMAAQ\"}]}");
//
//                  //Removing leading and trailing double quotes and also replacing \
//                  if (res.length() >= 2 && res.charAt(0) == '"' && res.charAt(res.length() - 1) == '"')
//                  {
//                      res = res.substring(1, res.length() - 1);
//                      res = res.replace("\\","");
//                  }
//                  JSONObject rootObj = new JSONObject(res);
//                  JSONArray paymentResponseArray = rootObj.getJSONArray("payment");       
//                  JSONObject paymentResponseJsonObject = paymentResponseArray.getJSONObject(0);
//                  System.out.println("paymentResponseJsonObject " + paymentResponseJsonObject);
//                         
//                  String payment_method = paymentResponseJsonObject.get("Type__c").toString();
//                  String payment_token = paymentResponseJsonObject.get("Token__c").toString();
//                 
//                  System.out.println("Method " + payment_method + " and token is " + payment_token);
//                                         
//                                         
//                            } catch (IOException e) {
//                                          // TODO Auto-generated catch block
//                                          e.printStackTrace();
//                            }
//                   }
// 
              }
 

