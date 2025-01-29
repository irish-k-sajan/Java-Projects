package com.example.demo.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.csv.CSVFormat;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Employee;
import com.example.demo.configure.DataSourceConfig;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
public class EmployeeService {

	@Autowired
	private DataSourceConfig data=new DataSourceConfig();
	@Autowired
	private SqlSessionFactory sqlSession;
	EmployeeService() throws Exception
	{
		sqlSession=data.sqlSessionFactory();
	}

    public Employee getEmployee(long id){
        String sql="SELECT * From Employee where id=?";
        Employee emp=null;
        try(SqlSession session= sqlSession.openSession();
        Connection con=session.getConnection();
        PreparedStatement psql=con.prepareStatement(sql);)
        {
        psql.setLong(1, id);
        ResultSet r=psql.executeQuery();
        if(r.next()) {
        	emp=new Employee();
        	emp.setName(r.getString("name"));
        	emp.setRole(r.getString("role"));
        	emp.setId(id);
        }
        }catch(SQLException e)
    	{
    		System.err.println("SQLException: "+ e.getMessage());
    	}
        return emp;        
        
    }
    public List<Employee> getALL()
    {
    	String sql="SELECT * FROM EMPLOYEE";
    	List<Employee>emps =new ArrayList<Employee>();
    	try(
        SqlSession session= sqlSession.openSession();
        Connection con=session.getConnection();
        PreparedStatement psql=con.prepareStatement(sql);
    			){
        ResultSet r=psql.executeQuery();
        while(r.next()) {
        	Employee emp=new Employee();
        	emp.setName(r.getString("name"));
        	emp.setRole(r.getString("role"));
        	emp.setId(r.getLong("id"));
        	emps.add(emp);
        }
    	}catch(SQLException e)
    	{
    		System.err.println("SQLException: "+ e.getMessage());
    	}
        return emps;        

    }
    public boolean validate_role(String role) {
    	String sql="SELECT role_name FROM roles";
    	List<String> roles=new ArrayList<String>();
    	try(
    	        SqlSession session= sqlSession.openSession();
    	        Connection con=session.getConnection();
    	        PreparedStatement psql=con.prepareStatement(sql);
    	){
    		ResultSet r=psql.executeQuery();
    		while(r.next())
    		{
    			roles.add(r.getString("role_name"));
    		}
    	}catch(SQLException e)
    	{
    		System.err.println("SQLException: "+ e.getMessage());
    	}
    	return roles.stream().filter(r->r.equals(role)).count() != 0;
    	
    }
    public boolean validate_name(String name)
    {
    	if(name.length()==1)
    		return false;
    	char[] chars = new char[name.length()];
    	name.getChars(0, name.length(),chars, 0);
		for(char ch : chars)
    	{
    		if(Character.isDigit(ch))
    			return false;
    	}
		return true;
    }
    public String updateemp(Employee emp)
    {
    	String sql="";
    	if(emp.getName()!=null && emp.getRole()!=null)
    	{
    		if(validate_role(emp.getRole())&&validate_name(emp.getName()))
    		{
    		sql="UPDATE EMPLOYEE SET name=?,role=? WHERE id=?";
    		}
    		else
    			return "Incorrect Role or Name";
    		
    	}
    	else if(emp.getName()!=null)
    	{
    		if(validate_name(emp.getName()))
    		{
    		sql="UPDATE EMPLOYEE SET name=? WHERE id=?";
    		}
    		else {
    			return "Incorrect format of name";
    		}
    	}
    	else
    	{
    		if(validate_role(emp.getRole()))
    		{
    		sql="UPDATE EMPLOYEE SET role=? WHERE id=?";
    		}
    		else
    			return "Incorrect Role";
    	}
    	SqlSession session=null;
    	Connection con=null;
    	PreparedStatement psql=null;
    	try {
    	session=sqlSession.openSession();
    	con=session.getConnection();
    	psql=con.prepareStatement(sql);
        if (emp.getName() != null && emp.getRole() != null) {
        	if(validate_role(emp.getRole()))
            psql.setString(1, emp.getName());
            psql.setString(2, emp.getRole());
            psql.setLong(3, emp.getId());
        } else if (emp.getName() != null) {
            psql.setString(1, emp.getName());
            psql.setLong(2, emp.getId());
        } else {
            psql.setString(1, emp.getRole());
            psql.setLong(2, emp.getId());
        }
        psql.executeUpdate();
    	}
    	catch(SQLException e)
    	{
    		System.err.println("SQLException: "+e.getMessage());
    	}
    	finally {
    		if(psql!=null)
    		try {
    			psql.close();
    		}catch(SQLException e)
        	{
        		System.err.println("SQLException: "+e.getMessage());
        	}
    		if(con!=null)
    		try {
    			con.close();
    		}catch(SQLException e)
        	{
        		System.err.println("SQLException: "+e.getMessage());
        	}
    		if(session!=null)
    			session.close();
    	}
        return "Updated"; 	
    }
	public void insert_data(MultipartFile file) throws IOException {
		String sql = "insert into data_(processed_data) values(?)";
		List<Map<String, String>> data = new ArrayList<>();
		String type = file.getContentType();
		if (type.equals("application/json")) {
			data = processJson(file);
		} else if (type.equals("text/csv")) {
			data = processCsv(file);
		} else if (type.equals("application/vnd.ms-excel")
				|| type.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
			data = processExcel(file);
		}
		String processed_data = data.toString();
		try (SqlSession session = sqlSession.openSession();
				Connection con = session.getConnection();
				PreparedStatement psql = con.prepareStatement(sql);) {
			psql.setString(1, processed_data);
			psql.executeUpdate();
			session.commit();

		} catch (SQLException e) {
			System.err.println("SQLException: " + e.getMessage());
		}
	}

	private List<Map<String, String>> processJson(MultipartFile file) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		List<Map<String, String>> records = objectMapper.readValue(file.getInputStream(),
				new TypeReference<List<Map<String, String>>>() {
				});
		return records.stream().collect(Collectors.toList());
	}

	private List<Map<String, String>> processCsv(MultipartFile file) throws IOException {
		Reader reader = new InputStreamReader(file.getInputStream());
		List<Map<String, String>> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader).getRecords().stream().map(record -> {
			Map<String, String> map = new HashMap<>();
			record.toMap().forEach(map::put);
			return map;
		}).collect(Collectors.toList());
		return records;
	}

	private List<Map<String, String>> processExcel(MultipartFile file) throws IOException {
		Workbook workbook = new XSSFWorkbook(file.getInputStream());
		Sheet sheet = workbook.getSheetAt(0);
		List<Map<String, String>> records = StreamSupport.stream(sheet.spliterator(), false).skip(1).map(row -> {
			Map<String, String> map = new HashMap<>();
			for(Cell cell:row)
			{
				String value= getValue(cell);
				map.put(sheet.getRow(0).getCell(cell.getColumnIndex()).toString(), value);
			}
			return map;
		}).collect(Collectors.toList());
		workbook.close();
		return records;
	}

	private String getValue(Cell cell) {
		switch (cell.getCellType()) {
        case STRING:
            return cell.getStringCellValue();
        case NUMERIC:
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue().toString();
            } else {
                return Double.toString(cell.getNumericCellValue());
            }
        case BOOLEAN:
            return Boolean.toString(cell.getBooleanCellValue());
        case FORMULA:
            return cell.getCellFormula();
        case BLANK:
            return "";
        default:
            return "Unknown Cell Type";
    }
	}
}