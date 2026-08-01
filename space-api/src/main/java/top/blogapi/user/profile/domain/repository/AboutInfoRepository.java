package top.blogapi.user.profile.domain.repository;

import top.blogapi.user.profile.domain.entity.AboutInfo;

import java.util.List;
import java.util.Optional;

public interface AboutInfoRepository {

    Optional<AboutInfo> findById(Long id);

    Optional<AboutInfo> findByType(String type);

    List<AboutInfo> findAll();

    void insert(AboutInfo about);

    void update(AboutInfo about);
}
