package top.blogapi.service;


import top.blogapi.model.vo.BlogInfo;
import top.blogapi.model.vo.PageResult;

import java.util.List;

public interface RedisService {
    PageResult<BlogInfo> getPageResultByHash(String hash, Integer pageNum);

    void setPageResultToHash(String hash, Integer pageNum, Object object);
}
