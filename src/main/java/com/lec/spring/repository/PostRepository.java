package com.lec.spring.repository;

import com.lec.spring.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Post findByPostId(Long postId);

    List<Post> findAllByOrderByRegDateDesc();

}