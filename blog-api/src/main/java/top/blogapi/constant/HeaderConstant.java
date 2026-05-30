package top.blogapi.constant;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

public class HeaderConstant {
    public final static String GUEST_HEADER = "guestToken";
    public final static String USER_AGENT = "user-agent";
    public final static String TOKEN_HEADER = "x-guest-token";
    private HeaderConstant(){

    }
}
