package top.blogapi.content.category.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.content.category.domain.service.CategoryService;

@Service
@RequiredArgsConstructor
public class DeleteCategoryCommand {

    private final CategoryService categoryService;

    @Transactional
    public void execute(Long id) {
        categoryService.softDelete(id);
    }
}
