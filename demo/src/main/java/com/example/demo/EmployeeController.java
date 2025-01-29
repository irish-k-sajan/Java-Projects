package com.example.demo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.EmployeeService;

@RestController
public class EmployeeController {
	@Autowired
	private EmployeeRepository employeerepository;
	@Autowired
	private EmployeeService service;
	@PostMapping("/create-employees")
	public String createEmployee(@RequestBody List<Employee> emp) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
            	emp.stream().forEach(employee->{
            		employeerepository.save(employee);
            	});
                
            }
        });
        thread.start();
        return "Data is getting saved";
       	}
	@GetMapping("/all-employees")
	public List<Employee> retrieveAllEmployees(){
		return employeerepository.findAll();	
	}
	@GetMapping("/employees/{id}")
	public Employee retrieveEmployees(@PathVariable long id){
		return employeerepository.findById(id).orElse(null)	;
	}
	@DeleteMapping("/delete-employee/{id}")
	public void deleteEmployee(@PathVariable long id) {
		employeerepository.deleteById(id);
	}
	@PatchMapping("/update-employee/{id}")
	public Employee updateEmployee(@PathVariable long id,@RequestBody Map<String,Object> updates){
		return employeerepository.findById(id).map(employee->{
			updates.forEach((key,value)->{
				switch(key) {
				case "name":
					employee.setName((String) value);
					break;
				case "role":
					employee.setRole((String) value);
					break;
				}
			});
			return employeerepository.save(employee);
		}).orElse(null);
	}
	@GetMapping("/native")
	public List<Employee> getAll(){
		return employeerepository.findALLEmployees();
	}
	@GetMapping("/emp/{id}")
	public Employee emp(@PathVariable long id)
	{
		return service.getEmployee(id);
	}
	@GetMapping("/empall")
	public List<Employee> emps() 
	{
		return service.getALL();
	}
	@PutMapping("/emp-update/{id}")
	public String update_emp(@PathVariable Long id,@RequestBody Map<String,Object> updates)
	{
		Employee emp= new Employee();
		updates.forEach((key,value)->{
			switch(key) {
			case "name": 
				emp.setName((String) value);
				break;
			case "role":
				emp.setRole((String) value);
				break;
			
			}
		});
		emp.setId(id);
		return service.updateemp(emp);
		 
	}
	@PostMapping("/data")
	public String insert_data(@RequestBody MultipartFile file)
	{
		try {
			service.insert_data(file);
		}catch(Exception e)
		{
			return e.getMessage();
		}
		return "Inserted";
	}
}
;