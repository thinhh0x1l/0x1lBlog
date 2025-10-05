package top.blogapi.config;

import org.springframework.security.crypto.password.PasswordEncoder;
import top.blogapi.util.MD5Utils;

public class MyPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return MD5Utils.getMD5(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return rawPassword.equals(MD5Utils.getMD5(encodedPassword));
    }
}
