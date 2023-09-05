package com.sparta.board.controller;
import com.sparta.board.dto.BoardRequestDto;
import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.dto.SignupRequestDto;
import com.sparta.board.repository.BoardRepository;
import com.sparta.board.service.BoardService;
import com.sparta.board.service.SignupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api")
public class BoardController {

    private final BoardService boardService;
    private final SignupService signupService;
    private final BoardRepository boardRepository;

    public BoardController(BoardService boardService, SignupService signupService, BoardRepository boardRepository) {
        this.boardService = boardService;
        this.signupService = signupService;
        this.boardRepository = boardRepository;
    }

    @PostMapping("/join")
    @ResponseStatus(HttpStatus.OK)
    public Long join(@Valid @RequestBody SignupRequestDto request) throws Exception{
        return signupService.signUp(request);
    }

    @PostMapping("/post")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto) {
        return boardService.createBoard(requestDto);
    }
    @GetMapping("/posts")
    public List<BoardResponseDto> getBoards() {
        return boardService.getBoards();
    }
    @GetMapping("/post/{id}")
    public List<BoardResponseDto> getBoardsByKeyword(String keyword) {
        return boardService.getBoardsByKeyword(keyword);
    }
    @PutMapping("/post/{id}")
    public BoardResponseDto updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto) {
        return boardService.updateBoard(id, requestDto);
    }
    @DeleteMapping("/post/{id}")
    public BoardResponseDto deleteBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto) {
        return boardService.deleteBoard(id, requestDto);
    }

}