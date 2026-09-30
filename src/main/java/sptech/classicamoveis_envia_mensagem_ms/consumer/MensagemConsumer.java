package sptech.classicamoveis_envia_mensagem_ms.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sptech.classicamoveis_envia_mensagem_ms.service.TelegramService;

@Component
public class MensagemConsumer {

    private static final Logger logger = LoggerFactory.getLogger(MensagemConsumer.class);

    private final TelegramService telegramService;

    public MensagemConsumer(TelegramService telegramService) {
        this.telegramService = telegramService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void receberMensagem(String mensagem) {
        logger.info("Mensagem recebida da fila: {}", mensagem);

        telegramService.enviarMensagem(mensagem);
    }
}
