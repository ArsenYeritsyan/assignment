package org.technamin.assignment.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Indexes(@Index(options = @IndexOptions(unique = true), fields = {@Field("doc_id")}))
@Entity(value = "items", noClassnameStored = true)
public class Item implements Serializable {
    @Id
    @JsonIgnore
    private ObjectId id;

    @Property("doc_id")
    @JsonProperty("doc_id")
    private int docId;
    private Date creationDate;
    private Date lastChange;

    //    @Version
    private Long seq;
    private String data;
    private String time;

    public Item(int id, Long seq, String data, String time) {
        this.docId = id;
        this.seq = seq;
        this.data = data;
        this.time = time;
    }

    public Item() {
    }

    @PrePersist
    public void prePersist() {
        this.creationDate = (creationDate == null) ? new Date() : creationDate;
        this.lastChange = (lastChange == null) ? creationDate : new Date();
    }

    @Override
    public int hashCode() {
        return Objects.hash(docId, seq);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "Item{" +
                "doc_id='" + docId + '\'' +
                ", seq=" + seq +
                ", data='" + data + '\'' +
                ", time='" + time + '\'' +
                '}';
    }


    public int getDocId() {
        return docId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setLastChange(Date lastChange) {
        this.lastChange = lastChange;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }
}


