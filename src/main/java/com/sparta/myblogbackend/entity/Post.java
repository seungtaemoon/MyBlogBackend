package com.sparta.myblogbackend.entity;

import com.sparta.myblogbackend.dto.PostRequestDto;
import jakarta.persistence.*;
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

    @OneToMany(mappedBy = "post" , cascade = CascadeType.ALL)
    private List<Reply> replyList = new ArrayList<>();// 구조 다시 짜기!


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
    public void removeReply(Reply reply) {this.replyList.remove(reply); }
}

