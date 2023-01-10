package org.technamin.assignment.model;

public class Information {
    private final int docId;
    private final UpdateType updateType;
    private final String fieldName;
    private String fieldUpdateValue;

    public Information(int docId, UpdateType updateType, String fieldName) {
        this.docId = docId;
        this.updateType = updateType;
        this.fieldName = fieldName;
    }

    public Information(int docId, UpdateType updateType, String fieldName, String fieldUpdateValue) {
        this.docId = docId;
        this.updateType = updateType;
        this.fieldName = fieldName;
        this.fieldUpdateValue = fieldUpdateValue;
    }

    @Override
    public String toString() {
        return "Log Information {" +
                "doc_id=" + docId +
                ", updateType=" + updateType +
                ", fieldName='" + fieldName + '\'' +
                '}';
    }
}

