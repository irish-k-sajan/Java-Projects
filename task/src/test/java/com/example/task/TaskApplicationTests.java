package com.example.task;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.client.RestTemplate;

import com.example.task.DAO.Transfer;
import com.example.task.Entity.Transaction;
import com.example.task.configure.h2DataSourceConfig;
@EntityScan(basePackages = "com.example.task.entity")
@SpringBootTest
@SpringJUnitConfig(classes = {h2DataSourceConfig.class})
@ActiveProfiles("test")
class TaskApplicationTests {

    @Mock
    private h2DataSourceConfig dataSourceConfig= new h2DataSourceConfig();
    @Mock
    private ReentrantLock re =new ReentrantLock();
   // @Mock
   // @Qualifier("h2SqlSessionFactory")
   // private SqlSessionFactory sqlSessionFactory;
   // @Mock
    //private RestTemplate restTemplate;

    @InjectMocks
    private Transfer accountsService=new Transfer(dataSourceConfig.h2sqlSessionFactory());

   /* @Before
    public void setUp() throws Exception {
    	MockitoAnnotations.openMocks(this);
        //when(dataSourceConfig.h2sqlSessionFactory()).thenReturn(sqlSessionFactory);
    	System.out.println(dataSourceConfig.h2sqlSessionFactory());
        //accountsService = new Transfer(dataSourceConfig.h2sqlSessionFactory());
    	//sqlSessionFactory=dataSourceConfig.h2sqlSessionFactory();
    	//System.out.println(sqlSessionFactory.openSession().getConnection().isValid(1000));
    	
    }
*/
   @Test
   @Sql(scripts = "test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
   @Sql(scripts = "cleanup-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testTransfer() throws InterruptedException, ExecutionException {
        List<Transaction> transactions = Arrays.asList(
            new Transaction() {{
                setFrom_ac_no("234523435");
                setTo_ac_no("123456789");
                setAmount(10);
            }},
            new Transaction() {{
                setFrom_ac_no("234523435");
                setTo_ac_no("123456789");
                setAmount(20);
            }},
            new Transaction() {{
                setFrom_ac_no("234523435");
                setTo_ac_no("123456789");
                setAmount(60);
            }},
            new Transaction() {{
                setFrom_ac_no("234523435");
                setTo_ac_no("456789123");
                setAmount(10);
            }},
            new Transaction() {{
                setFrom_ac_no("456789123");
                setTo_ac_no("234523435");
                setAmount(10);
            }},
            new Transaction() {{
                setFrom_ac_no("123456789");
                setTo_ac_no("456789123");
                setAmount(10);
            }}
        );
        double acc1=accountsService.getBalance("234523435");
        double acc2=accountsService.getBalance("123456789");
        double acc3=accountsService.getBalance("456789123");
        final ExecutorService executorService = Executors.newFixedThreadPool(4);
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch endLatch = new CountDownLatch(transactions.size());
        final List<AssertionError> assertionErrors = new CopyOnWriteArrayList<>();

        transactions.parallelStream().forEach(transaction -> {
        System.out.println("Transfer: From: " + transaction.getFrom_ac_no() + " To: " + transaction.getTo_ac_no());
        executorService.submit(() -> {
        	String result="";
        	
            try {
                startLatch.await();
                //re.lock();
               synchronized(this) {
                result = accountsService.makeTransfer(transaction);
                }
                try {
                    assertEquals("Transferred", result); 
                } catch (AssertionError e) {
                    assertionErrors.add(e); 
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } finally {
            	//re.unlock();
                endLatch.countDown(); 
                
            }
        });
        });

        startLatch.countDown(); 
        endLatch.await(); 
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        if (!assertionErrors.isEmpty()) {
            assertionErrors.forEach(Throwable::printStackTrace);
            fail("There were assertion errors during the test execution.");
        }
        double delta = 0.001;
        assertEquals(acc1-90, accountsService.getBalance("234523435"),delta);
        assertEquals(acc2+80, accountsService.getBalance("123456789"),delta);
        assertEquals(acc3+10, accountsService.getBalance("456789123"),delta);
        System.out.println(accountsService.getBalance("234523435"));
        System.out.println(accountsService.getBalance("123456789"));
        System.out.println(accountsService.getBalance("456789123"));
   }
}