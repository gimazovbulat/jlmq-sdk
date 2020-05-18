package ru.itis;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface MessageI {
    Object getBody() throws JsonProcessingException;

    void accepted();

    void completed();
}
