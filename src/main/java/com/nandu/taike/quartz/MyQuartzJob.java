package com.nandu.taike.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyQuartzJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(MyQuartzJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("MyQuartzJob execute...");
    }

}
