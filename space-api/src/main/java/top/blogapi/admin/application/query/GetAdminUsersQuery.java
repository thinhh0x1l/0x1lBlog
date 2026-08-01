package top.blogapi.admin.application.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.blogapi.admin.domain.repository.AdminUserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GetAdminUsersQuery {

    private final AdminUserRepository adminUserRepository;

    public Map<String, Object> execute(int page, int size) {
        List<Object[]> users = adminUserRepository.findAll(page, size);
        long total = adminUserRepository.count();
        Map<String, Object> result = new HashMap<>();
        result.put("users", users);
        result.put("totalElements", total);
        result.put("page", page);
        result.put("size", size);
        result.put("totalPages", (int) Math.ceil((double) total / size));
        return result;
    }
}
