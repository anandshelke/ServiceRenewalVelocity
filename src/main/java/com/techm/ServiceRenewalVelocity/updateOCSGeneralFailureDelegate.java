package com.techm.ServiceRenewalVelocity;

import java.util.logging.Logger;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class updateOCSGeneralFailureDelegate implements JavaDelegate{
	
private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
	LOGGER.info("General Failure. Please connect with support team");
	}

}