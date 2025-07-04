package com.jeffjackson.blockSchedule.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection ="blockSchedule")
public class BlockSchedule {
    @Id
    private String id;
    private String date;
    private String time;
    private String reason;

    public BlockSchedule() {
    }

    public BlockSchedule(String id, String date, String time, String reason) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.reason = reason;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
