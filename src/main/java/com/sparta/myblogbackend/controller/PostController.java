package com.sparta.myblogbackend.controller;

import com.sparta.myblogbackend.dto.*;
import com.sparta.myblogbackend.jwt.JwtUtil;
import com.sparta.myblogbackend.service.PostService;
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
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto, @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String author){
        return postService.createPost(requestDto, author);
    }

    @GetMapping("/posts")
    public List<PostResponseDto> getPosts(){
        return postService.getPosts();
    }

    @GetMapping("/post/{id}")
    public List<PostResponseDto> getPostsByKeyword(String keyword){
        return postService.getPostsByKeyword(keyword);
    }

    @PutMapping("/post/{id}")
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String author){
        return postService.updatePost(id, requestDto, author);
    }

    @DeleteMapping("/post/{id}")
    public PostDeleteResponseDto deletePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String author){
        return postService.deletePost(id, requestDto, author);
    }

    @PostMapping("/post/{id}/reply/{replyId}")
    public ReplyResponseDto createReply(@PathVariable Long id, @PathVariable("replyId") Long replyId, @RequestBody PostRequestDto postRequestDto, ReplyRequestDto replyRequestDto, @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String author){
        return postService.createReply(id, replyId, postRequestDto, replyRequestDto, author);
    }

    @PutMapping("/post/{id}/reply/{replyId}")
    public ReplyResponseDto updatePost(@PathVariable Long id, @PathVariable("replyId") Long replyId, @RequestBody PostRequestDto postRequestDto, ReplyRequestDto replyRequestDto, @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String author){
        return postService.updateReply(id, replyId, postRequestDto, replyRequestDto, author);
    }

    @DeleteMapping("/post/{id}/reply/{replyId}")
    public PostDeleteResponseDto deleteReply(@PathVariable Long id, @PathVariable("replyId") Long replyId, @RequestBody PostRequestDto postRequestDto, ReplyRequestDto replyRequestDto, @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String author){
        return postService.deleteReply(id, replyId, postRequestDto, replyRequestDto, author);
    }


}
