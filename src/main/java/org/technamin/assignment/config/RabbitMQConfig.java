package org.technamin.assignment.config;

import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQConfig {

    private RabbitMQConfig() {
    }

    public static ConnectionFactory connectionFactory() {
        final var TIMEOUT = Integer.parseInt(System.getProperty("TIMEOUT", "300000"));
        final var RABBIT_HOST = System.getProperty("RABBIT_HOST", "localhost");
        final var PORT = Integer.parseInt(System.getProperty("PORT", "5672"));
        final var USERNAME = System.getProperty("USERNAME", "guest");
        final var PASSWORD = System.getProperty("PASSWORD", "guest");
        final var VHOST = System.getProperty("VHOST", "/");

        ConnectionFactory factory = new ConnectionFactory();
        factory.setPort(PORT);
        factory.setHost(RABBIT_HOST);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        factory.setVirtualHost(VHOST);
        factory.setConnectionTimeout(TIMEOUT);
        factory.setAutomaticRecoveryEnabled(true);
        factory.setTopologyRecoveryEnabled(true);
        return factory;
    }
}
