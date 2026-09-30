package sptech.classicamoveis_envia_mensagem_ms.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import sptech.classicamoveis_envia_mensagem_ms.service.TelegramService;

@Component
public class TelegramScheduler {

    private static final Logger logger = LoggerFactory.getLogger(TelegramScheduler.class);

    private final TelegramService telegramService;
    private final String chatId;

    public TelegramScheduler(
            TelegramService telegramService,
            @Value("${telegram.chat.id}") String chatId) {
        this.telegramService = telegramService;
        this.chatId = chatId;
    }

    @Scheduled(fixedDelayString = "${telegram.scheduler.delay:180000}")
    public void enviarMensagemAutomatica() {
        String mensagem = "Mensagem automática do microserviço de estoque mínimo: tudo ok no sistema.";
        logger.info("Enviando mensagem automática para o Telegram. Chat ID: {}", chatId);
        telegramService.enviarMensagem(mensagem);
    }
}
