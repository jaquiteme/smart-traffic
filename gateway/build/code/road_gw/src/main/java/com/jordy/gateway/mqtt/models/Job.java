package com.jordy.gateway.mqtt.models;

public class Job {
    private int tag;
    private String jobContent;

    public Job() {
    }

    public Job(int tag, String jobContent) {
        this.tag = tag;
        this.jobContent = jobContent;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public String getJobContent() {
        return jobContent;
    }

    public void setJobContent(String jobContent) {
        this.jobContent = jobContent;
    }

    @Override
    public String toString() {
        return "Job [jobContent=" + jobContent + ", tag=" + tag + "]";
    }
    
}
