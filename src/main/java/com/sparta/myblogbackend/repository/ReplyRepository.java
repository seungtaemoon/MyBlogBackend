package com.sparta.myblogbackend.repository;

import com.sparta.myblogbackend.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findALlByOrderByCreatedAtDesc();

}
