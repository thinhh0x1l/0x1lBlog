package top.blogapi.util.markdown.ext.cover.internal;

import org.commonmark.node.Node;
import org.commonmark.renderer.html.HtmlNodeRendererContext;
import org.commonmark.renderer.html.HtmlWriter;

import java.util.HashMap;
import java.util.Map;

/** Renderer HTML cho node Cover — bọc nội dung trong span với lớp m-text-cover. */
public class CoverHtmlNodeRenderer extends AbstractCoverNodeRenderer {

    private final HtmlWriter html;
    private final HtmlNodeRendererContext context;

    public CoverHtmlNodeRenderer( HtmlNodeRendererContext context) {
        this.html = context.getWriter();
        this.context = context;
    }

    @Override
    public void render(Node node) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("class", "m-text-cover");
        html.tag("span", context.extendAttributes(node,"span", attributes));
        renderChildNode(node);
        html.tag("/span");

    }

    private void renderChildNode(Node parent) {
        Node child = parent.getFirstChild();
        while(child != null) {
            Node next = child.getNext();
            context.render(child);
            child = next;
        }
    }
}
