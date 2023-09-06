package com.sparta.myblogbackend.entity;

import com.sparta.myblogbackend.dto.PostRequestDto;
import com.sparta.myblogbackend.jwt.JwtUtil;
import jakarta.persistence.*;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "contents", nullable = false, length = 500)
    private String contents;
//    @Column(name = "token", nullable = false, unique = true)
//    private String token;


    public Post(PostRequestDto requestDto){
        this.title = requestDto.getTitle();
        this.username = requestDto.getUsername();
        this.contents = requestDto.getContents();
    }

    public void update(PostRequestDto requestDto){
        this.title = requestDto.getTitle();
        this.username = requestDto.getUsername();
        this.contents = requestDto.getContents();
//        this.token = JwtUtil.
    }
}

