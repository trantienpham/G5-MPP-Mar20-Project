package business;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Fine implements Serializable{
	private Date fineDate;
	private double amount;
	
	public Date getFineDate() {
		return this.fineDate;
	}
	
	public void setFineDate(Date fineDate) {
		this.fineDate = fineDate;
	}
	
	public double getAmount() {
		return this.amount;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}

}