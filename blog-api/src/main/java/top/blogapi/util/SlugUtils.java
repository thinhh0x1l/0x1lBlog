package top.blogapi.util;

public class SlugUtils {

    public static String convertSpaceToHyphen(String source){
        return source.trim().toLowerCase().replaceAll("\\s+", "-");
    }
    public static String convertHyphenToSpace(String source){
        return source.trim().replaceAll("-", " ");
    }

    private SlugUtils(){}
}
