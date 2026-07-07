package top.blogapi.util;

/** Tiện ích chuyển đổi giữa slug thân thiện với URL và văn bản thuần túy. */
public class SlugUtils {

    public static String convertSpaceToHyphen(String source){
        return source.trim().toLowerCase().replaceAll("\\s+", "-");
    }
    public static String convertHyphenToSpace(String source){
        return source.trim().replace("-", " ");
    }

    private SlugUtils(){}
}
