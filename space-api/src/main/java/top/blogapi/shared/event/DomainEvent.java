package top.blogapi.shared.event;

import java.time.Instant;

public class DomainEvent {
    private final String type;
    private final Instant occurredAt;

    public DomainEvent(String type) {
        this.type = type;
        this.occurredAt = Instant.now();
    }

    public DomainEvent(String type, Instant occurredAt) {
        this.type = type;
        this.occurredAt = occurredAt;
    }

    public String getType() { return type; }
    public Instant getOccurredAt() { return occurredAt; }
}
