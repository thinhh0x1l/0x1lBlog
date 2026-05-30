package top.blogapi.util.markdown.ext.heimu.internal;

import org.commonmark.node.Node;
import org.commonmark.renderer.html.HtmlNodeRendererContext;
import org.commonmark.renderer.html.HtmlWriter;


import java.util.HashMap;
import java.util.Map;

/**
 * Description: Kết xuất ra Html
 * */

public class HeimuHtmlNodeRenderer extends AbstractHeimuNodeRenderer {
    private final HtmlWriter html;
    private final HtmlNodeRendererContext context;

    public HeimuHtmlNodeRenderer(HtmlNodeRendererContext context) {
        this.context = context;
        this.html = context.getWriter();
    }

    @Override
    public void render(Node node) {
        Map<String,String> attributes = new HashMap<>();
        attributes.put("class", "m-text-heimu");
        attributes.put("title", "Bạn biết quá nhiều");
        html.tag("span", context.extendAttributes(node,"span",attributes));
        renderChildNode(node);
        html.tag("/span");
    }

    private void renderChildNode(Node parent) {
        Node child = parent.getFirstChild();
        while (child != null) {
            Node next = child.getNext();
            context.render(child);
            child = next;
        }
    }
}
