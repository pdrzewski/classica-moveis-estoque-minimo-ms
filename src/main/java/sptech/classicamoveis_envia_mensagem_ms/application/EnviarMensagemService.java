package sptech.classicamoveis_envia_mensagem_ms.application;

import sptech.classicamoveis_envia_mensagem_ms.domain.port.in.EnviarMensagemUseCase;
import sptech.classicamoveis_envia_mensagem_ms.domain.port.out.MensagemSender;

/** Caso de uso da aplicação para envio de mensagens. */
public class EnviarMensagemService implements EnviarMensagemUseCase {

    private final MensagemSender mensagemSender;

    public EnviarMensagemService(MensagemSender mensagemSender) {
        this.mensagemSender = mensagemSender;
    }

    @Override
    public void enviarMensagem(String mensagem) {
        mensagemSender.enviarMensagem(mensagem);
    }
}
