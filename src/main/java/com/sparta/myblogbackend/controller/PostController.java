package com.sparta.myblogbackend.controller;

import com.sparta.myblogbackend.dto.*;
import com.sparta.myblogbackend.jwt.JwtUtil;
import com.sparta.myblogbackend.security.UserDetailsImpl;
import com.sparta.myblogbackend.service.PostService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService){
        this.postService = postService;
    }

    @PostMapping("/post")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.createPost(requestDto, userDetails);
    }

    @GetMapping("/posts")
    public List<PostResponseDto> getPosts(){
        return postService.getPosts();
    }

    @GetMapping("/post/{id}")
    public PostResponseDto getPostById(@PathVariable Long id){
        return postService.getPostById(id);
    }

    @PutMapping("/post/{id}")
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.updatePost(id, requestDto, userDetails);
    }

    @DeleteMapping("/post/{id}")
    public PostDeleteResponseDto deletePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.deletePost(id, requestDto, userDetails);
    }

    @PostMapping("/post/{id}/reply/")
    public ReplyResponseDto createReply(@PathVariable Long id, @RequestBody ReplyRequestDto replyRequestDto, @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String author){
        return postService.createReply(id, replyRequestDto, author);
    }

    @PutMapping("/post/{postId}/reply/{replyId}")
    public ReplyResponseDto updatePost(@PathVariable("postId") Long id, @PathVariable("replyId") Long replyId, @RequestBody ReplyRequestDto replyRequestDto, @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String author){
        return postService.updateReply(id, replyId, replyRequestDto, author);
    }

    @DeleteMapping("/post/{postId}/reply/{replyId}")
    public PostDeleteResponseDto deleteReply(@PathVariable("postId") Long id, @PathVariable("replyId") Long replyId, @RequestBody ReplyRequestDto replyRequestDto, @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String author){
        return postService.deleteReply(id, replyId, replyRequestDto, author);
    }


}
