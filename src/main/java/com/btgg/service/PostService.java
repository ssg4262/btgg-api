package com.btgg.service;

import com.btgg.dto.request.PostCreateRequest;
import com.btgg.dto.request.PostSearchRequest;
import com.btgg.dto.request.PostUpdateRequest;
import com.btgg.dto.response.PostListResponse;
import com.btgg.dto.response.PostResponse;
import com.btgg.entity.Post;
import com.btgg.entity.PostLike;
import com.btgg.entity.User;
import com.btgg.repository.PostLikeRepository;
import com.btgg.repository.PostRepository;
import com.btgg.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public PostResponse create(PostCreateRequest request, Long userId) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(author)
                .build();

        Post savedPost = postRepository.save(post);
        return PostResponse.from(savedPost, 0);
    }

    @Transactional
    public PostResponse getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다"));

        post.increaseViewCount();
        long likeCount = postLikeRepository.countByPostId(postId);

        return PostResponse.from(post, likeCount);
    }

    public Page<PostListResponse> search(PostSearchRequest searchRequest, Pageable pageable) {
        Page<Post> posts = postRepository.search(searchRequest, pageable);

        return posts.map(post -> {
            long likeCount = postLikeRepository.countByPostId(post.getId());
            long commentCount = post.getComments().size();
            return PostListResponse.from(post, likeCount, commentCount);
        });
    }

    @Transactional
    public PostResponse update(Long postId, PostUpdateRequest request, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다"));

        if (!post.getAuthor().getId().equals(userId)) {
            throw new IllegalArgumentException("수정 권한이 없습니다");
        }

        post.update(request.getTitle(), request.getContent());
        long likeCount = postLikeRepository.countByPostId(postId);

        return PostResponse.from(post, likeCount);
    }

    @Transactional
    public void delete(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다"));

        if (!post.getAuthor().getId().equals(userId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다");
        }

        postRepository.delete(post);
    }

    @Transactional
    public boolean toggleLike(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        return postLikeRepository.findByPostIdAndUserId(postId, userId)
                .map(like -> {
                    postLikeRepository.delete(like);
                    return false;
                })
                .orElseGet(() -> {
                    PostLike newLike = PostLike.builder()
                            .post(post)
                            .user(user)
                            .build();
                    postLikeRepository.save(newLike);
                    return true;
                });
    }

    public boolean isLiked(Long postId, Long userId) {
        return postLikeRepository.existsByPostIdAndUserId(postId, userId);
    }
}
