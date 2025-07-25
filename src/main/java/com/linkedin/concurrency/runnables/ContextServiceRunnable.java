package com.linkedin.concurrency.runnables;

import javax.security.auth.Subject;
import java.security.AccessController;

public class ContextServiceRunnable implements Runnable {

    @Override
    public void run() {
        System.out.println("Thread "+Thread.currentThread().getName());
        Subject subject = Subject.getSubject(AccessController.getContext());
        System.out.println("Security information from a normal thread: " + subject);
    }
}
