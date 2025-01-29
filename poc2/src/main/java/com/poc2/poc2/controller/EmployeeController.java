package com.poc2.poc2.controller;

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

import com.poc2.poc2.dao.EmployeeDAO;
import com.poc2.poc2.entity.Employee;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private EmployeeDAO employeeDAO;

    @GetMapping("/all")
    public List<Employee> getAllEmployees() {
        return employeeDAO.findAll();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return employeeDAO.findById(id);
    }

    @PostMapping("/create-employee")
    public int createEmployee(@RequestBody Employee emp) {
        return employeeDAO.insert(emp);
    }

    @PutMapping("/update-employee/{id}")
    public int updateEmployee(@PathVariable Long id, @RequestBody Employee emp) {
        emp.setId(id);
        return employeeDAO.update(emp);
        }

    @DeleteMapping("/delete-employee/{id}")
    public int deleteEmployee(@PathVariable Long id) {
        return employeeDAO.deleteById(id);
    }

}
