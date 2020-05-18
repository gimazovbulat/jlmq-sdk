package ru.itis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

@Getter
public class JlmqConsumer {

    private JlmqConsumer(ConsumerBuilder consumerBuilder) {
    }

    public static ConsumerBuilder builder(JlmqConnector jlmqConnector) {
        return new ConsumerBuilder(jlmqConnector);
    }

    public static class ConsumerBuilder {
        private WebSocketSession session;
        private ObjectMapper objectMapper;
        private ConsumerWebSocketHandler webSocketHandler;

        public ConsumerBuilder(JlmqConnector jlmqConnector) {
            this.session = jlmqConnector.getSession();
            this.webSocketHandler = jlmqConnector.getWebSocketHandler();
            objectMapper = new ObjectMapper();
        }

        @SneakyThrows
        public ConsumerBuilder subscribe(String queueName) {
            MessageDto messageDtoSdk = new MessageDto();
            Map<String, String> headers = new HashMap<>();
            headers.put("command", "subscribe");
            headers.put("queue_name", queueName);
            messageDtoSdk.setHeaders(headers);

            String messageAsString = objectMapper.writeValueAsString(messageDtoSdk);
            session.sendMessage(new TextMessage(messageAsString));
            return this;
        }

        public ConsumerBuilder onReceive(ConsumerCallback cc) {
            webSocketHandler.setCallback(cc);
            return this;
        }

        public JlmqConsumer create() {
            return new JlmqConsumer(this);
        }
    }
}
