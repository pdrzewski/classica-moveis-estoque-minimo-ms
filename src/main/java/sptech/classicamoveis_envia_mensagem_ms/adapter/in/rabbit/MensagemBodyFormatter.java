package sptech.classicamoveis_envia_mensagem_ms.adapter.in.rabbit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Iterator;
import java.util.Map;

/** Converte um corpo JSON recebido da fila em texto para o Telegram. */
public class MensagemBodyFormatter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String formatar(String corpo) {
        if (corpo == null || corpo.isBlank() || !corpo.trim().startsWith("{")) {
            return corpo;
        }

        try {
            JsonNode json = objectMapper.readTree(corpo);
            if (!json.isObject()) {
                return corpo;
            }

            if (json.has("nome")
                    && json.has("estoqueMinimo")
                    && json.has("estoqueAtual")
                    && json.has("tipoMovimentacao")
                    && json.has("dataHora")) {
                return "Produto " + valor(json.get("nome"))
                        + " abaixo do estoque mínimo de " + valor(json.get("estoqueMinimo"))
                        + " produto(s).\n"
                        + "Estoque atual: " + valor(json.get("estoqueAtual")) + "\n"
                        + "tipo da movimentação: " + valor(json.get("tipoMovimentacao")) + "\n"
                        + "Data/Hora: " + valor(json.get("dataHora"));
            }

            StringBuilder mensagem = new StringBuilder();
            Iterator<Map.Entry<String, JsonNode>> campos = json.fields();
            while (campos.hasNext()) {
                Map.Entry<String, JsonNode> campo = campos.next();
                if (mensagem.length() > 0) {
                    mensagem.append('\n');
                }
                mensagem.append(campo.getKey()).append(": ").append(valor(campo.getValue()));
            }
            return mensagem.toString();
        } catch (Exception e) {
            // Corpos que não sejam JSON continuam sendo tratados como texto normal.
            return corpo;
        }
    }

    private String valor(JsonNode valor) {
        return valor.isTextual() ? valor.textValue() : valor.toString();
    }
}
