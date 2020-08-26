package com.techm.ServiceRenewalVelocity;

import java.util.logging.Logger;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import com.techm.Helper.HttpConnection;
import com.techm.Models.MtxRequestSubscriberAdjustBalance;

public class UpdateOCSInfoDelegate implements JavaDelegate{
	private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
    
	LOGGER.info("updating OCS Information from UpdateOCSInfoDelegate started.....");
	
	String amount = (String) execution.getVariable("amount");
	String subscriberId = (String) execution.getVariable("ocs_subscriber_id");
	String wallet = "2";
	
	String url = "http://10.7.23.86:8080/rsgateway/data/v3/subscriber/" + subscriberId + "/wallet/"+wallet+"/adjustment";
	LOGGER.info("OCS URL for cancellation of service "+ url);
	
	Double requestedAmount =Double.parseDouble(amount);
	MtxRequestSubscriberAdjustBalance adjustBalance = new MtxRequestSubscriberAdjustBalance();
	
	LOGGER.info("updating OCS information for payment success..");
	adjustBalance.setAdjustType(1);
	adjustBalance.setAmount(requestedAmount);
	adjustBalance.setReason("Credit Into Non Usage Balance.");
		
	
	String xmlRequestString=HttpConnection.convertToXml(adjustBalance, MtxRequestSubscriberAdjustBalance.class);
	LOGGER.info("updating OCS info with input xml data: "+xmlRequestString);
	
//	String url="http://10.7.23.86:8080/rsgateway/data/v3/subscriber/0-1-5-386/wallet/2/adjustment";
	String ocsResponse =HttpConnection.httpConnection(url, "PUT", xmlRequestString, null, null, null, "application/xml");
	LOGGER.info("OCS Update Response Information: "+ocsResponse);
	LOGGER.info("updating OCS Information from UpdateOCSInfoDelegate completed.....");
	
	}
	
	
}