package sptech.classicamoveis_envia_mensagem_ms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sptech.classicamoveis_envia_mensagem_ms.application.EnviarMensagemService;
import sptech.classicamoveis_envia_mensagem_ms.domain.port.in.EnviarMensagemUseCase;
import sptech.classicamoveis_envia_mensagem_ms.domain.port.out.MensagemSender;

@Configuration
public class ApplicationConfig {

    @Bean
    public EnviarMensagemUseCase enviarMensagemUseCase(MensagemSender mensagemSender) {
        return new EnviarMensagemService(mensagemSender);
    }
}
