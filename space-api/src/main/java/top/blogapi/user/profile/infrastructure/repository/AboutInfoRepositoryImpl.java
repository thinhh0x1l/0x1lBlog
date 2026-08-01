package top.blogapi.user.profile.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.user.profile.domain.entity.AboutInfo;
import top.blogapi.user.profile.domain.repository.AboutInfoRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AboutInfoRepositoryImpl implements AboutInfoRepository {

    private final AboutInfoJpaRepository jpaAdapter;

    @Override
    public Optional<AboutInfo> findById(Long id) {
        return jpaAdapter.findById(id);
    }

    @Override
    public Optional<AboutInfo> findByType(String type) {
        return jpaAdapter.findFirstByTypeOrderByUpdatedAtDesc(type);
    }

    @Override
    public List<AboutInfo> findAll() {
        return jpaAdapter.findAll();
    }

    @Override
    public void insert(AboutInfo about) {
        Instant now = Instant.now();
        about.setCreatedAt(now);
        about.setUpdatedAt(now);
        jpaAdapter.save(about);
    }

    @Override
    public void update(AboutInfo about) {
        about.setUpdatedAt(Instant.now());
        jpaAdapter.save(about);
    }
}
