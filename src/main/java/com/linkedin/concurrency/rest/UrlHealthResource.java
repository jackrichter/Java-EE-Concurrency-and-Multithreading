package com.linkedin.concurrency.rest;

import com.linkedin.concurrency.runnables.UrlHealthProcessor;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.concurrent.TimeUnit;

@Path("urlCheck")
public class UrlHealthResource {

//    @Resource(lookup = "java:comp/DefaultManagedScheduledExecutorService")
    @Resource(lookup = "java:jboss/ee/concurrency/scheduler/default")
    private ManagedScheduledExecutorService scheduledExecutorService;

    @GET
    public String health() {
        scheduledExecutorService.schedule(new UrlHealthProcessor(), 3, TimeUnit.SECONDS);
        return "Health check initiated";
    }
}