package com.lec.spring.service;

import com.lec.spring.domain.Post;
import com.lec.spring.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;

    // 기본적인 CRUD
    @Transactional
    public Post create(Post post) {
        return postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public Post readOne(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
    }

    @Transactional(readOnly = true)
    public List<Post> readAll() {
        return postRepository.findAll();
    }

    @Transactional
    public Post update(Post post) {
        Post postEntity = postRepository.findById(post.getPostId()).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
        // TODO
        return postEntity;
    }

    @Transactional
    public String delete(Long postId) {
        postRepository.deleteById(postId);
        return "ok";
    }

    // 추가 기능
    // TODO
}