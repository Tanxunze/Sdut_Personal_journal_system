package main.java.Journal_Management_System.util;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Response implements Serializable {
    private int statusCode;
    private String message;
    private Map<String, Object> data;

    public Response() {
        data = new HashMap<>();
    }

    // Getters and Setters
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
