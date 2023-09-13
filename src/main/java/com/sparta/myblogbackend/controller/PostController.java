package com.sparta.myblogbackend.controller;

import com.sparta.myblogbackend.dto.*;
import com.sparta.myblogbackend.jwt.JwtUtil;
import com.sparta.myblogbackend.security.details.UserDetailsImpl;
import com.sparta.myblogbackend.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Slf4j(topic = "PostController")
@RestControllerAdvice//전역 예외 처리
public class PostController {
    private final PostService postService;


    @PostMapping("/post")
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto requestDto,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(postService.createPost(requestDto, userDetails));
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostResponseDto>> getPosts(){
        return ResponseEntity.ok(postService.getPosts());
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id){
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @PutMapping("/post/{id}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long id,
                                      @RequestBody PostRequestDto requestDto,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(postService.updatePost(id, requestDto, userDetails));
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<PostDeleteResponseDto> deletePost(@PathVariable Long id,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(postService.deletePost(id, userDetails));
    }


    @PostMapping("/post/{id}/reply/")
    public ResponseEntity<ReplyResponseDto> createReply(@PathVariable Long id,
                                        @RequestBody ReplyRequestDto replyRequestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(postService.createReply(id, replyRequestDto, userDetails));
    }

    @PutMapping("/post/{postId}/reply/{replyId}")
    public ResponseEntity<ReplyResponseDto> updatePost(@PathVariable("postId") Long id,
                                       @PathVariable("replyId") Long replyId,
                                       @RequestBody ReplyRequestDto replyRequestDto,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(postService.updateReply(id, replyId, replyRequestDto, userDetails));
    }

    @DeleteMapping("/post/{postId}/reply/{replyId}")
    public ResponseEntity<PostDeleteResponseDto> deleteReply(@PathVariable("postId") Long id,
                                             @PathVariable("replyId") Long replyId,
                                             @RequestBody ReplyRequestDto replyRequestDto,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(postService.deleteReply(id, replyId, replyRequestDto, userDetails));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleNotFoundEntity(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
