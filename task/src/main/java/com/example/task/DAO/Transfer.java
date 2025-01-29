package com.example.task.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Repository;

import com.example.task.Entity.Transaction;
@Repository
public class Transfer {
	private ReentrantLock re =new ReentrantLock();
	private SqlSessionFactory sqlSession;
	public Transfer(SqlSessionFactory sqlSession) {
		this.sqlSession=sqlSession;		
		//System.out.println(sqlSession);
		//System.out.println(this.sqlSession);
	
	}
	public String makeTransfer(Transaction T)throws InterruptedException, ExecutionException 
	{   //re.lock();
		// synchronized (this) {
		            System.out.println("Entered: From: " + T.getFrom_ac_no() + " To: " + T.getTo_ac_no());
		            String sql1 = "UPDATE account SET balance=? WHERE ac_no=?";
		            String sql2 = "SELECT balance FROM account WHERE ac_no=?";
		            SqlSession session = null;
		            Connection con = null;
		            PreparedStatement psql = null;

		            try {
		                session = sqlSession.openSession();
		                con = session.getConnection();
		                psql = con.prepareStatement(sql2);
		                psql.setString(1, T.getFrom_ac_no());
		                ResultSet r = psql.executeQuery();
		                if (r.next()) { 
		                    if (r.getDouble("balance") < T.getAmount())
		                        return "Invalid Transaction";
		                    else {
		                        psql = con.prepareStatement(sql1);
		                        psql.setDouble(1, r.getDouble("balance") - T.getAmount());
		                        psql.setString(2, T.getFrom_ac_no());
		                        psql.executeUpdate();

		                        psql = con.prepareStatement(sql2);
		                        psql.setString(1, T.getTo_ac_no());
		                        r = psql.executeQuery();
		                        if (r.next()) { 
		                            psql = con.prepareStatement(sql1);
		                            psql.setDouble(1, r.getDouble("balance") + T.getAmount());
		                            psql.setString(2, T.getTo_ac_no());
		                            psql.executeUpdate();
		                            session.commit();
		                        } else {
		                            return "Failed";
		                        }
		                    }
		                } else {
		                    return "Failed";
		                }
		            } catch (Exception e) {
		                return e.getMessage();
		            } finally {
		                if (psql != null)
		                    try {
		                        psql.close();
		                    } catch (SQLException e) {
		                        System.err.println("SQLException: " + e.getMessage());
		                    }
		                if (con != null)
		                    try {
		                        con.close();
		                    } catch (SQLException e) {
		                        System.err.println("SQLException: " + e.getMessage());
		                    }
		                if (session != null)
		                    session.close();
		            
		                System.out.println("Exited: From: " + T.getFrom_ac_no() + " To: " + T.getTo_ac_no());
		                //re.unlock();
		            }

		            return "Transferred";
		      // }
		}
	public double getBalance(String acc) {
		double re=0;
		String sql="SELECT balance from ACCOUNT where ac_no=?";
			try (SqlSession session = sqlSession.openSession();
					Connection con = session.getConnection();
					PreparedStatement psql = con.prepareStatement(sql);) {
				psql.setString(1, acc);
				ResultSet r=psql.executeQuery();
				r.next();
				re=r.getDouble("balance");
			}catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
			return re;

	}
	public List<Transaction> pastTransactions(String acc){
		String sql="SELECT * FROM transactions WHERE from_ac_no=? LIMIT 10";
		List<Transaction> T= new ArrayList<Transaction>();
		try (SqlSession session = sqlSession.openSession();
				Connection con = session.getConnection();
				PreparedStatement psql = con.prepareStatement(sql);) {
			psql.setString(1, acc);
			ResultSet r=psql.executeQuery();
			while(r.next())
			{
				Transaction t=new Transaction();
				t.setFrom_ac_no(r.getString("from_ac_no"));
				t.setTo_ac_no(r.getString("to_ac_no"));
				t.setAmount(r.getDouble("amount"));
				t.setStatus(r.getString("status"));
				t.setId(r.getLong("id"));
				T.add(t);
			}
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return T;
	}
}
