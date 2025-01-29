package com.example.task.Entity;

import jakarta.annotation.Generated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Transaction {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String from_ac_no;
	private String to_ac_no;
	private double amount;
	private String status;
	 public long getId() {
	        return id;
	    }
	 public void setId(Long id)
	 {
		 this.id=id;
	 }
	public String getFrom_ac_no() {
		return from_ac_no;
	}
	public void setFrom_ac_no(String from_ac_no) {
		this.from_ac_no = from_ac_no;
	}
	public String getTo_ac_no() {
		return to_ac_no;
	}
	public void setTo_ac_no(String to_ac_no) {
		this.to_ac_no = to_ac_no;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	

}
