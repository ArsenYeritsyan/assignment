package org.technamin.assignment.config;

import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQConfig {

    //    public static final String HOST = "localhost";  -use without Docker
    public static final String DOCKER_HOST = "rabbitmq";
    public static final String USERNAME = "guest";
    public static final String PASSWORD = "guest";
    public static final Integer TIMEOUT = 300000;
    public static final Integer PORT = 5672;
    public static final String VHOST = "/";

    public static final String BASIC_DEMO_EX = "assignment-exchange";
    public static final String BASIC_DEMO_QUEUE = "information-update";
    public static final String BASIC_DEMO_QUEUE_2 = "basic-queue-2";
    public static final String BASIC_DEMO_AE = "basic-alternate-exchange";

    public static ConnectionFactory connectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setPort(PORT);
        factory.setHost(DOCKER_HOST);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        factory.setVirtualHost(VHOST);
        factory.setConnectionTimeout(TIMEOUT);
        factory.setAutomaticRecoveryEnabled(true);
        factory.setTopologyRecoveryEnabled(true);
        return factory;
    }


}
