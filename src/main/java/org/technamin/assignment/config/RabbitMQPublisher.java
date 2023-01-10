package org.technamin.assignment.config;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.technamin.assignment.model.Msg;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RabbitMQPublisher {
    private static final Logger logger = Logger.getLogger(RabbitMQPublisher.class.toString());
    private static final String RE_SEND_MAX_TIME = "Re-Send touch max time, [%s] \n";
    private static final String SUCCESS = "Send [%s] -  Success \n";
    private static final String RE_SEND = "Re-Send [%s] \n";
    private final SortedMap<Long, Msg> nackMsg = new TreeMap<>();
    private final Channel channel;

    public RabbitMQPublisher(Connection connection) throws IOException {
        channel = connection.createChannel();
        enablePublishConfirm(channel);
        declareAe(channel);
        declareExAndQueue(channel);
    }

    public void sendMsg(Msg msg) throws IOException {
        if (msg.getQueue().equals("")) {
            msg.setQueue(RabbitMQConfig.BASIC_DEMO_QUEUE);
        }
        msg.setDeliveryTag(channel.getNextPublishSeqNo());
        msg.setSendAmount(msg.getSendAmount() + 1);
        nackMsg.put(msg.getDeliveryTag(), msg);
        channel.basicPublish(RabbitMQConfig.BASIC_DEMO_EX, msg.getQueue(),
                false, null, msg.getMsg().toString().getBytes());
    }

    private void enablePublishConfirm(Channel channel) throws IOException {
        channel.confirmSelect();
        channel.addConfirmListener(this::ackCallback, this::nackCallback);
    }

    private void ackCallback(long deliveryTag, boolean multiple) {
        Collection<Msg> msgs;
        if (multiple) {
            msgs = nackMsg.headMap(deliveryTag).values();
            nackMsg.headMap(deliveryTag).clear();
        } else {
            msgs = Collections.singleton(nackMsg.get(deliveryTag));
            nackMsg.remove(deliveryTag);
        }
        for (Msg msg : msgs) {
            logger.log(Level.INFO, (String.format(SUCCESS, msg.getQueue())), msg.getMsg().toString());
        }
    }

    private void nackCallback(long deliveryTag, boolean multiple) throws IOException {
        Collection<Msg> msgs;
        if (multiple) {
            msgs = nackMsg.headMap(deliveryTag).values();
            nackMsg.headMap(deliveryTag).clear();
        } else {
            msgs = Collections.singleton(nackMsg.get(deliveryTag));
            nackMsg.remove(deliveryTag);
        }
        for (Msg msg : msgs) {
            if (msg.getDeliveryTag() > 3) {
                logger.log(Level.INFO, String.format(RE_SEND_MAX_TIME, msg.getDeliveryTag()), msg.getMsg());
                continue;
            }
            logger.log(Level.INFO, String.format(RE_SEND, msg.getDeliveryTag()), msg.getMsg());
            sendMsg(msg);
        }
    }

    private void declareExAndQueue(Channel channel) throws IOException {
        Map<String, Object> args = new HashMap<>();
        args.put("alternate-exchange", RabbitMQConfig.BASIC_DEMO_AE);
        channel.exchangeDeclare(RabbitMQConfig.BASIC_DEMO_EX,
                BuiltinExchangeType.DIRECT, true, false, args);

        channel.queueDeclare(
                RabbitMQConfig.BASIC_DEMO_QUEUE,
                true, false, false, null);
        channel.queueBind(RabbitMQConfig.BASIC_DEMO_QUEUE, RabbitMQConfig.BASIC_DEMO_EX,
                RabbitMQConfig.BASIC_DEMO_QUEUE);

        channel.queueDeclare(
                RabbitMQConfig.BASIC_DEMO_QUEUE_2,
                true, false, false, null);
        channel.queueBind(RabbitMQConfig.BASIC_DEMO_QUEUE_2, RabbitMQConfig.BASIC_DEMO_EX,
                RabbitMQConfig.BASIC_DEMO_QUEUE_2);
    }

    private void declareAe(Channel channel) throws IOException {
        channel.exchangeDeclare(RabbitMQConfig.BASIC_DEMO_AE,
                BuiltinExchangeType.FANOUT, true, false, null);

        channel.queueDeclare(RabbitMQConfig.BASIC_DEMO_AE,
                true, false, false, null);

        channel.queueBind(RabbitMQConfig.BASIC_DEMO_AE, RabbitMQConfig.BASIC_DEMO_AE, "");
    }
}
