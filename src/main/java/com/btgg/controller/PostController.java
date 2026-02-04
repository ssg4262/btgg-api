package com.btgg.controller;

import com.btgg.dto.request.PostCreateRequest;
import com.btgg.dto.request.PostSearchRequest;
import com.btgg.dto.request.PostUpdateRequest;
import com.btgg.dto.response.ApiResponse;
import com.btgg.dto.response.PostListResponse;
import com.btgg.dto.response.PostResponse;
import com.btgg.security.CustomUserDetails;
import com.btgg.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> create(
            @Valid @RequestBody PostCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        PostResponse response = postService.create(request, userDetails.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("게시글이 작성되었습니다", response));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> getPost(@PathVariable Long postId) {
        PostResponse response = postService.getPost(postId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostListResponse>>> search(
            @ModelAttribute PostSearchRequest searchRequest,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostListResponse> response = postService.search(searchRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> update(
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        PostResponse response = postService.update(postId, request, userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.success("게시글이 수정되었습니다", response));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        postService.delete(postId, userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.success("게시글이 삭제되었습니다"));
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<ApiResponse<Boolean>> toggleLike(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        boolean liked = postService.toggleLike(postId, userDetails.getUserId());
        String message = liked ? "좋아요를 눌렀습니다" : "좋아요를 취소했습니다";
        return ResponseEntity.ok(ApiResponse.success(message, liked));
    }

    @GetMapping("/{postId}/like")
    public ResponseEntity<ApiResponse<Boolean>> isLiked(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        boolean liked = postService.isLiked(postId, userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.success(liked));
    }
}
