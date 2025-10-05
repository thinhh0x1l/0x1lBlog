package top.blogapi.bean;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Result {
    Integer code;
    String msg;
    Object data;

    public static Result ok(String msg, Object data) {
        return new Result(null, msg, data);
    }
    public static Result create(Integer code, String msg, Object data) {
        return new Result(code, msg, data);
    }
    public static Result create(Integer code, String msg) {
        return new Result(code, msg, null);
    }
}
