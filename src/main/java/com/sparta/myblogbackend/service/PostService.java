package com.sparta.myblogbackend.service;

import com.sparta.myblogbackend.dto.*;
import com.sparta.myblogbackend.entity.Post;
import com.sparta.myblogbackend.entity.Reply;
import com.sparta.myblogbackend.jwt.JwtUtil;
import com.sparta.myblogbackend.repository.PostRepository;
import com.sparta.myblogbackend.repository.ReplyRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;
    private final ReplyRepository replyRepository;

    @Autowired
    public PostService(PostRepository postRepository, JwtUtil jwtUtil, ReplyRepository replyRepository){
        this.postRepository = postRepository;
        this.jwtUtil = jwtUtil;
        this.replyRepository = replyRepository;
    }

    public PostResponseDto createPost(PostRequestDto requestDto, String token){
        // 토큰 검증
        if (jwtUtil.validateToken(jwtUtil.substringToken(token))){
            Post post = new Post(requestDto);
            Post savePost = postRepository.save(post);
            PostResponseDto postResponseDto = new PostResponseDto(savePost);
            return postResponseDto;
        } else{
            throw new IllegalArgumentException("유효한 사용자가 아닙니다.");
        }
    }

    public List<PostResponseDto> getPosts(){
        return postRepository.findAllByOrderByCreatedAtDesc().stream().map(PostResponseDto::new).toList();
    }

    public List<PostResponseDto> getPostsByKeyword(String keyword) {
        return postRepository.findAllByContentsContainsOrderByCreatedAtDesc(keyword).stream().map(PostResponseDto::new).toList();
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, String token){
        // 입력된 토큰이 저장된 것과 같은지 체크
        String jwToken = jwtUtil.substringToken(token);
        Claims info = jwtUtil.getUserInfoFromToken(jwToken);
        String username = info.getSubject();
        if ( requestDto.getUsername().equals(username)){
            Post post = findPost(id);
            post.update(requestDto);
            return new PostResponseDto(post);
        }
        else{
            throw new IllegalArgumentException("비밀번호가 다릅니다.");
        }
    }

    public PostDeleteResponseDto deletePost(Long id, PostRequestDto requestDto, String token){
        // 입력된 토큰이 저장된 것과 같은지 체크
        String jwToken = jwtUtil.substringToken(token);
        Claims info = jwtUtil.getUserInfoFromToken(jwToken);
        String username = info.getSubject();
        if( requestDto.getUsername().equals(username)){
            Post post = findPost(id);
            postRepository.delete(post);
            PostDeleteResponseDto deleteResponseDto = new PostDeleteResponseDto(200, HttpStatus.OK, "성공적으로 삭제 되었습니다.");
            return deleteResponseDto;
        }
        else{
            PostDeleteResponseDto deleteResponseDto = new PostDeleteResponseDto(400, HttpStatus.BAD_REQUEST, "토큰이 잘못 되었습니다.");
            return deleteResponseDto;
        }
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("데이터가 없습니다.")
        );
    }

    public ReplyResponseDto createReply(Long id, PostRequestDto postRequestDto, ReplyRequestDto replyRequestDto, String token) {
        // 토큰 검증
        if (jwtUtil.validateToken(jwtUtil.substringToken(token))){
            // 게시물이 DB에 있는지 확인
            if (postRequestDto.getTitle().equals(replyRequestDto.getTitle())){
                Reply reply = new Reply(replyRequestDto);
                Reply saveReply = replyRepository.save(reply);
                ReplyResponseDto replyResponseDto = new ReplyResponseDto(saveReply);
                return replyResponseDto;
            } else{
                throw new IllegalArgumentException("작성할 게시물이 유효하지 않습니다.");
            }

        } else{
            throw new IllegalArgumentException("유효한 사용자가 아닙니다.");
        }
    }

    public ReplyResponseDto updateReply(Long id, PostRequestDto postRequestDto, ReplyRequestDto replyRequestDto, String token) {
        // 입력된 토큰이 저장된 것과 같은지 체크
        String jwToken = jwtUtil.substringToken(token);
        Claims info = jwtUtil.getUserInfoFromToken(jwToken);
        String username = info.getSubject();
        if ( replyRequestDto.getUsername().equals(username)){
            // 댓글을 달 게시물이 있는지 체크
            if( postRequestDto.getTitle().equals(replyRequestDto.getTitle())) {
                Reply reply = findReply(id);
                reply.update(replyRequestDto);
                return new ReplyResponseDto(reply);
            } else{
                throw new IllegalArgumentException("작성할 게시물이 유효하지 않습니다.");
            }
        }
        else{
            throw new IllegalArgumentException("비밀번호가 다릅니다.");
        }
    }

    private Reply findReply(Long id) {
        return replyRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("데이터가 없습니다.")
        );
    }

    public PostDeleteResponseDto deleteReply(Long id, PostRequestDto postRequestDto, ReplyRequestDto replyRequestDto, String token) {
        // 입력된 토큰이 저장된 것과 같은지 체크
        String jwToken = jwtUtil.substringToken(token);
        Claims info = jwtUtil.getUserInfoFromToken(jwToken);
        String username = info.getSubject();
        if( replyRequestDto.getUsername().equals(username)){
            if ( postRequestDto.getTitle().equals(replyRequestDto.getTitle())){
                Reply reply = findReply(id);
                replyRepository.delete(reply);
                PostDeleteResponseDto deleteResponseDto = new PostDeleteResponseDto(200, HttpStatus.OK, "성공적으로 삭제 되었습니다.");
                return deleteResponseDto;
            } else{
                PostDeleteResponseDto deleteResponseDto = new PostDeleteResponseDto(400, HttpStatus.BAD_REQUEST, "삭제할 게시물이 유효하지 않습니다.");
                return deleteResponseDto;
            }
        }
        else{
            PostDeleteResponseDto deleteResponseDto = new PostDeleteResponseDto(400, HttpStatus.BAD_REQUEST, "토큰이 잘못 되었습니다.");
            return deleteResponseDto;
        }
    }
}
