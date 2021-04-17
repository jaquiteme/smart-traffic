package com.jordy.backup.xmpp.models;

public class Payload {
    private String uri;
    private String data;
    
    public Payload(String uri, String data) {
        this.uri = uri;
        this.data = data;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    

}
