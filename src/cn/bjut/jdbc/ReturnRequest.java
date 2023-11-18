package cn.bjut.jdbc;

import java.time.LocalDateTime;

public class ReturnRequest {
    private int r_id;
    private int o_id;
    private String  request_time;
    private String reason;
    private String status;

    // Constructors
    public ReturnRequest(int r_id, int o_id, String request_time, String reason, String status) {
        this.r_id = r_id;
        this.o_id = o_id;
        this.request_time = request_time;
        this.reason = reason;
        this.status = status;
    }
    public ReturnRequest() {
        super();
    }

    // Getters and Setters
    public int getR_id() {
        return r_id;
    }

    public void setR_id(int r_id) {
        this.r_id = r_id;
    }

    public int getO_id() {
        return o_id;
    }

    public void setO_id(int o_id) {
        this.o_id = o_id;
    }

    public String getRequest_time() {
        return request_time;
    }

    public void setRequest_time(String request_time) {
        this.request_time = request_time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // toString method for better logging and debugging
    @Override
    public String toString() {
        return "ReturnRequest{" +
                "r_id=" + r_id +
                ", o_id=" + o_id +
                ", request_time=" + request_time +
                ", reason='" + reason + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
