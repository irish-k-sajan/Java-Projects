package com.example.cutomer.Entity;


public class Transaction {
	private String from_ac_no;
	private String to_ac_no;
	private double amount;
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
	

}
