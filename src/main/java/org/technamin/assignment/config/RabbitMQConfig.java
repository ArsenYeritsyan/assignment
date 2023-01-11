package org.technamin.assignment.config;

import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQConfig {
    public static final Integer TIMEOUT = Integer.valueOf(System.getProperty("TIMEOUT","300000"));
    public static final String DOCKER_HOST = System.getProperty("DOCKER_HOST","rabbitmq");
    public static final Integer PORT = Integer.valueOf(System.getProperty("PORT","5672"));
    public static final String USERNAME = System.getProperty("USERNAME","guest");
    public static final String PASSWORD = System.getProperty("PASSWORD","guest");
    public static final String VHOST = System.getProperty("VHOST","/");

    public static final String BASIC_DEMO_EX = System.getProperty("VHOST","assignment-exchange");
    public static final String BASIC_DEMO_QUEUE = System.getProperty("VHOST","information-update");
    public static final String BASIC_DEMO_QUEUE_2 = System.getProperty("VHOST","basic-queue-2");
    public static final String BASIC_DEMO_AE = System.getProperty("VHOST","basic-alternate-exchange");

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
