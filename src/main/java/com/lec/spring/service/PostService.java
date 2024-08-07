package com.lec.spring.service;

import com.lec.spring.domain.Attachment;
import com.lec.spring.domain.Post;
import com.lec.spring.domain.User;
import com.lec.spring.repository.AttachmentRepository;
import com.lec.spring.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class PostService {

    @Value("${app.upload.path}")
    private String uploadDir;

    private final PostRepository postRepository;
    private final UserService userService;
    private final AttachmentRepository attachmentRepository;

    private void addFiles(Map<String, MultipartFile> files, Post post) {
        if (files == null) return;
        for (Map.Entry<String, MultipartFile> e : files.entrySet()) {
            System.out.println("Processing file: " + e.getKey());
//            if (!e.getKey().startsWith("upfile")) continue;     // name="upfile##" 인 경우에만 첨부파일 등록
            Attachment file = upload(e.getValue());   // 파일 물리적으로 저장
            if (file != null) {
                file.setPost(post);
                attachmentRepository.saveAndFlush(file);
            }
        }
    }

    private Attachment upload(MultipartFile multipartFile) {
        Attachment attachment = null;

        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) return null;

        String source = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String fileName = source;

        File file = new File(uploadDir, fileName);
        if (file.exists()) {

            int pos = fileName.lastIndexOf(".");
            if (pos > -1) {
                String name = fileName.substring(0, pos);
                String ext = fileName.substring(pos + 1);

                fileName = name + "_" + System.currentTimeMillis() + "." + ext;

            } else {
                fileName += "_" + System.currentTimeMillis();
            }
        }
        System.out.println("fileName: " + fileName);

        Path copyOfLocation = Paths.get(new File(uploadDir, fileName).getAbsolutePath());
        System.out.println(copyOfLocation);

        try {
            Files.copy(
                    multipartFile.getInputStream(),
                    copyOfLocation,
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        attachment = Attachment.builder()
                .filename(fileName)
                .source(source)
                .build();

        return attachment;
    }

    private void setImage(List<Attachment> fileList) {
        String realPath = new File(uploadDir).getAbsolutePath();

        for (Attachment attachment : fileList) {
            BufferedImage imgData = null;
            File f = new File(realPath, attachment.getFilename());

            try {
                imgData = ImageIO.read(f);
                if (imgData != null) attachment.setImage(true);

            } catch (IOException e) {
                System.out.println("파일이 존재하지 않습니다.");
            }
        }
    }

    private void delFile(Attachment file) {
        String saveDirectory = new File(uploadDir).getAbsolutePath();

        File f = new File(saveDirectory, file.getFilename());
        if (f.exists()) {
            if (f.delete()) {
                System.out.println("파일 삭제 성공");
            } else {
                System.out.println("파일 삭제 실패 ");
            }
        } else {
            System.out.println("파일이 존재하지 않습니다.");
        }
    }

    // 기본적인 CRUD
    @Transactional
    public int create(Post post, Long userId, Map<String, MultipartFile> files) {
        post.setUser(userService.readOne(userId));
        post = postRepository.saveAndFlush(post);
        addFiles(files, post);

        return 1;
    }

    @Transactional(readOnly = true)
    public Post readOne(Long postId) {
        Post post =  postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
        if (post != null){
            List<Attachment> fileList = attachmentRepository.findByPost(post);
            setImage(fileList);
            post.setFileList(fileList);
        }
        return post;
    }

    @Transactional(readOnly = true)
    public List<Post> readAll() {
        return postRepository.findAll();
    }

    @Transactional
    public int update(Post post, Long postId, Map<String, MultipartFile> files, Long[] delfile) {
//    public int update(Post post, Long postId, Map<String, MultipartFile> files) {
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
            addFiles(files, p);

            if (delfile != null){
                for (Long fileId : delfile){
                    Attachment file = attachmentRepository.findById(fileId).orElse(null);
                    if (file != null){
                        delFile(file);
                        attachmentRepository.delete(file);
                    }
                }
            }
            result = 1;
        }
        return result;
    }

    @Transactional
    public String delete(Long postId) {
        postRepository.deleteById(postId);
        return "ok";
    }

    // 추가 기능
    // TODO
}