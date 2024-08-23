package com.lec.spring.service;

import com.lec.spring.domain.Attachment;
import com.lec.spring.domain.Post;
import com.lec.spring.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final AttachmentService attachmentService;
    private final UserService userService;

    // 기본적인 CRUD
    @Transactional
    public Post create(Post post, Long userId, MultipartFile[] files) {
        post.setUser(userService.readOne(userId));
        post = postRepository.saveAndFlush(post);
        attachmentService.addFiles(files, post);
        return post;
    }

    @Transactional(readOnly = true)
    public Post readOne(Long postId) {
        Post post =  postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
        if (post != null){
            List<Attachment> fileList = attachmentService.findByPost(post);
            attachmentService.setImage(fileList);
            post.setFileList(fileList);
        }
        return post;
    }

    @Transactional(readOnly = true)
    public List<Post> readAll() {
        return postRepository.findAllByOrderByRegDateDesc();
    }

    @Transactional
    public int update(Post post, Long postId, MultipartFile[] files, Long[] delfile) {
//    public int update(Post post, Long postId, MultipartFile[] files) {
        int result = 0;

        Post p = postRepository.findByPostId(postId);
        if (p != null){
            post.setUser(p.getUser());
            post.setRegDate(p.getRegDate());
            post.setPostId(postId);
            p.setType(post.getType());
            p.setTitle(post.getTitle());
            p.setContent(post.getContent());
            p = postRepository.saveAndFlush(p);

//            Post savedPost = postRepository.save(post);
//            addFiles(files, savedPost);
            attachmentService.addFiles(files, p);

            if (delfile != null){
                for (Long fileId : delfile){
                    Attachment file = attachmentService.readOne(fileId);
                    if (file != null){
                        attachmentService.delFile(file);
                        attachmentService.delete(fileId);
                    }
                }
            }
            result = 1;
        }
        return result;
    }

    @Transactional
    public int delete(Long postId) {
        postRepository.deleteById(postId);
        return 1;
    }
}