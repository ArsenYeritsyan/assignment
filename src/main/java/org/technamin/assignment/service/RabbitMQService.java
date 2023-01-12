package org.technamin.assignment.service;

import com.rabbitmq.client.Connection;
import org.technamin.assignment.config.RabbitMQConfig;
import org.technamin.assignment.config.RabbitMQPublisher;
import org.technamin.assignment.exceptions.RabbitMQException;
import org.technamin.assignment.model.Information;
import org.technamin.assignment.model.Msg;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RabbitMQService {
    private static final Logger logger = Logger.getLogger(RabbitMQService.class.toString());
    private static final RabbitMQPublisher publisher;

    static {
        try {
            Connection connection = RabbitMQConfig.connectionFactory().newConnection();
            publisher = new RabbitMQPublisher(connection);
        } catch (IOException | TimeoutException e) {
            throw new RabbitMQException(e);
        }
    }

    public static void sendLog(Information info) {
        try {
            Msg message = new Msg();
            message.setSendAmount(0);
            message.setMsg(info);
            message.setQueue(RabbitMQConfig.BASIC_DEMO_QUEUE);
            publisher.sendMsg(message);
            logger.log(Level.INFO, info.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


