package top.blogapi.util.markdown;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.task.list.items.TaskListItemsExtension;
import org.commonmark.node.Image;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.AttributeProviderContext;
import org.commonmark.renderer.html.AttributeProviderFactory;
import org.commonmark.renderer.html.HtmlRenderer;
import top.blogapi.util.markdown.ext.cover.CoverExtension;
import top.blogapi.util.markdown.ext.heimu.HeimuExtension;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MarkdownUtils {
    /**
     * Chuyển đổi định dạng markdown sang HTML
     */
    public static String markdownToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    /**
     * Thêm extension
     */
    public static String markdownToHtmlExtensions(String markdown) {
        // Tiêu đề h tạo id
        // Set<Extension> headingAnchorExtensions = Set.of(HeadingAnchorExtension.create());

        // Chuyển đổi bảng sang HTML
        List<Extension> tableExtension = List.of(TablesExtension.create());

        // Danh sách công việc
        Set<Extension> taskListExtension = Set.of(TaskListItemsExtension.create());

        // Gạch ngang
        Set<Extension> delExtension = Set.of(StrikethroughExtension.create());

        // Heimu (màn đen)
        Set<Extension> heimuExtension = Set.of(HeimuExtension.create());

        // Lớp che phủ
        Set<Extension> coverExtension = Set.of(CoverExtension.create());

        Parser parser = Parser.builder()
                .extensions(tableExtension)
                .extensions(taskListExtension)
                .extensions(delExtension)
                .extensions(heimuExtension)
                .extensions(coverExtension)
                .build();

        Node document = parser.parse(markdown);

        HtmlRenderer renderer = HtmlRenderer.builder()
                // .extensions(headingAnchorExtensions)
                .extensions(tableExtension)
                .extensions(taskListExtension)
                .extensions(delExtension)
                .extensions(heimuExtension)
                .extensions(coverExtension)
                .attributeProviderFactory(context -> new CustomAttributeProvider())
                .build();

        return renderer.render(document);
    }

    /**
     * Xử lý thuộc tính của thẻ
     */
    static class CustomAttributeProvider implements AttributeProvider {
        @Override
        public void setAttributes(Node node, String tagName, Map<String, String> attributes) {
            // Thay đổi thuộc tính target của thẻ a thành _blank
            if (node instanceof Link) {
                attributes.put("target", "_blank");
                attributes.put("rel", "noreferrer nofollow noopener");
            }
            if (node instanceof TableBlock) {
                attributes.put("class", "ui celled table");
            }
            if(node instanceof Image)
                attributes.put("loading", "lazy");
        }
    }

    public static void main(String[] args) {

        String testMarkdown = """
# Tiêu đề chính

Chào mừng đến với **blog** của tôi!

## Nội dung ẩn (Heimu)
Đoạn văn này chứa @@nội dung bí mật@@.

## Danh sách việc cần làm
- [ ] Viết bài mới
- [x] Cập nhật blog
- [ ] Quảng bá nội dung

## Liên kết
Truy cập [trang chủ](https://blogapi.top) để biết thêm chi tiết.

## Bảng ví dụ
| Ngôn ngữ | Mức độ thông thạo |
|----------|------------------|
| Java | Cao |
| Python | Trung bình |
| JavaScript | Cao |
                """;

        System.out.println("HTML extension:");
        System.out.println(markdownToHtmlExtensions(testMarkdown));
//
//        System.out.println("\n\nHTML basic:");
//        System.out.println(markdownToHtml(testMarkdown));
    }
}