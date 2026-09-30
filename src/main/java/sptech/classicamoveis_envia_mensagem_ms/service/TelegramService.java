package sptech.classicamoveis_envia_mensagem_ms.service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TelegramService {

    private final HttpClient httpClient;
    private final String botToken;
    private final String chatId;

    public TelegramService(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.chat.id}") String chatId) {
        this.httpClient = HttpClient.newHttpClient();
        this.botToken = botToken;
        this.chatId = chatId;
    }

    public void enviarMensagem(String mensagem) {
        validarConfiguracao();

        String body = "chat_id=" + encode(chatId)
                + "&text=" + encode(mensagem);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.telegram.org/bot" + botToken + "/sendMessage"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() >= 300
                    || !response.body().contains("\"ok\":true")) {
                throw new IllegalStateException(
                        "Telegram recusou a mensagem. HTTP " + response.statusCode()
                                + ": " + response.body());
            }
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível conectar à API do Telegram.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("O envio para o Telegram foi interrompido.", e);
        }
    }

    private void validarConfiguracao() {
        if (botToken == null || botToken.isBlank()) {
            throw new IllegalStateException("telegram.bot.token não foi configurado.");
        }

        if (chatId == null || chatId.isBlank()) {
            throw new IllegalStateException("telegram.chat.id não foi configurado.");
        }
    }

    private String encode(String valor) {
        return URLEncoder.encode(valor, StandardCharsets.UTF_8);
    }
}
