package top.blogapi.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.model.entity.About;
import top.blogapi.repository.AboutRepository;
import top.blogapi.service.AboutService;
import top.blogapi.util.markdown.MarkdownUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@Slf4j
@Transactional
public class AboutServiceImpl implements AboutService {
    AboutRepository aboutRepository;

    @Override
    public List<About> getAboutInfo() {
        return aboutRepository.getList();
    }

    @Override
    public int updateAbout(List<About> list) {
        return aboutRepository.updateAbout(list);
    }
}
