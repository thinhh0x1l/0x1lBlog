package top.blogapi.util.markdown.ext.heimu.internal;

import org.commonmark.node.Node;
import org.commonmark.renderer.NodeRenderer;
import top.blogapi.util.markdown.ext.heimu.Heimu;

import java.util.Set;

abstract class AbstractHeimuNodeRenderer implements NodeRenderer {
    @Override
    public Set<Class<? extends Node>> getNodeTypes() {
        return Set.of(Heimu.class);
    }
}
