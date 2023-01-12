package org.technamin.assignment;

import com.rabbitmq.client.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RabbitMQConfigTest {
    private static Channel channel;

    @Test
    void shouldSendAndConsume() {
        String exchange = "Test Exchange";
        String message = "Message TEST";
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setConnectionTimeout(30000);
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(exchange, BuiltinExchangeType.FANOUT);
            channel.basicPublish(exchange, "", null, message.getBytes());

            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, exchange, "");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String getMessage = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("Received '" + getMessage + "'");
                assertNotNull(getMessage);
                assertEquals(getMessage, message);
            };
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
