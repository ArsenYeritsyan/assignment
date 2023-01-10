package org.technamin.assignment.config;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQConsumer {
    private static ConnectionFactory factory = RabbitMQConfig.connectionFactory();
    private static Connection connection;
    private static Channel channel;
    private static DefaultConsumer consumer;

    public static void defaultConsumerInit() {
        {
            try {
                connection = factory.newConnection();
                channel = connection.createChannel();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }

        consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(
                    String consumerTag,
                    Envelope envelope,
                    AMQP.BasicProperties properties,
                    byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Consumed: " + message);
            }
        };
        try {
            channel.basicConsume(RabbitMQConfig.BASIC_DEMO_QUEUE, true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
