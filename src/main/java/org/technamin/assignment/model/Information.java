package org.technamin.assignment.model;

import org.bson.types.ObjectId;

import java.math.BigInteger;

public class Information {
    private int doc_id;
    private UpdateType updateType;
    private String fieldName;
    private String fieldUpdateValue;

    public Information(int doc_id, UpdateType updateType, String fieldName) {
        this.doc_id = doc_id;
        this.updateType = updateType;
        this.fieldName = fieldName;
    }

    public Information(int doc_id, UpdateType updateType, String fieldName, String fieldUpdateValue) {
        this.doc_id = doc_id;
        this.updateType = updateType;
        this.fieldName = fieldName;
        this.fieldUpdateValue = fieldUpdateValue;
    }
}

