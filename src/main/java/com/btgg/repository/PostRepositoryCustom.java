package com.btgg.repository;

import com.btgg.dto.request.PostSearchRequest;
import com.btgg.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<Post> search(PostSearchRequest searchRequest, Pageable pageable);
}
