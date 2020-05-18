package ru.itis;

import lombok.*;

import java.util.Map;

import static ru.itis.MessageDto.Status.ACCEPTED;
import static ru.itis.MessageDto.Status.COMPLETED;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto implements MessageI {
    private Map<String, String> headers;
    private Object body;
    private Status status;
    private String messageId;

    @Override
    public void accepted() {
        status = ACCEPTED;
    }

    @Override
    public void completed() {
        status = COMPLETED;
    }

    @Getter
    public enum Status {
        NEW(0, "NEW"), ACCEPTED(1, "ACCEPTED"), COMPLETED(2, "COMPLETED");

        private String title;
        private int num;

        Status(int num, String title) {
            this.num = num;
            this.title = title;
        }
    }
}
