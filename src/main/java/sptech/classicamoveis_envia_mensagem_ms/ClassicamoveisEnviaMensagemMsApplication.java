package sptech.classicamoveis_envia_mensagem_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ClassicamoveisEnviaMensagemMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClassicamoveisEnviaMensagemMsApplication.class, args);
	}

}
