package top.blogapi.admin.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.admin.domain.repository.AdminCommentRepository;

@Service
@RequiredArgsConstructor
public class AdminModerateCommentCommand {

    private final AdminCommentRepository adminCommentRepository;

    @Transactional
    public void approve(Long id) {
        adminCommentRepository.approve(id);
    }

    @Transactional
    public void reject(Long id) {
        adminCommentRepository.reject(id);
    }

    @Transactional
    public void flag(Long id) {
        adminCommentRepository.flag(id);
    }

    @Transactional
    public void delete(Long id) {
        adminCommentRepository.softDelete(id);
    }
}
