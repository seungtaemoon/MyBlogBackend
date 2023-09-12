package com.sparta.myblogbackend.service;

import com.sparta.myblogbackend.dto.*;
import com.sparta.myblogbackend.entity.Post;
import com.sparta.myblogbackend.entity.Reply;
import com.sparta.myblogbackend.jwt.JwtUtil;
import com.sparta.myblogbackend.repository.PostRepository;
import com.sparta.myblogbackend.repository.ReplyRepository;
import com.sparta.myblogbackend.security.UserDetailsImpl;
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

    public PostResponseDto createPost(PostRequestDto requestDto, UserDetailsImpl userDetails){
        // 토큰 검증
        String token = jwtUtil.createToken(userDetails.getUsername(), userDetails.getUser().getRole());
        token = jwtUtil.substringToken(token);
        if ( jwtUtil.validateToken(token)){
            Post post = new Post(requestDto);
            Post savePost = postRepository.save(post);
            PostResponseDto postResponseDto = new PostResponseDto(savePost);
            return postResponseDto;
        } else{
            throw new IllegalArgumentException("유효한 사용자가 아닙니다.");
        }
    }

    // 게시글 조회 API(각 게시글에 댓글도 조회해야 함)
    // 여기서 어떻게 해야 replyReplositry의 findAllByOrderByCreatedAtDesc()를 활용 가능할지?
    public List<PostResponseDto> getPosts(){
        return postRepository.findAllByOrderByCreatedAtDesc().stream().map(PostResponseDto::new).toList();
    }

    public List<PostResponseDto> getPostsByKeyword(String keyword) {
        return postRepository.findAllByContentsContainsOrderByCreatedAtDesc(keyword).stream().map(PostResponseDto::new).toList();
    }

    public PostResponseDto getPostById(Long id){
        Post post = findPost(id);
        return new PostResponseDto(post);
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, UserDetailsImpl userDetails){
        // 입력된 토큰이 저장된 것과 같은지 체크
        String token = jwtUtil.createToken(userDetails.getUsername(), userDetails.getUser().getRole());
        token = jwtUtil.substringToken(token);
        Claims info = jwtUtil.getUserInfoFromToken(token);
        String username = info.getSubject();
        if ( requestDto.getUsername().equals(username) && jwtUtil.validateToken(token)){
            Post post = findPost(id);
            post.update(requestDto);
            return new PostResponseDto(post);
        }
        else{
            throw new IllegalArgumentException("비밀번호가 다릅니다.");
        }
    }

    public PostDeleteResponseDto deletePost(Long id, PostRequestDto requestDto, UserDetailsImpl userDetails){
        // 입력된 토큰이 저장된 것과 같은지 체크
        String token = jwtUtil.createToken(userDetails.getUsername(), userDetails.getUser().getRole());
        token = jwtUtil.substringToken(token);
        Claims info = jwtUtil.getUserInfoFromToken(token);
        String username = info.getSubject();
        if( requestDto.getUsername().equals(username) && jwtUtil.validateToken(token)){
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


    public ReplyResponseDto createReply(Long id, ReplyRequestDto replyRequestDto, String token) {
        // 토큰 검증
        if (jwtUtil.validateToken(jwtUtil.substringToken(token))){
            // 게시물이 DB에 있는지 확인
            if (findPost(id) != null){
                // 댓글을 저장하기전에 reply에 post를 저장
                // 댓글을 게시물에 추가
                Post post = findPost(id);
                Reply reply = new Reply(replyRequestDto);
                post.addReply(reply);
                // 댓글을 저장
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

    public ReplyResponseDto updateReply(Long id, Long replyId, ReplyRequestDto replyRequestDto, String token) {
        // 입력된 토큰이 저장된 것과 같은지 체크
        String jwToken = jwtUtil.substringToken(token);
        Claims info = jwtUtil.getUserInfoFromToken(jwToken);
        String username = info.getSubject();
        if ( replyRequestDto.getUsername().equals(username)){
            // 댓글을 달 게시물이 있는지 체크
            if( findPost(id) != null) {
                Reply reply = findReply(replyId);
                reply.update(replyRequestDto);
                Reply saveReply = replyRepository.save(reply);
                ReplyResponseDto replyResponseDto = new ReplyResponseDto(saveReply);
                return replyResponseDto;
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

    public PostDeleteResponseDto deleteReply(Long id, Long replyId, ReplyRequestDto replyRequestDto, String token) {
        // 입력된 토큰이 저장된 것과 같은지 체크
        String jwToken = jwtUtil.substringToken(token);
        Claims info = jwtUtil.getUserInfoFromToken(jwToken);
        String username = info.getSubject();
        if( replyRequestDto.getUsername().equals(username)){
            if ( findPost(id) != null){
                Reply reply = findReply(replyId);
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
