package top.blogapi.util.markdown.ext.cover.internal;

import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.delimiter.DelimiterProcessor;
import org.commonmark.parser.delimiter.DelimiterRun;
import top.blogapi.util.markdown.ext.cover.Cover;
import top.blogapi.util.markdown.ext.heimu.Heimu;

/** Bộ xử lý dấu phân cách khớp cặp %% và xây dựng node Cover trong AST. */
public class CoverDelimiterProcessor implements DelimiterProcessor {
    @Override
    public char getOpeningCharacter() {
        return '%';
    }

    @Override
    public char getClosingCharacter() {
        return '%';
    }

    @Override
    public int getMinLength() {
        return 2;
    }

    @Override
    public int process(DelimiterRun opener, DelimiterRun closer) {
        if (opener.length() >= 2 && closer.length() >= 2) {
            Text openerText = opener.getOpener();
            Text closerText = closer.getCloser();

            Node cover = new Cover();
            Node tmp = openerText.getNext();
            while (tmp != null && tmp != closerText) {
                Node next = tmp.getNext();
                cover.appendChild(tmp);
                tmp = next;
            }

            openerText.insertAfter(cover);
            openerText.unlink();
            closerText.unlink();
            return 2;
        }

        return 0;  
    }
}
