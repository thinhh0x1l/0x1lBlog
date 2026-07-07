package top.blogapi.util.markdown.ext.heimu;

import org.commonmark.node.CustomNode;
import org.commonmark.node.Delimited;


/** Node tùy chỉnh CommonMark đại diện cho cú pháp @@heimu@@ (spoiler) trong cây AST markdown. */
public class Heimu extends CustomNode implements Delimited {

    private final static String DELIMITER = "@@";

    @Override
    public String getOpeningDelimiter() {
        return DELIMITER;
    }

    @Override
    public String getClosingDelimiter() {
        return DELIMITER;
    }
}
