package com.junyu.service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Consumer {
	public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("junyu");
        factory.setPassword("huang");
        factory.setHost("192.168.226.202");
        //create new connection
        Connection conn = factory.newConnection();
        //obtain channel
        final Channel channel = conn.createChannel();
        //claim exchange
        String exchangeName = "hello-exchange";
        channel.exchangeDeclare(exchangeName, "direct", true);
        //claim queue
        String queueName = channel.queueDeclare().getQueue();
        String routingKey = "hola";
        //band queue through key "hola", to combine queue and exchange
        channel.queueBind(queueName, exchangeName, routingKey);

        while(true) {
            //consumer message
            boolean autoAck = false;
            String consumerTag = "";
            channel.basicConsume(queueName, autoAck, consumerTag, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag,
                                           Envelope envelope,
                                           AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {
                    String routingKey = envelope.getRoutingKey();
                    String contentType = properties.getContentType();
                    System.out.println("consumer routing key: " + routingKey);
                    System.out.println("consumer content type: " + contentType);
                    long deliveryTag = envelope.getDeliveryTag();
                    //confirm message
                    channel.basicAck(deliveryTag, false);
                    System.out.println("consumer message content: ");
                    String bodyStr = new String(body, "UTF-8");
                    System.out.println(bodyStr);

                }
            });
        }
    }

}
