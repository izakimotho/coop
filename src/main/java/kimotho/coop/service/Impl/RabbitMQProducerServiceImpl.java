package kimotho.coop.service.Impl;

import kimotho.coop.service.RabbitMQProducerService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducerServiceImpl implements RabbitMQProducerService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    public void sendMessage(String msg) {
//        rabbitTemplate.convertAndSend("customer_exchange", "", msg);
    }
}
