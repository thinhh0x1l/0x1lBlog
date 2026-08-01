package top.blogapi.util.markdown.ext.cover.internal;

import org.commonmark.node.Node;
import org.commonmark.renderer.text.TextContentNodeRendererContext;
import org.commonmark.renderer.text.TextContentWriter;

/** Renderer nội dung văn bản cho node Cover — hiển thị nội dung được bọc trong dấu gạch chéo. */
public class CoverTextContentNodeRenderer extends AbstractCoverNodeRenderer {

    private final TextContentWriter text;
    private final TextContentNodeRendererContext context;

    public CoverTextContentNodeRenderer( TextContentNodeRendererContext context) {
        this.text = context.getWriter();
        this.context = context;
    }

    @Override
    public void render(Node node) {
        text.write('/');
        renderChildNode(node);
        text.write('/');
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
