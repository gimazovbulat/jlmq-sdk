package ru.itis;

import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Getter
public class JlmqConnector {
    private ConsumerWebSocketHandler webSocketHandler;
    private WebSocketSession session;

    private final static String URL_PATTERN = "ws://localhost:%d/queue";

    private JlmqConnector(Connector connector) {
        this.webSocketHandler = connector.webSocketHandler;
        this.session = connector.session;
    }

    public static Connector connector(ConsumerWebSocketHandler webSocketHandler) {
        return new Connector(webSocketHandler);
    }

    public JlmqConsumer.ConsumerBuilder consumer() {
        return JlmqConsumer.builder(this);
    }

    public JlmqProducer.ProducerBuilder producer() {
        return JlmqProducer.builder(this);
    }

    public static class Connector {
        private String formattedUrl;
        private WebSocketSession session;
        private ConsumerWebSocketHandler webSocketHandler;

        public Connector(ConsumerWebSocketHandler webSocketHandler) {
            this.webSocketHandler = webSocketHandler;
        }

        public Connector port(int port) {
            formattedUrl = String.format(URL_PATTERN, port);
            return this;
        }

        public JlmqConnector connect() throws ExecutionException, InterruptedException {
            WebSocketClient webSocketClient = new StandardWebSocketClient();
            this.session = webSocketClient.doHandshake(
                    (WebSocketHandler) webSocketHandler,
                    new WebSocketHttpHeaders(),
                    URI.create(formattedUrl)).get();

            return new JlmqConnector(this);
        }
    }
}
