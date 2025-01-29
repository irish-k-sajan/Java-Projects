package com.example.task.Scheduler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.task.Entity.Transaction;
import com.example.task.configure.DataSourceConfig;

@Component
public class TransactionScheduler {
	@Autowired
	private DataSourceConfig data = new DataSourceConfig();
	@Autowired
	private SqlSessionFactory sqlSession=data.sqlSessionFactory();
	@Scheduled(fixedDelay=5000)
	public void transactions()
	{
		List<String> ac=new ArrayList<String>();
		List<String> Status=new ArrayList<String>();
		Status.add("Pending");
		Status.add("Failed| due to runtime or compilation error");
			Random random=new Random();
		for(int i=1;i<=10;i++)
		{
			Transaction T=new Transaction();
			String sql="SELECT ac_no from Account";
			try( SqlSession session = sqlSession.openSession();
					Connection con = session.getConnection();
					PreparedStatement psql = con.prepareStatement(sql);)					
			{
				ResultSet r=psql.executeQuery();
				while(r.next())
				{
					ac.add(r.getString("ac_no"));					
				}
				
			}catch(Exception e)
			{
					System.out.println(e.getMessage());
			}
			T.setFrom_ac_no(ac.get(random.nextInt(ac.size())));
			T.setTo_ac_no(ac.get(random.nextInt(ac.size())));
			T.setAmount(random.nextDouble(10,100));
			T.setStatus(Status.get(random.nextInt(Status.size())));
			sql="INSERT INTO transactions(from_ac_no,to_ac_no,amount,status) values(?,?,?,?)";
			try( SqlSession session = sqlSession.openSession();
					Connection con = session.getConnection();
					PreparedStatement psql = con.prepareStatement(sql);)
			{
				psql.setString(1,T.getFrom_ac_no());
				psql.setString(2,T.getTo_ac_no());
				psql.setDouble(3,T.getAmount());
				psql.setString(4,T.getStatus());
				psql.executeUpdate();
				session.commit();
				
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}

			
		}
		System.out.println("All Inserted");
	}

}
