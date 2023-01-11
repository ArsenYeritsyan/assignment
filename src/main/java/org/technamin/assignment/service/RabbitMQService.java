package org.technamin.assignment.service;

import com.rabbitmq.client.Connection;
import org.technamin.assignment.config.RabbitMQConfig;
import org.technamin.assignment.config.RabbitMQPublisher;
import org.technamin.assignment.model.Information;
import org.technamin.assignment.model.Msg;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RabbitMQService {
    private static final Logger logger = Logger.getLogger(RabbitMQService.class.toString());
    private static Connection connection = null;

    static {
        try {
            connection = RabbitMQConfig.connectionFactory().newConnection();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public static void sendLog(Information info) {
        try {
            RabbitMQPublisher publisher = new RabbitMQPublisher(connection);
            String queue = RabbitMQConfig.BASIC_DEMO_QUEUE;

            Msg message = new Msg();
            message.setSendAmount(0);
            message.setMsg(info);
            message.setQueue(queue);
            publisher.sendMsg(message);
            logger.log(Level.INFO, info.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


