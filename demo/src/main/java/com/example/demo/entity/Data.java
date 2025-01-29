package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="data_")
public class Data {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String processed_data;
	public String getProcessed_data() {
		return processed_data;
	}
	public void setProcessed_data(String processed_data) {
		this.processed_data = processed_data;
	}

}
