package org.technamin.assignment.model;

public class ItemUpdateDto {
    private int docId;
    private String fieldName;
    private String fieldUpdateValue;

    public ItemUpdateDto(int docId, String fieldName, String fieldUpdateValue) {
        this.docId = docId;
        this.fieldName = fieldName;
        this.fieldUpdateValue = fieldUpdateValue;
    }

    public int getDocId() {
        return docId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldUpdateValue() {
        return fieldUpdateValue;
    }
}
