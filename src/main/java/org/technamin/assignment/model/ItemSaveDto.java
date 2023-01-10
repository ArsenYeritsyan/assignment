package org.technamin.assignment.model;

import java.math.BigInteger;

public class ItemSaveDto {
    private int id;
    private Long seq;
    private String data;
    private String time;

    public ItemSaveDto(int id, Long seq, String data, String time) {
        this.id = id;
        this.seq = seq;
        this.data = data;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public Long getSeq() {
        return seq;
    }

    public String getData() {
        return data;
    }

    public String getTime() {
        return time;
    }
}
