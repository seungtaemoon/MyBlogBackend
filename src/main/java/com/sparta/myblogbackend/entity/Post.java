package com.sparta.myblogbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.myblogbackend.dto.PostRequestDto;
import com.sparta.myblogbackend.jwt.JwtUtil;
import jakarta.persistence.*;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "post")
@NoArgsConstructor
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "contents", nullable = false, length = 500)
    private String contents;

    @JsonIgnore// 이건 뭔가요?
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)//변경시 관련 엔티티 업데이트 추가
    private List<Reply> replyList = new ArrayList<>();


    public Post(PostRequestDto requestDto){
        this.title = requestDto.getTitle();
        this.username = requestDto.getUsername();
        this.contents = requestDto.getContents();
    }

    public void update(PostRequestDto requestDto){
        this.title = requestDto.getTitle();
        this.username = requestDto.getUsername();
        this.contents = requestDto.getContents();
    }

    // 댓글 더하기
    public void addReply(Reply reply){
        this.replyList.add(reply);
        reply.setPost(this);
    }
}

