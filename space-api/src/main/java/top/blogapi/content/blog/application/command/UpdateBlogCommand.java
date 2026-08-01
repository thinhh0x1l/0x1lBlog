package top.blogapi.content.blog.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.content.blog.domain.entity.Blog;
import top.blogapi.content.blog.domain.service.BlogService;
import top.blogapi.content.blog.interfaces.dto.UpdateBlogRequest;
import top.blogapi.shared.exception.AppException;
import top.blogapi.shared.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class UpdateBlogCommand {

    private final BlogService blogService;

    @Transactional
    public Blog execute(Long id, UpdateBlogRequest request, Long userId) {
        Blog blog = blogService.findById(id);
        if (!blog.getAuthorId().equals(userId)) {
            throw new AppException(ErrorCode.FORBIDDEN, "Bạn không có quyền chỉnh sửa bài viết này");
        }
        if (request.title() != null) blog.setTitle(request.title());
        if (request.content() != null) blog.setContent(request.content());
        if (request.description() != null) blog.setDescription(request.description());
        if (request.coverImage() != null) blog.setCoverImage(request.coverImage());
        if (request.categoryId() != null) blog.setCategoryId(request.categoryId());
        if (request.contentType() != null) blog.setContentType(request.contentType());
        if (request.allowComments() != null) blog.setAllowComments(request.allowComments());
        return blogService.update(blog);
    }
}
