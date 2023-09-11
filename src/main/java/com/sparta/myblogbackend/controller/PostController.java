package com.sparta.myblogbackend.controller;

import com.sparta.myblogbackend.dto.*;
import com.sparta.myblogbackend.entity.Post;
import com.sparta.myblogbackend.jwt.JwtUtil;
import com.sparta.myblogbackend.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public Optional<Post> getPostById(@PathVariable Long id){
        return postService.getPostById(id);
    }

    @PutMapping("/post/{id}")
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String author){
        return postService.updatePost(id, requestDto, author);
    }

    @DeleteMapping("/post/{id}")
    public PostDeleteResponseDto deletePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String author){
        return postService.deletePost(id, requestDto, author);
    }

    @PostMapping("/post/{postId}/reply/{replyId}")
    public ReplyResponseDto createReply(@PathVariable("postId") Long id, @PathVariable("replyId") Long replyId, @RequestBody PostRequestDto postRequestDto, ReplyRequestDto replyRequestDto, @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String author){
        return postService.createReply(id, replyId, postRequestDto, replyRequestDto, author);
    }

    @PutMapping("/post/{postId}/reply/{replyId}")
    public ReplyResponseDto updatePost(@PathVariable("postId") Long id, @PathVariable("replyId") Long replyId, @RequestBody PostRequestDto postRequestDto, ReplyRequestDto replyRequestDto, @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String author){
        return postService.updateReply(id, replyId, postRequestDto, replyRequestDto, author);
    }

    @DeleteMapping("/post/{postId}/reply/{replyId}")
    public PostDeleteResponseDto deleteReply(@PathVariable("postId") Long id, @PathVariable("replyId") Long replyId, @RequestBody PostRequestDto postRequestDto, ReplyRequestDto replyRequestDto, @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String author){
        return postService.deleteReply(id, replyId, postRequestDto, replyRequestDto, author);
    }


}
