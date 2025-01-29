package com.example.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.example.task.Scheduler.scheduleHelloWorld;

@SpringBootApplication
@EntityScan(basePackages = "com.example.task.entity")
@EnableScheduling
public class TaskApplication {
	public static void main(String[] args) {
		
		SpringApplication.run(TaskApplication.class, args);
	}
	

}
