package org.technamin.assignment.model;

public class Msg {

    private String queue;
    private Long deliveryTag;
    private Information msg;
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
        return msg;
    }

    public void setMsg(Information msg) {
        this.msg = msg;
    }

    public Integer getSendAmount() {
        return sendAmount;
    }

    public void setSendAmount(Integer sendAmount) {
        this.sendAmount = sendAmount;
    }
}
