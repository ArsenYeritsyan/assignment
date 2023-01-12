package org.technamin.assignment;

public class ItemUpdateDto {
    private final int docId;
    private final String fieldName;
    private final String fieldUpdateValue;

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
