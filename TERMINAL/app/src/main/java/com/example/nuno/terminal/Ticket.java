package com.example.nuno.terminal;

import java.io.Serializable;

public class Ticket implements Serializable {
    String id, performanceName, performanceDate, performanceId, customerId, seat;
    boolean isUsed;

    public Ticket(String id, String performanceName, String performanceDate, String performanceId, String customerId, String seat, boolean isUsed) {
        this.id = id;
        this.performanceName = performanceName;
        this.performanceDate = performanceDate;
        this.performanceId = performanceId;
        this.customerId = customerId;
        this.seat = seat;
        this.isUsed = isUsed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPerformanceName() {
        return performanceName;
    }

    public void setPerformanceName(String performanceName) {
        this.performanceName = performanceName;
    }

    public String getPerformanceDate() {
        return performanceDate;
    }

    public void setPerformanceDate(String performanceDate) {
        this.performanceDate = performanceDate;
    }

    public String getPerformanceId() {
        return performanceId;
    }

    public void setPerformanceId(String performanceId) {
        this.performanceId = performanceId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }
}