package com.sparta.board.security;

import com.sparta.board.dto.SignupRequestDto;
import com.sparta.board.entity.Board;
import com.sparta.board.repository.BoardRepository;
import com.sparta.board.service.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class SignupServiceImpl implements SignupService {

    private final BoardRepository boardRepository;
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public Long signUp(SignupRequestDto requestDto) throws Exception{
        if(boardRepository.findByUsername(requestDto.getUsername()).isPresent()){
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        if(!requestDto.getPassword().equals(requestDto.getCheckedPassword())){
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }

        Board board = boardRepository.save(requestDto.toEntity());
        board.encodePassword(passwordEncoder);

        board.addUserAuthority();
        return board.getId();
    }

}
