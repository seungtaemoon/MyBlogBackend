package com.sparta.myblogbackend.controller;

import com.sparta.myblogbackend.dto.*;
import com.sparta.myblogbackend.entity.Post;
import com.sparta.myblogbackend.jwt.JwtUtil;
import com.sparta.myblogbackend.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j(topic = "Post controller")
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
    }//ResponseEntity<>으로 리턴하고 , PostRequestDto 에 유저네임 필요 없음

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
    public ReplyResponseDto createReply(@PathVariable("postId") Long id, @PathVariable("replyId") Long replyId, @RequestBody ReplyRequestDto replyRequestDto, @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String author){
        //log.info("id " + id + "/replyid " + replyId + "/Dto" + replyRequestDto.getUsername() + "/token : " + author);//작동 확인
        return postService.createReply(id, replyId, null, replyRequestDto, author);
    }//댓글 생성 할땐 replyId 가 필요하지 않을꺼에요. 일일히 지정하는게 아니라면
    // PostRequestDto는 필요 없을꺼에요. , ReplyRequestDto.username은 쿠키에 이미 있어요. , 댓글에 제목을 달진 않죠?
    // 406 - No Acceptable -> postService.createReply 내부 문제 (Getter는 아님) + 예외 처리에 안잡힘 직접 throw 필요

    @PutMapping("/post/{postId}/reply/{replyId}")
    public ReplyResponseDto updatePost(@PathVariable("postId") Long id, @PathVariable("replyId") Long replyId, @RequestBody PostRequestDto postRequestDto, ReplyRequestDto replyRequestDto, @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String author){
        return postService.updateReply(id, replyId, postRequestDto, replyRequestDto, author);
    }

    @DeleteMapping("/post/{postId}/reply/{replyId}")
    public PostDeleteResponseDto deleteReply(@PathVariable("postId") Long id, @PathVariable("replyId") Long replyId, @RequestBody PostRequestDto postRequestDto, ReplyRequestDto replyRequestDto, @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String author){
        return postService.deleteReply(id, replyId, postRequestDto, replyRequestDto, author);
    }


}
