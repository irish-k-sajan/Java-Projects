package com.poc.poc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.poc.poc.model.Employee;

@Mapper
public interface EmployeeMapper {
	void CreateEmployee(Employee emp);
	void UpdateEmployee(Employee emp);
	void DeleteEmployee(long id);
	List<Employee> FindAllEmployee();
	Employee FindEmployeeById(long id);
	
}
