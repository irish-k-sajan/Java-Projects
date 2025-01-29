package com.example.task.Services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.task.DAO.Transfer;
import com.example.task.Entity.Account;
import com.example.task.Entity.Transaction;
import com.example.task.configure.DataSourceConfig;

@Service
public class AccountsService {
	@Autowired
	private Transfer trans;
	@Autowired
	private DataSourceConfig data = new DataSourceConfig();
	@Autowired
	private SqlSessionFactory sqlSession;
	private final RestTemplate restTemplate;
	private final ReentrantLock re = new ReentrantLock();
	private final ExecutorService executorService = Executors.newFixedThreadPool(4);
	public AccountsService(RestTemplate restTemplate) throws Exception {
		sqlSession = data.sqlSessionFactory();
		trans= new Transfer(sqlSession);
		this.restTemplate = restTemplate;
	}

	public String create_account(Account acc) throws Exception {
		return executorService.submit(() -> {
			re.lock();
			try {
				
			String sql = "INSERT INTO Account(ac_no,ac_owner,balance) VALUES(?,?,?)";
			
			if (acc.getAC_no() != null && acc.getAc_owner() != null) {
				try (SqlSession session = sqlSession.openSession();
						Connection con = session.getConnection();
						PreparedStatement psql = con.prepareStatement(sql);) {
					psql.setString(1, acc.getAC_no());
					psql.setString(2, acc.getAc_owner());
					psql.setDouble(3, acc.getBalance());
					psql.executeUpdate();
					session.commit();
					return "Created";
				} catch (Exception e) {
					return e.getMessage();
				} 
			} else {
				return "Failed";
			}
			}
			finally {
				re.unlock();
			}
		}).get();

	}

	public String transfer(Transaction T) throws InterruptedException, ExecutionException {
		System.out.println("Transfer: From: " + T.getFrom_ac_no() + " To: " + T.getTo_ac_no());
	    return (String) executorService.submit(() -> {
	    	String result=null;
	    	
	    	re.lock();
	    	try {
	    	  result= trans.makeTransfer(T);
	    	}catch(Exception e)
	    	{
	    		System.out.println(e.getMessage());
	    	}
	    	finally{
	    		re.unlock();// synchronized (this) {
	    	}
//	            System.out.println("Entered: From: " + T.getFrom_ac_no() + " To: " + T.getTo_ac_no());
//	            String sql1 = "UPDATE account SET balance=? WHERE ac_no=?";
//	            String sql2 = "SELECT balance FROM account WHERE ac_no=?";
//	            SqlSession session = null;
//	            Connection con = null;
//	            PreparedStatement psql = null;
//
//	            try {
//	                session = sqlSession.openSession();
//	                con = session.getConnection();
//	                psql = con.prepareStatement(sql2);
//	                psql.setString(1, T.getFrom_ac_no());
//	                ResultSet r = psql.executeQuery();
//	                if (r.next()) { 
//	                    if (r.getDouble("balance") < T.getAmount())
//	                        return "Invalid Transaction";
//	                    else {
//	                        psql = con.prepareStatement(sql1);
//	                        psql.setDouble(1, r.getDouble("balance") - T.getAmount());
//	                        psql.setString(2, T.getFrom_ac_no());
//	                        psql.executeUpdate();
//
//	                        psql = con.prepareStatement(sql2);
//	                        psql.setString(1, T.getTo_ac_no());
//	                        r = psql.executeQuery();
//	                        if (r.next()) { 
//	                            psql = con.prepareStatement(sql1);
//	                            psql.setDouble(1, r.getDouble("balance") + T.getAmount());
//	                            psql.setString(2, T.getTo_ac_no());
//	                            psql.executeUpdate();
//	                            session.commit();
//	                        } else {
//	                            return "Failed";
//	                        }
//	                    }
//	                } else {
//	                    return "Failed";
//	                }
//	            } catch (Exception e) {
//	                return e.getMessage();
//	            } finally {
//	                if (psql != null)
//	                    try {
//	                        psql.close();
//	                    } catch (SQLException e) {
//	                        System.err.println("SQLException: " + e.getMessage());
//	                    }
//	                if (con != null)
//	                    try {
//	                        con.close();
//	                    } catch (SQLException e) {
//	                        System.err.println("SQLException: " + e.getMessage());
//	                    }
//	                if (session != null)
//	                    session.close();
//	            
//	                System.out.println("Exited: From: " + T.getFrom_ac_no() + " To: " + T.getTo_ac_no());
//	                re.unlock();
//	            }
//
//	            return "Transferred";
	      // }
			return result;
	    }).get();
	}
	public double getBalance(String acc) {
		
			return trans.getBalance(acc);

	}
	public List<Transaction> prevTransactions(String acc)
	{
		return trans.pastTransactions(acc);
	}
}