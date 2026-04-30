package top.blogapi.util;

public class StringUtils {
    public static boolean isEmpty(String ...str) {
        for(String s : str) {
            if(!org.springframework.util.StringUtils.hasText(s))
                return true;
        }
        return false;
    }

    public static boolean hasSpecialChar(String ...str){
        for(String s: str)
            if (s.contains("%") || s.contains("_") || s.contains("[") || s.contains("#") || s.contains("*"))
                return true;
		return false;
    }

    private StringUtils(){}
}
