package com.sparta.myblogbackend.entity;

import com.sparta.myblogbackend.dto.ReplyRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "reply")
@NoArgsConstructor
public class Reply extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "reply_contents", nullable = false, length = 500)
    private String replyContents;


    public Reply(ReplyRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.title = requestDto.getTitle();
        this.replyContents = requestDto.getReplyContents();
    }

    public void update(ReplyRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.replyContents = requestDto.getReplyContents();
    }
}
