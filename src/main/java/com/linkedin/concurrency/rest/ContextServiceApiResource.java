package com.linkedin.concurrency.rest;

import com.linkedin.concurrency.runnables.ContextServiceRunnable;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ContextService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("contextservice")
public class ContextServiceApiResource {

    @Resource(lookup = "java:jboss/ee/concurrency/context/default")
    private ContextService contextService;

    @GET
    public String accessSecurityInfo(){

        ContextServiceRunnable runnable = new ContextServiceRunnable();

        Runnable proxy = contextService.createContextualProxy(runnable, Runnable.class);
        Thread proxyThread = new Thread(proxy);
        proxyThread.start();

        return "Context capturing information";
    }

}
