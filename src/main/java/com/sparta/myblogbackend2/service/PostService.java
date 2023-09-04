package com.sparta.myblogbackend2.service;

import com.sparta.myblogbackend2.dto.PostRequestDto;
import com.sparta.myblogbackend2.dto.PostResponseDto;
import com.sparta.myblogbackend2.entity.Post;
import com.sparta.myblogbackend2.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    public PostResponseDto createPost(PostRequestDto requestDto){
        // RequestDto -> Entity
        Post post = new Post(requestDto);

        // DB 저장
        Post savePost = postRepository.save(post);

        // Entity -> ResponseDto
        PostResponseDto postResponseDto = new PostResponseDto(savePost);
        return postResponseDto;

    }

    public List<PostResponseDto> getPosts() {
        // DB 조회
        return postRepository.findAllByOrderByCreatedAtDesc().stream().map(PostResponseDto::new).toList();
    }

    public List<PostResponseDto> getPostsByKeyword(String keyword) {
        return postRepository.findAllByContentsContainsOrderByCreatedAtDesc(keyword).stream().map(PostResponseDto::new).toList();
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto){
        // 해당 메모가 DB에 존재하는지 확인
        Post post = findPost(id);
        // memo 삭제
        if( requestDto.getPassword().equals(post.getPassword())){
            post.update(requestDto);
            return new PostResponseDto(post);
        }
        else{
            throw new IllegalArgumentException("비밀번호가 다릅니다.");
        }
    }

    public PostResponseDto deletePost(Long id, PostRequestDto requestDto){
        // 해당 메모가 DB에 존재하는지 확인
        Post post = findPost(id);
        // memo 삭제
        if(requestDto.getPassword().equals(post.getPassword())){
            postRepository.delete(post);
            return new PostResponseDto(post);
        }
        else{
            throw new IllegalArgumentException("비밀번호가 다릅니다.");
        }
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("데이터가 없습니다.")
        );
    }
}
