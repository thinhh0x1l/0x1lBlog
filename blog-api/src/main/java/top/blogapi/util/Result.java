package top.blogapi.util;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Result<T> {
    Integer code;
    String msg;
    T data;

    public static <T> Result<T> ok(String msg, T data) {
        return new Result<>(200, msg, data);
    }

    public static <T> Result<T> ok(String msg){
        return new Result<>(200, msg , null);
    }

    public static <T> Result<T> error(String msg){
        return new Result<>(500, msg , null);
    }

    public static <T> Result<T> error(){
        return new Result<>(500, "Lỗi ngoại lệ" , null);
    }

    public static <T> Result<T> create(Integer code, String msg, T data) {
        return new Result<>(code, msg, data);
    }

    public static <T> Result<T> create(Integer code, String msg) {
        return new Result<>(code, msg, null);
    }


}
