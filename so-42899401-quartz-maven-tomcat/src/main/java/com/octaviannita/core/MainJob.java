package com.octaviannita.core;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import static java.lang.System.out;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Mar 20, 2017
 */
public class MainJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        // Simulate job execution for 5 seconds...
        try {
            out.println("Executing job in background...");
            Thread.sleep(1000 * 5 /* secs */);
            out.println("Done executing job.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
