package com.example.task.Controller;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.task.Entity.Account;
import com.example.task.Entity.Transaction;
import com.example.task.Services.AccountsService;

@RestController
@RequestMapping("/accounts")
public class AccountController {
	@Autowired
	AccountsService service;
	@PostMapping("/create-account")
	public String create(@RequestBody Account acc)
	{
		System.out.println("Received Account: " + acc.getAC_no());
		try {
			return service.create_account(acc);
		} catch (Exception e) {
			return e.getMessage();
		} 
		
	}
	@PutMapping("/transfer")
	public String Transfers(@RequestBody Transaction T) throws Exception
	{
		return service.transfer(T);
	}
	@GetMapping("/balance/{id}")
	public double accountBalance(@PathVariable String id)
	{
		return service.getBalance(id);
	}
	@GetMapping("/previous-transactions/{id}")
	public List<Transaction> previousTransaction(@PathVariable String id)
	{
		return service.prevTransactions(id);
	}

}
