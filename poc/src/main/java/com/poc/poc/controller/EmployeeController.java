package com.poc.poc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poc.poc.mapper.EmployeeMapper;
import com.poc.poc.model.Employee;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
	@Autowired
	EmployeeMapper empMapper;
	@PostMapping("/create-employee")
	public void CreateEmployee(@RequestBody Employee emp)
	{
		empMapper.CreateEmployee(emp);
	}
	@GetMapping("/get-all-employees")
	public List<Employee> FindAllEmployee()
	{
		return empMapper.FindAllEmployee();
	}
	@GetMapping("get-employees/{id}")
	public Employee FindEmployeeById(@PathVariable long id)
	{
		return empMapper.FindEmployeeById(id);
	}
	@DeleteMapping("delete-employees/{id}")
	public void DeleteEmployee(@PathVariable long id)
	{
		empMapper.DeleteEmployee(id);
	}
	@PutMapping("update-employees/{id}")
	public void UpdateEmployee(@RequestBody Employee emp,@PathVariable long id)
	{
		emp.setId(id);
		empMapper.UpdateEmployee(emp);
	}
}
