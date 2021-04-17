package com.jordy.gateway.app.events;

import com.jordy.gateway.mqtt.models.Job;

public class JobEvent {
    private Job job;

    public JobEvent(Job job) {
        this.job = job;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }
}
