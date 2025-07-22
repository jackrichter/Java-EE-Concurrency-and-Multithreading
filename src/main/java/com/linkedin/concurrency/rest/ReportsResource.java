package com.linkedin.concurrency.rest;

import com.linkedin.concurrency.beans.BankAccount;
import com.linkedin.concurrency.dao.BankAccountDao;
import com.linkedin.concurrency.runnables.ReportsProcessor;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.beans.PropertyVetoException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("reports")
public class ReportsResource {
    private Logger logger = Logger.getLogger(ReportsResource.class.getName());
    private BankAccountDao dao;

    @Resource(lookup = "java:comp/DefaultManagedExecutorService")
    private ManagedExecutorService executor;

    public ReportsResource() throws NamingException {
        // Set up connection pooling using c3p0 library
        try {
            ComboPooledDataSource dataSource = new ComboPooledDataSource();
            dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/test");
            dataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
            dataSource.setUser("root");
            dataSource.setPassword("mysql@123");
            dao = new BankAccountDao(dataSource);
        } catch (PropertyVetoException e) {
            logger.log(Level.SEVERE, null, e);
        }
    }

    @GET
    public String generateReports() {
        List<BankAccount> accounts = dao.getAllBankAccounts();
        for (BankAccount account : accounts) {
            Future<Boolean> future = executor.submit(new ReportsProcessor(account, dao));
            try {
                System.out.println("report generated? " + future.get());
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, null, e);
            } catch (ExecutionException e) {
                logger.log(Level.SEVERE, null, e);
            }
        }

        return "Report generation tasks submitted!";
    }
}
