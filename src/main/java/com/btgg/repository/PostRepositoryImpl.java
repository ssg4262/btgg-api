package com.btgg.repository;

import com.btgg.dto.request.PostSearchRequest;
import com.btgg.entity.Post;
import com.btgg.entity.QPost;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Post> search(PostSearchRequest searchRequest, Pageable pageable) {
        QPost post = QPost.post;

        List<Post> content = queryFactory
                .selectFrom(post)
                .leftJoin(post.author).fetchJoin()
                .where(
                        titleContains(searchRequest.getTitle()),
                        contentContains(searchRequest.getContent()),
                        authorNicknameContains(searchRequest.getAuthorNickname())
                )
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post)
                .where(
                        titleContains(searchRequest.getTitle()),
                        contentContains(searchRequest.getContent()),
                        authorNicknameContains(searchRequest.getAuthorNickname())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression titleContains(String title) {
        return StringUtils.hasText(title) ? QPost.post.title.containsIgnoreCase(title) : null;
    }

    private BooleanExpression contentContains(String content) {
        return StringUtils.hasText(content) ? QPost.post.content.containsIgnoreCase(content) : null;
    }

    private BooleanExpression authorNicknameContains(String nickname) {
        return StringUtils.hasText(nickname) ? QPost.post.author.nickname.containsIgnoreCase(nickname) : null;
    }
}
