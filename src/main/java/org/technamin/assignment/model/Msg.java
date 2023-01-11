package org.technamin.assignment.model;

public class Msg {
    private String queue;
    private Long deliveryTag;
    private Information information;
    private Integer sendAmount = 0;

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public Long getDeliveryTag() {
        return deliveryTag;
    }

    public void setDeliveryTag(Long deliveryTag) {
        this.deliveryTag = deliveryTag;
    }

    public Information getMsg() {
        return information;
    }

    public void setMsg(Information msg) {
        this.information = msg;
    }

    public Integer getSendAmount() {
        return sendAmount;
    }

    public void setSendAmount(Integer sendAmount) {
        this.sendAmount = sendAmount;
    }
}
