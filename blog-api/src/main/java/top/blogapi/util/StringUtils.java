package top.blogapi.util;

public class StringUtils {
    public static boolean isEmpty(String ...str) {
        for(String s : str) {
            if(!org.springframework.util.StringUtils.hasText(s))
                return true;
        }
        return false;
    }


}
