package com.example.task.Scheduler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.task.Entity.Transaction;
import com.example.task.Services.AccountsService;
import com.example.task.configure.DataSourceConfig;

@Component
public class FailureScheduler {
    @Autowired
    AccountsService service;
    @Autowired
    private DataSourceConfig data = new DataSourceConfig();
    @Autowired
    private SqlSessionFactory sqlSession = data.sqlSessionFactory();
    private final ReentrantLock re = new ReentrantLock();
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    @Scheduled(fixedDelay = 10000)
    public void scheduledFailureTransfer() {
        executorService.submit(() -> {
            List<Transaction> transactions = new ArrayList<>();
            String sql = "SELECT * FROM transactions WHERE status=? OR status=? ORDER BY Id LIMIT 10 FOR UPDATE;";
            try (SqlSession session = sqlSession.openSession();
                 Connection con = session.getConnection();
                 PreparedStatement psql = con.prepareStatement(sql)) {
            	psql.setString(1, "Failed| due to runtime or compilation error");
            	psql.setString(2, "Failed| Insufficient Funds");
                ResultSet r = psql.executeQuery();
                while (r.next()) {
                    Transaction trans = new Transaction();
                    trans.setFrom_ac_no(r.getString("from_ac_no"));
                    trans.setTo_ac_no(r.getString("to_ac_no"));
                    trans.setAmount(r.getDouble("amount"));
                    trans.setStatus(r.getString("status"));
                    trans.setId(r.getLong("id"));
                    transactions.add(trans);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            transactions.parallelStream().forEach(trans -> {
                if (trans.getFrom_ac_no().equals(trans.getTo_ac_no())) {
                    trans.setStatus("Failed| Cannot transfer cash to the same account");
                } else if (trans.getAmount() < 0) {
                    trans.setStatus("Failed| Amount should be positive");
                } else {
                    String result=null;
					try {
						result = service.transfer(trans);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
                    if ("Invalid Transaction".equals(result)) {
                        trans.setStatus("Failed| Insufficient Funds");
                    } else if ("Transferred".equals(result)) {
                        trans.setStatus("Passed");
                    } else {
                        System.out.println(result);
                        trans.setStatus("Failed");
                    }
                }

                String sql2 = "UPDATE transactions SET status=? WHERE id=?";
                try (SqlSession session = sqlSession.openSession();
                     Connection con = session.getConnection();
                     PreparedStatement psql2 = con.prepareStatement(sql2);) {
                    re.lock();
                    try {
                        psql2.setString(1, trans.getStatus());
                        psql2.setLong(2,trans.getId());
                        psql2.executeUpdate();
                    } finally {
                        re.unlock();
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            });
        });
    }

}
