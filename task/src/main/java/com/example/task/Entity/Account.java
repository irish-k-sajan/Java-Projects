package com.example.task.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="account")
public class Account {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
    @Column(unique = true)
    private String ac_no;
    private String ac_owner;
	private double balance;
	public String getAC_no() {
		return ac_no;
	}
	public void setAC_no(String ac_no) {
		this.ac_no = ac_no;
	}
	public String getAc_owner() {
		return ac_owner;
	}
	public void setAc_owner(String ac_owner) {
		this.ac_owner = ac_owner;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
}
