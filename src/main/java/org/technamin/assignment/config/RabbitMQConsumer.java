package org.technamin.assignment.config;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RabbitMQConsumer {
    private static final Logger logger = Logger.getLogger(RabbitMQConsumer.class.toString());
    private static final ConnectionFactory factory = RabbitMQConfig.connectionFactory();
    private static Channel channel;

    public static void defaultConsumerInit() {
        try {
            Connection connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }

        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(
                    String consumerTag,
                    Envelope envelope,
                    AMQP.BasicProperties properties,
                    byte[] body) {
                String message = new String(body, StandardCharsets.UTF_8);
                logger.log(Level.INFO, message);
            }
        };
        try {
            channel.basicConsume(RabbitMQConfig.BASIC_DEMO_QUEUE, true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
