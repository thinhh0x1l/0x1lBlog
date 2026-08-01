package top.blogapi.util.markdown.ext.heimu.internal;

import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.delimiter.DelimiterProcessor;
import org.commonmark.parser.delimiter.DelimiterRun;
import top.blogapi.util.markdown.ext.heimu.Heimu;

/**
 * Description: Giới hạn
 * */

public class HeimuDelimiterProcessor implements DelimiterProcessor {
    @Override
    public char getOpeningCharacter() {
        return '@';
    }

    @Override
    public char getClosingCharacter() {
        return '@';
    }

    @Override
    public int getMinLength() {
        return 2;
    }

    @Override
    public int process(DelimiterRun opener, DelimiterRun closer) {
        // Chỉ xử lý nếu có đủ 2 ký tự @@
        if (opener.length() >= 2 && closer.length() >= 2) {
            // Tạo HeimuNode và xử lý nội dung
            Text openerText = opener.getOpener();
            Text closerText = closer.getCloser();

            Node heimu = new Heimu();

            Node tmp = openerText.getNext();
            while (tmp != null && tmp != closerText) {
                Node next = tmp.getNext();
                heimu.appendChild(tmp);
                tmp = next;
            }

            openerText.insertAfter(heimu);
            openerText.unlink();  // Xóa "@@" mở
            closerText.unlink();
            return 2;  // Đã sử dụng 2 ký tự @@
        }

        return 0;  // Không xử lý
    }

}
