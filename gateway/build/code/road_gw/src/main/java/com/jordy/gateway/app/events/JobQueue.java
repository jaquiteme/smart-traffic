package com.jordy.gateway.app.events;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import com.jordy.gateway.mqtt.models.Job;

public class JobQueue {
    private BlockingQueue<Job> jobQueue;
    private Hashtable<String, JobEventListener> jobEvtListener;

    public JobQueue() {
        this.jobQueue = new LinkedBlockingDeque<>();
        this.jobEvtListener = new Hashtable<>();
    }

    public void newTodo(Job job) {
        this.jobQueue.add(job);
        processJobEvent(new JobEvent(job));
    }

    public Job getJob() throws InterruptedException {
        return this.jobQueue.take();
    }

    public Job peekAJob() {
        return this.jobQueue.peek();
    }

    public boolean isQueueEmpty() {
        return this.jobQueue.isEmpty();
    }

    public void clearJobQueue() {
        this.jobQueue.clear();
    }

    public int sizeOfQueue() {
        return this.jobQueue.size();
    }

    public synchronized void addJobEventListener(String registerId, JobEventListener jobEventListener) {
        if (!this.jobEvtListener.containsKey(registerId)) {
            this.jobEvtListener.put(registerId, jobEventListener);
        }
    }

    public synchronized void removeJobEventListener(String registerId) {
        if (this.jobEvtListener.containsKey(registerId)) {
            //System.out.println("REMOVED LISTENER " + registerId);
            this.jobEvtListener.remove(registerId);
        }
    }

    private void processJobEvent(JobEvent event) {
        Hashtable<String, JobEventListener> tmpjobEvtListener;
        synchronized (this) {
            System.out.println("LISTNERS SIZE " + jobEvtListener.size());
            if (jobEvtListener.size() == 0)
                return;
            tmpjobEvtListener = (Hashtable<String, JobEventListener>) jobEvtListener.clone();
        }
        Set<String> keys = tmpjobEvtListener.keySet();

        for (String key : keys) {
            tmpjobEvtListener.get(key).jobArrived(event);
        }
    }
}
