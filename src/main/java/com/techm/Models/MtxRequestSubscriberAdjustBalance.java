package com.techm.Models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="MtxRequestSubscriberAdjustBalance")
public class MtxRequestSubscriberAdjustBalance {

	private int adjustType;
	private Double amount;
	private String reason;
	
	@XmlElement(name="AdjustType")
	public int getAdjustType() {
		return adjustType;
	}
	public void setAdjustType(int adjustType) {
		this.adjustType = adjustType;
	}
	@XmlElement(name="Amount")
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	@XmlElement(name="Reason")
	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
}

