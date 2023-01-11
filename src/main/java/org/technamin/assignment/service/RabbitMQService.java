package org.technamin.assignment.service;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.technamin.assignment.config.RabbitMQConfig;
import org.technamin.assignment.config.RabbitMQPublisher;
import org.technamin.assignment.model.Information;
import org.technamin.assignment.model.Msg;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RabbitMQService {
    private static final Logger logger = Logger.getLogger(RabbitMQService.class.toString());

    public static void sendLog(Information info) {
        ConnectionFactory factory = RabbitMQConfig.connectionFactory();
        try {
            Connection connection = factory.newConnection();
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


