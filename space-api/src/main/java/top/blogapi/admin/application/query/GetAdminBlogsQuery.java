package top.blogapi.admin.application.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.blogapi.admin.domain.repository.AdminBlogRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAdminBlogsQuery {

    private final AdminBlogRepository adminBlogRepository;

    public List<Object[]> execute(int page, int size) {
        return adminBlogRepository.findPublished(page, size);
    }
}
