package com.sparta.board.dto;

import com.sparta.board.entity.Board;
import com.sparta.board.role.Role;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;


@Data
@Builder
@AllArgsConstructor
public class SignupRequestDto {

    @NotBlank(message = "아이디를 입력해주세요")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Pattern(regexp = "([a-zA-Z0-9_.+-]+)@([a-zA-Z0-9_.+-]+)\\.([a-zA-Z0-9_.+-]+)")
    private String password;

    private String checkedPassword;

    private Role role;

    @Builder
    public Board toEntity(){
        return Board.builder()
                .username(username)
                .password(password)
                .role(role)
                .build();
    }
}
