package ru.itis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public class JlmqProducer {
    private ObjectMapper objectMapper;
    private WebSocketSession webSocketSession;
    private String queueName;

    private JlmqProducer(ProducerBuilder producerBuilder) {
        objectMapper = producerBuilder.objectMapper;
        webSocketSession = producerBuilder.jlmqConnector.getSession();
        this.queueName = producerBuilder.queueName;
    }

    public void send(MessageDto message) throws IOException {
        message.getHeaders().put("queue_name", queueName);
        message.getHeaders().put("command", "send");
        String requestMes = objectMapper.writeValueAsString(message);
        TextMessage textMessage = new TextMessage(requestMes);
        webSocketSession.sendMessage(textMessage);
    }

    public static ProducerBuilder builder(JlmqConnector jlmqConnector) {
        return new ProducerBuilder(jlmqConnector);
    }

    public static class ProducerBuilder {
        private String queueName;
        private ObjectMapper objectMapper;
        private JlmqConnector jlmqConnector;

        public ProducerBuilder(JlmqConnector jlmqConnector) {
            this.jlmqConnector = jlmqConnector;
            objectMapper = new ObjectMapper();
        }

        @SneakyThrows
        public ProducerBuilder toQueue(String name) {
            queueName = name;
            return this;
        }

        public JlmqProducer create() {
            return new JlmqProducer(this);
        }
    }
}
