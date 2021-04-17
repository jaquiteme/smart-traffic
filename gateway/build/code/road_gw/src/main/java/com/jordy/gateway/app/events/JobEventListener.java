package com.jordy.gateway.app.events;

public interface JobEventListener {
    public void jobArrived(JobEvent jobEvent);
}
