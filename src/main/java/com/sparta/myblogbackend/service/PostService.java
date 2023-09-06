package com.sparta.myblogbackend.service;

import com.sparta.myblogbackend.dto.PostDeleteResponseDto;
import com.sparta.myblogbackend.dto.PostRequestDto;
import com.sparta.myblogbackend.dto.PostResponseDto;
import com.sparta.myblogbackend.entity.Post;
import com.sparta.myblogbackend.entity.User;
import com.sparta.myblogbackend.jwt.JwtUtil;
import com.sparta.myblogbackend.repository.PostRepository;
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

    @Autowired
    public PostService(PostRepository postRepository, JwtUtil jwtUtil){
        this.postRepository = postRepository;
        this.jwtUtil = jwtUtil;
    }

    public PostResponseDto createPost(PostRequestDto requestDto){
        // 토큰 검증
        if (jwtUtil.validateToken(requestDto.getToken())){
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
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto){
        // 입력된 토큰이 저장된 것과 같은지 체크
        Post post = findPost(id);
        Claims info = jwtUtil.getUserInfoFromToken(requestDto.getToken());
        String username = info.getSubject();
        if ( post.getUsername().equals(username)){
            post.update(requestDto);
            return new PostResponseDto(post);
        }
        else{
            throw new IllegalArgumentException("비밀번호가 다릅니다.");
        }
    }

    public PostDeleteResponseDto deletePost(Long id, PostRequestDto requestDto){
        // 입력된 토큰이 저장된 것과 같은지 체크
        Post post = findPost(id);
        Claims info = jwtUtil.getUserInfoFromToken(requestDto.getToken());
        String username = info.getSubject();
        if( post.getUsername().equals(username)){
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
