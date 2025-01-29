package com.example.task;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.concurrent.ExecutionException;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import com.example.task.Services.*;
import com.example.task.Controller.AccountController;
import com.example.task.Entity.Account;
import com.example.task.Entity.Transaction;
import com.example.task.configure.DataSourceConfig;

@SpringBootTest
class AccountsTests {

    @Mock
    private DataSourceConfig dataSourceConfig;

    @Mock
    private SqlSessionFactory sqlSessionFactory;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AccountsService accountsService;

    @Before
    public void setUp() throws Exception {
        when(dataSourceConfig.sqlSessionFactory()).thenReturn(sqlSessionFactory);
        accountsService = new AccountsService(restTemplate);
    }

    @Test
    public void testCreateAccount() throws Exception {
        Account account = new Account();
        account.setAC_no("12345");
        account.setAc_owner("John Doe");
        account.setBalance(1000.0);

        String result = accountsService.create_account(account);
        assertEquals("Created", result);
    }

    @Test
    public void testTransfer() throws InterruptedException, ExecutionException {
        Transaction transaction = new Transaction();
        transaction.setFrom_ac_no("12345");
        transaction.setTo_ac_no("67890");
        transaction.setAmount(500.0);

        String result = accountsService.transfer(transaction);
        assertEquals("Transferred", result);
    }
	
	@Test
	public void hi() {
		assertEquals(1,1);
	}
}