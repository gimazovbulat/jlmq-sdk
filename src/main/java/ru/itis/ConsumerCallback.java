package ru.itis;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface ConsumerCallback <T extends MessageI> {
    void onReceive(T message) throws JsonProcessingException;
}
