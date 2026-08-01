package top.blogapi.util;

import org.hashids.Hashids;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Tiện ích làm xáo trộn ID dùng Hashid, tạo định danh công khai ngắn gọn và duy nhất. */
public class Encryptor {

    public final static Hashids hashids = new Hashids("my-salt", 6);
}
