package top.blogapi.service;

import top.blogapi.model.entity.About;

import java.util.List;


public interface AboutService {
    List<About> getAboutInfo();

    int updateAbout(List<About> list);
}
