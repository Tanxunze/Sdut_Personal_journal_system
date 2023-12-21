package main.java.Journal_Management_System.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Request implements Serializable {
    private String requestType;
    private Map<String, Object> data;

    public Request() {
        data = new HashMap<>();
    }

    // Getters and Setters
    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    // Convenience method to add data
    public void addData(String key, Object value) {
        data.put(key, value);
    }
}

