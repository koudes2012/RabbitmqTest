package com.junyu.service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {
	public static void main(String[] args) throws IOException, TimeoutException {
        //create connect factory
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("junyu");
        factory.setPassword("huang");
        //set RabbitMQ IP address
        factory.setHost("192.168.226.202");
        //create new connection
        Connection conn = factory.newConnection();
        //obtain channel
        Channel channel = conn.createChannel();
        //claim exchange
        String exchangeName = "hello-exchange";
        channel.exchangeDeclare(exchangeName, "direct", true);

        String routingKey = "hola";
        //message distribution
        byte[] messageBodyBytes = "quit".getBytes();
        channel.basicPublish(exchangeName, routingKey, null, messageBodyBytes);

        channel.close();
        conn.close();
    }
}
