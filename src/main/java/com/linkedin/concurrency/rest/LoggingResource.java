package com.linkedin.concurrency.rest;

import com.linkedin.concurrency.runnables.LoggingProcessor;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Path("logging")
public class LoggingResource {

    @Resource(lookup = "java:jboss/ee/concurrency/factory/default")
    private ManagedThreadFactory threadFactory;

    @GET
    public String logData() {

        Thread thread = threadFactory.newThread(new LoggingProcessor());
        thread.setName("Logging-Thread");
        thread.start();

        ExecutorService service = getApplicationPool();

        for (int i = 0; i < 7; i++) {
            service.submit(new LoggingProcessor());
        }

        return "Logging thread doing its job, check console!";
    }

    public ExecutorService getApplicationPool() {
        ExecutorService service = new ThreadPoolExecutor(3, 10, 5,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(2), this.threadFactory);
        return service;
    }
}
