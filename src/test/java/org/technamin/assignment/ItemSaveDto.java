package org.technamin.assignment;

public class ItemSaveDto {
    private final int id;
    private final Long seq;
    private final String data;
    private final String time;

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
