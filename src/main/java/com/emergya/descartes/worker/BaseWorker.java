package com.emergya.descartes.worker;

import com.emergya.descartes.job.JobValidator;


/**
 * @author affernandez
 * 
 */
public class BaseWorker implements Runnable {

    private JobValidator job;

    /**
     * @param job
     */
    public BaseWorker(JobValidator job) {
        setJob(job);
    }

    @Override
    public void run() {

    }

    /**
     * @return JobConverter
     */
    public JobValidator getJob() {
        return job;
    }

    /**
     * @param job
     */
    private void setJob(JobValidator job) {
        this.job = job;
    }
}
