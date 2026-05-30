package top.blogapi.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Guest {
    Long id;
    String tokenHash; // String hash = DigestUtils.sha256Hex(token);
    LocalDateTime createAt;
    LocalDateTime lastSeenAt;

    public Guest(String tokenHash){
        this.tokenHash = tokenHash;
        this.createAt = LocalDateTime.now();
        this.lastSeenAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Guest{" +
                "\nid=" + id +
                "\n, tokenHash='" + tokenHash + '\'' +
                "\n, createAt=" + createAt +
                "\n, lastSeenAt=" + lastSeenAt +
                "\n}";
    }
}
