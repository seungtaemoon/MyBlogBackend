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
public class PostController {
    private final PostService postService;


    @PostMapping("/post")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails){
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
    public PostResponseDto updatePost(@PathVariable Long id,
                                      @RequestBody PostRequestDto requestDto,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.updatePost(id, requestDto, userDetails);
    }

    @DeleteMapping("/post/{id}")
    public PostDeleteResponseDto deletePost(@PathVariable Long id,
                                            @RequestBody PostRequestDto requestDto,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.deletePost(id, requestDto, userDetails);
    }


    @GetMapping("/post/replyTest/")
    public ResponseEntity<String> TestReply(
                                        //@CookieValue(JwtUtil.AUTHORIZATION_HEADER) String author
                                        @AuthenticationPrincipal UserDetailsImpl userDetails// 쿠키랑 같이 사용 불가
                                        ){

        if (userDetails == null){

            log.error("userDetails is null");
            return ResponseEntity.badRequest().body("userDetails is null");
        }
        else
            return ResponseEntity.ok(userDetails.getUsername());
    }

    @PostMapping("/post/{id}/reply/")
    public ReplyResponseDto createReply(@PathVariable Long id,
                                        @RequestBody ReplyRequestDto replyRequestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.createReply(id, replyRequestDto, userDetails);
    }

    @PutMapping("/post/{postId}/reply/{replyId}")
    public ReplyResponseDto updatePost(@PathVariable("postId") Long id,
                                       @PathVariable("replyId") Long replyId,
                                       @RequestBody ReplyRequestDto replyRequestDto,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.updateReply(id, replyId, replyRequestDto, userDetails);
    }

    @DeleteMapping("/post/{postId}/reply/{replyId}")
    public PostDeleteResponseDto deleteReply(@PathVariable("postId") Long id,
                                             @PathVariable("replyId") Long replyId,
                                             @RequestBody ReplyRequestDto replyRequestDto,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.deleteReply(id, replyId, replyRequestDto, userDetails);
    }


}
