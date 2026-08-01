package top.blogapi.util.markdown.ext.cover.internal;

import org.commonmark.node.Node;
import org.commonmark.renderer.NodeRenderer;
import top.blogapi.util.markdown.ext.cover.Cover;

import java.util.Set;

/** Triển khai cơ sở {@link NodeRenderer} đăng ký loại node Cover. */
abstract class AbstractCoverNodeRenderer implements NodeRenderer {
    @Override
    public Set<Class<? extends Node>> getNodeTypes() {
        return Set.of(Cover.class);
    }
}
