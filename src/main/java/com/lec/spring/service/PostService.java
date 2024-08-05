package com.lec.spring.service;

import com.lec.spring.domain.Attachment;
import com.lec.spring.domain.Post;
import com.lec.spring.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final AttachmentService attachmentService;

    // 기본적인 CRUD
    @Transactional
    public Post create(Post post, Long userId) {
        post.setUser(userService.readOne(userId));
        System.out.println(userId + "---------------------------------------------------------------------------");
        return postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public Post readOne(Long postId) {
        Post post =  postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
        return post;
    }

    @Transactional(readOnly = true)
    public List<Post> readAll() {
        return postRepository.findAll();
    }

    @Transactional
    public Post update(Post post) {
        Post postEntity = postRepository.findById(post.getPostId()).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
        postEntity.setType(post.getType());
        postEntity.setTitle(post.getTitle());
        postEntity.setContent(post.getContent());
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