package sptech.classicamoveis_envia_mensagem_ms.adapter.in.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import sptech.classicamoveis_envia_mensagem_ms.domain.port.in.EnviarMensagemUseCase;

@Component
public class MensagemConsumer {

    private static final Logger logger = LoggerFactory.getLogger(MensagemConsumer.class);

    private final EnviarMensagemUseCase enviarMensagemUseCase;
    private final MensagemBodyFormatter mensagemBodyFormatter;

    public MensagemConsumer(EnviarMensagemUseCase enviarMensagemUseCase) {
        this.enviarMensagemUseCase = enviarMensagemUseCase;
        this.mensagemBodyFormatter = new MensagemBodyFormatter();
    }

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void receberMensagem(String mensagem) {
        logger.info("Mensagem recebida da fila: {}", mensagem);

        enviarMensagemUseCase.enviarMensagem(mensagemBodyFormatter.formatar(mensagem));
    }
}
