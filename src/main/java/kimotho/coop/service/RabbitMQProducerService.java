package kimotho.coop.service;

public interface RabbitMQProducerService {
    void sendMessage(String message);
}
