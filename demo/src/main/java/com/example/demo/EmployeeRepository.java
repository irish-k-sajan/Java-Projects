package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepository extends JpaRepository<Employee,Long>{
	@Query(value="Select * from Employee",nativeQuery=true)
	List<Employee> findALLEmployees();


}
