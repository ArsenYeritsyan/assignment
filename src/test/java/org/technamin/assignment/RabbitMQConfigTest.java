package org.technamin.assignment;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RabbitMQConfigTest {
    private static Channel channel;

    @Test
    public void consume() {
        String exchange = "MyExchange";
        String message = "Message TEST";
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(exchange, "fanout");
            do {
                channel.basicPublish(exchange, "", null, message.getBytes());
            } while (!message.equalsIgnoreCase("Quit"));

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
