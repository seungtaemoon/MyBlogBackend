package com.sparta.myblogbackend.repository;

import com.sparta.myblogbackend.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();

    List<Post> findAllByContentsContainsOrderByCreatedAtDesc(String keyword);
}
