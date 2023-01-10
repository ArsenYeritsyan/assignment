package org.technamin.assignment.model;

import org.bson.types.ObjectId;

import java.math.BigInteger;

public class ItemUpdateDto {
    private int doc_id;
    private String fieldName;
    private String fieldUpdateValue;

    public ItemUpdateDto(int doc_id, String fieldName, String fieldUpdateValue) {
        this.doc_id = doc_id;
        this.fieldName = fieldName;
        this.fieldUpdateValue = fieldUpdateValue;
    }

    public int getDoc_id() {
        return doc_id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldUpdateValue() {
        return fieldUpdateValue;
    }
}
