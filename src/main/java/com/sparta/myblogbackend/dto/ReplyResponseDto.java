package com.sparta.myblogbackend.dto;

import com.sparta.myblogbackend.entity.Reply;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReplyResponseDto {
    private Long id;
    private String username;
    private String title;
    private String replyContents;
    private LocalDateTime createdAt;

    public ReplyResponseDto(Reply reply){
        this.id = reply.getId();
        this.username = reply.getUsername();
        this.title = reply.getTitle();
        this.replyContents = reply.getReplyContents();
        this.createdAt = reply.getCreatedAt();
    }
}
