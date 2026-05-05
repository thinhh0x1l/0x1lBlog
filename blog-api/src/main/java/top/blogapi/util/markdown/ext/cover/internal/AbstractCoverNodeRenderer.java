package top.blogapi.util.markdown.ext.cover.internal;

import org.commonmark.node.Node;
import org.commonmark.renderer.NodeRenderer;
import top.blogapi.util.markdown.ext.cover.Cover;

import java.util.Set;

abstract class AbstractCoverNodeRenderer implements NodeRenderer {
    @Override
    public Set<Class<? extends Node>> getNodeTypes() {
        return Set.of(Cover.class);
    }
}
