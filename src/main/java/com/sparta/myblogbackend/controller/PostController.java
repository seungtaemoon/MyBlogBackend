package com.sparta.myblogbackend.controller;

import com.sparta.myblogbackend.dto.*;
import com.sparta.myblogbackend.entity.Post;
import com.sparta.myblogbackend.jwt.JwtUtil;
import com.sparta.myblogbackend.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/post/{postId}/reply")
    public ResponseEntity<String> createReply(@PathVariable("postId") Long id,
                                      @RequestBody ReplyRequestDto replyRequestDto,
                                      @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String author){


        return ResponseEntity.ok(postService.createReply(id, replyRequestDto, author).toString());
    }//댓글 생성 할땐 replyId 가 필요하지 않을꺼에요. 일일히 지정하는게 아니라면
    // PostRequestDto는 필요 없을꺼에요. , ReplyRequestDto.username은 쿠키에 이미 있어요. , 댓글에 제목을 달진 않죠?
    // 406 - No Acceptable -> postService.createReply 내부 문제 (Getter는 아님) + 예외 처리에 안잡힘 직접 throw 필요
        //리턴을 Null으로 하니까 406 에러는 사라짐 , 대신 DB에 올라가지 않음 -> DB에 Reply를 추가 안함

    @PutMapping("/post/{postId}/reply")
    public ReplyResponseDto updatePost(@PathVariable("postId") Long id, @RequestBody ReplyRequestDto replyRequestDto, @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String author){
        return postService.updateReply(id, replyRequestDto, author);
    }//PostRequestDto를 굳이 필요하지 않고 id 으로 검색하면 되고

    @DeleteMapping("/post/{postId}/reply")
    public PostDeleteResponseDto deleteReply(@PathVariable("postId") Long id, @RequestBody ReplyRequestDto replyRequestDto, @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String author){
        return postService.deleteReply(id, replyRequestDto, author);
    }


}
