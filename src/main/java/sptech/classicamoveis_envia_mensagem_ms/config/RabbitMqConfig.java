package sptech.classicamoveis_envia_mensagem_ms.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${rabbitmq.queue.name}")
    private String queueName;

    @Bean
    public Queue filaMensagens() {
        return new Queue(queueName, true);
    }
}
