package com.techm.ServiceRenewalVelocity;

import java.util.logging.Logger;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.json.JSONObject;
import org.json.XML;

import com.google.gson.JsonParser;
import com.techm.Helper.HttpConnection;
import com.techm.Models.MtxRequestSubscriberAdjustBalance;

public class DirectDebitFromBalanceDelegate implements JavaDelegate {

	private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());
	JsonParser parser = new JsonParser(); 
	@Override
	public void execute(DelegateExecution execution) throws Exception {

		LOGGER.info("direct debit from balance in DirectDebitFromBalanceDelegate started..");
		try{
		String amount = (String) execution.getVariable("amount");
		
		Double requestedAmount =Double.parseDouble(amount);
		LOGGER.info("PPM is direct debit from prepaid balance amount:"+requestedAmount); 
		
		MtxRequestSubscriberAdjustBalance adjustBalance = new MtxRequestSubscriberAdjustBalance();
		adjustBalance.setAdjustType(2);
		adjustBalance.setAmount(requestedAmount);
		adjustBalance.setReason("PPM is set prepaid-balance");
		
		String xmlRequestString=HttpConnection.convertToXml(adjustBalance, MtxRequestSubscriberAdjustBalance.class);
		LOGGER.info("direct debit from balance with input xml string data: "+xmlRequestString);
		
//		String url=" http://10.7.23.86:8080/rsgateway/data/v3/subscriber/0-1-5-386/wallet/1/adjustment";
		String subscriberId = (String) execution.getVariable("ocs_subscriber_id");

//		Setting the subscriber id to what OCS provided in case BM and OCS are not in sync
//		subscriberId = "0-1-5-386";
		
		String wallet = "1";
		String url = "http://10.7.23.86:8080/rsgateway/data/v3/subscriber/" + subscriberId + "/wallet/"+wallet+"/adjustment";
		LOGGER.info("OCS URL "+ url);
		
		String debitResponse =HttpConnection.httpConnection(url, "PUT", xmlRequestString, null, null, null, "application/xml");
		LOGGER.info("dirct debit Response as XML String : "+debitResponse);
		
		 JSONObject debitResponseJsonObject = XML.toJSONObject(debitResponse);
	       if(debitResponseJsonObject.has("MtxResponse")){
	    	   
		    	   JSONObject mtxResponseJsonObject =debitResponseJsonObject.getJSONObject("MtxResponse");
		        if(mtxResponseJsonObject.get("ResultText")!= null && mtxResponseJsonObject.get("ResultText").equals("OK"))
		        	 execution.setVariable("isPaymentSucess", Boolean.TRUE);
		        else
					execution.setVariable("isPaymentSucess", Boolean.FALSE);
	       }
		   else{
			   LOGGER.info("dirct debit Response not as expected.");
			execution.setVariable("isPaymentSucess", Boolean.FALSE);
		   }
	        
		}catch(Exception ex){
			LOGGER.info("error occoured in DirectDebitFromBalanceDelegate class");
			ex.printStackTrace();
		}
		LOGGER.info("direct debit from balance in DirectDebitFromBalanceDelegate completed..");
	}
	
}

