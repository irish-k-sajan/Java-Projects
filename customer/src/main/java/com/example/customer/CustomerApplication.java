package com.example.customer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.boot.CommandLineRunner;
import com.example.cutomer.Entity.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class CustomerApplication implements CommandLineRunner{
	ReentrantLock l=new ReentrantLock();

	public static void main(String[] args) {
		SpringApplication.run(CustomerApplication.class, args);
	}
	
	@Override
	public void run(String... args) {
		RestTemplate restTemplate=new RestTemplate();
		ReentrantLock l=new ReentrantLock();
		List<Transaction> T=Arrays.asList(
				new Transaction(){{
					setFrom_ac_no("234523435");
					setTo_ac_no("123456789");
					setAmount(1000);
				}},
				new Transaction() {{
					setFrom_ac_no("234523435");
					setTo_ac_no("123456789");
					setAmount(2000);
					
				}},
			new Transaction() {{
					setFrom_ac_no("234523435");
					setTo_ac_no("123456789");
					setAmount(6000);
					
				}},
				new Transaction() {{
					setFrom_ac_no("234523435");
					setTo_ac_no("456789123");
					setAmount(1000);
					
				}},
				new Transaction() {{
					setFrom_ac_no("456789123");
					setTo_ac_no("234523435");
					setAmount(1000);
					
				}},
				new Transaction() {{
					setFrom_ac_no("123456789");
					setTo_ac_no("456789123");
					setAmount(1000);
					
				}});
		final ExecutorService executorService = Executors.newFixedThreadPool(4);
		 T.parallelStream().forEach(transaction -> executorService.submit(() -> {
	            String url = "http://localhost:8081/customer/transfer";
	            restTemplate.put(url, transaction);
	        }));

	        executorService.shutdown();		
	}

}
