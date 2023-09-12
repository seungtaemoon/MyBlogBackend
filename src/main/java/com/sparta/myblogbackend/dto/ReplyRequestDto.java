package com.sparta.myblogbackend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReplyRequestDto {
    private String username; // 댓글 작성자로써의 username
    private String title;   // 댓글 제목
    private String replyContents;  // 댓글 내용
}
