package kimotho.coop.service.Impl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConsumerService {

    @RabbitListener(queues = "customer_queue")
    public void receiveMessage(String message) {
        System.out.println("Received message: " + message);
        // Process the message here
    }
}
