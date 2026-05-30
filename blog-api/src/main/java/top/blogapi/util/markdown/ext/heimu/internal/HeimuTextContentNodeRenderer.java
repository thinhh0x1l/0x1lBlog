package top.blogapi.util.markdown.ext.heimu.internal;

import org.commonmark.node.Node;
import org.commonmark.renderer.text.TextContentNodeRendererContext;
import org.commonmark.renderer.text.TextContentWriter;

/**
 * Description: Kết xuất ra text thuần
 * */

public class HeimuTextContentNodeRenderer extends AbstractHeimuNodeRenderer {
    private final TextContentNodeRendererContext context;
    private final TextContentWriter textContext;


    public HeimuTextContentNodeRenderer(TextContentNodeRendererContext context) {
        this.context = context;
        this.textContext = context.getWriter();
    }
    @Override
    public void render(Node node) {
        textContext.write('/');
        renderChildNode(node);
        textContext.write('/');
    }

    private void renderChildNode(Node parent) {
        Node child = parent.getFirstChild();
        while (child != null) {
            Node next = child.getNext();
            context.render(next);
            child = next;
        }
    }
}
