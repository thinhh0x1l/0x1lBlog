package top.blogapi.content.blog.domain.event;

import top.blogapi.shared.event.DomainEvent;

public class BlogPublishedEvent extends DomainEvent {

    private final Long blogId;
    private final Long authorId;

    public BlogPublishedEvent(Long blogId, Long authorId) {
        super("blog.published");
        this.blogId = blogId;
        this.authorId = authorId;
    }

    public Long getBlogId() { return blogId; }
    public Long getAuthorId() { return authorId; }
}
