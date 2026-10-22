package sptech.classicamoveis_envia_mensagem_ms.adapter.out.telegram;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sptech.classicamoveis_envia_mensagem_ms.domain.port.out.MensagemSender;

@Component
public class TelegramAdapter implements MensagemSender {

    private final HttpClient httpClient;
    private final String botToken;
    private final String chatId;

    public TelegramAdapter(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.chat.id}") String chatId,
            @Value("${telegram.ssl.skip-verify:false}") boolean skipSslVerification) {
        this.botToken = botToken;
        this.chatId = chatId;
        this.httpClient = criarHttpClient(skipSslVerification);
    }

    @Override
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
        } catch (SSLHandshakeException e) {
            throw new IllegalStateException(
                    "Falha no handshake TLS com a API do Telegram. Verifique o certificado do ambiente ou configure telegram.ssl.skip-verify=true para desenvolvimento.",
                    e);
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível conectar à API do Telegram.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("O envio para o Telegram foi interrompido.", e);
        }
    }

    private HttpClient criarHttpClient(boolean skipSslVerification) {
        if (!skipSslVerification) {
            return HttpClient.newBuilder().build();
        }

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustAllManager()}, new SecureRandom());
            return HttpClient.newBuilder().sslContext(sslContext).build();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new IllegalStateException("Não foi possível configurar o cliente TLS do Telegram.", e);
        }
    }

    private X509TrustManager trustAllManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
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
