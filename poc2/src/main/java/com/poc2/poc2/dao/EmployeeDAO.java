package com.poc2.poc2.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.poc2.poc2.entity.Employee;

@Repository
public class EmployeeDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	public int insert(Employee emp)
	{
		String sql="INSERT INTO EMPLOYEE(name,role) VALUES(?,?)";
		return jdbcTemplate.update(sql,emp.getName(),emp.getRole());
	}
    public Employee findById(Long id) {
        String sql = "SELECT * FROM employee WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Employee.class), id);
    }

    public int update(Employee emp) {
    	if(emp.getName()!=null && emp.getRole()!=null)
    	{
        String sql = "UPDATE employee SET name = ?, role=? WHERE id = ?";
        return jdbcTemplate.update(sql, emp.getName(),emp.getRole(), emp.getId());
    	}
    	else if(emp.getName()!=null)
    	{
            String sql = "UPDATE employee SET name = ? WHERE id = ?";
            return jdbcTemplate.update(sql, emp.getName(),emp.getId());
    	}
    	else
    	{
            String sql = "UPDATE employee SET role=? WHERE id = ?";
            return jdbcTemplate.update(sql,emp.getRole() ,emp.getId());
    	}
    }
    public int deleteById(Long id) {
        String sql = "DELETE FROM employee WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public List<Employee> findAll() {
        String sql = "SELECT * FROM employee";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Employee.class));
    }

}
