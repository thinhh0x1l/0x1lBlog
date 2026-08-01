package top.blogapi.util.markdown.ext.heimu;

import org.commonmark.Extension;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlNodeRendererFactory;
import org.commonmark.renderer.html.HtmlRenderer;
import org.commonmark.renderer.text.TextContentNodeRendererFactory;
import org.commonmark.renderer.text.TextContentRenderer;
import top.blogapi.util.markdown.ext.heimu.internal.HeimuDelimiterProcessor;
import top.blogapi.util.markdown.ext.heimu.internal.HeimuHtmlNodeRenderer;
import top.blogapi.util.markdown.ext.heimu.internal.HeimuTextContentNodeRenderer;


/**
 * Description: class tiện ích blackout mở rộng
 * */

public class HeimuExtension implements Parser.ParserExtension, HtmlRenderer.HtmlRendererExtension, TextContentRenderer.TextContentRendererExtension {

    private HeimuExtension() {
    }

    public static Extension create() {
        return new HeimuExtension();
    }

    @Override
    public void extend(Parser.Builder builder) {
        builder.customDelimiterProcessor(new HeimuDelimiterProcessor());
    }

    @Override
    public void extend(HtmlRenderer.Builder builder) {
        /**
             rendererBuilder.nodeRendererFactory(new HtmlNodeRendererFactory() {
                @Override
                public NodeRenderer create(HtmlNodeRendererContext context) {
                    return new HeimuHtmlNodeRenderer(context);
                }
            }
         */
        builder.nodeRendererFactory((HtmlNodeRendererFactory) HeimuHtmlNodeRenderer::new);
    }


    @Override
    public void extend(TextContentRenderer.Builder builder) {
        builder.nodeRendererFactory((TextContentNodeRendererFactory) HeimuTextContentNodeRenderer::new);
    }
}
