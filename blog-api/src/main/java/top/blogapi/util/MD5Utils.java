package top.blogapi.util;

import org.springframework.util.DigestUtils;

public class MD5Utils {
    private static final String SALT = "mySecret";

    public static String getMD5(CharSequence str) {
        String base = SALT + str;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }

    public static void main(String[] args) {
        System.out.println(getMD5("123456"));
    }
}
