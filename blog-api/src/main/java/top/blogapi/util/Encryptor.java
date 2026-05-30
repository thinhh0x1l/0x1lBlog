package top.blogapi.util;

import org.hashids.Hashids;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class Encryptor {

    public final static Hashids hashids = new Hashids("my-salt", 6);
}
