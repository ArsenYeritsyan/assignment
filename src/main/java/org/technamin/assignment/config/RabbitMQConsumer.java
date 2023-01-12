package org.technamin.assignment.config;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.technamin.assignment.exceptions.RabbitMQException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RabbitMQConsumer {
    private static final Logger logger = Logger.getLogger(RabbitMQConsumer.class.toString());

    private RabbitMQConsumer() {
    }

    public static void defaultConsumerInit() {
        try {
            final var connection = RabbitMQConfig.connectionFactory().newConnection();
            final var channel = connection.createChannel();

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

            channel.basicConsume(RabbitMQPublisher.BASIC_DEMO_QUEUE, true, consumer);
            connection.close();
        } catch (IOException | TimeoutException e) {
            throw new RabbitMQException(e);
        }
    }
}
