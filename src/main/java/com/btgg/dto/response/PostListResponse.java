package com.btgg.dto.response;

import com.btgg.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class PostListResponse {
    private Long id;
    private String title;
    private Integer viewCount;
    private Long likeCount;
    private Long commentCount;
    private AuthorInfo author;
    private LocalDateTime createdAt;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class AuthorInfo {
        private Long id;
        private String nickname;
    }

    public static PostListResponse from(Post post, long likeCount, long commentCount) {
        return PostListResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .viewCount(post.getViewCount())
                .likeCount(likeCount)
                .commentCount(commentCount)
                .author(AuthorInfo.builder()
                        .id(post.getAuthor().getId())
                        .nickname(post.getAuthor().getNickname())
                        .build())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
