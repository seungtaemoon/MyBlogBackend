package com.sparta.myblogbackend.controller;

import com.sparta.myblogbackend.dto.PostDeleteResponseDto;
import com.sparta.myblogbackend.dto.PostRequestDto;
import com.sparta.myblogbackend.dto.PostResponseDto;
import com.sparta.myblogbackend.jwt.JwtUtil;
import com.sparta.myblogbackend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PostController {
    private final PostService postService;


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
}
