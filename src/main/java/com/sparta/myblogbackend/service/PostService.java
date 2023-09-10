package com.sparta.myblogbackend.service;

import com.sparta.myblogbackend.dto.PostDeleteResponseDto;
import com.sparta.myblogbackend.dto.PostRequestDto;
import com.sparta.myblogbackend.dto.PostResponseDto;
import com.sparta.myblogbackend.entity.Post;
import com.sparta.myblogbackend.jwt.JwtUtil;
import com.sparta.myblogbackend.repository.PostRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;


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

}
