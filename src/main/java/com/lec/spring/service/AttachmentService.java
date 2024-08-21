package com.lec.spring.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.lec.spring.domain.*;
import com.lec.spring.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AttachmentService {
    private AttachmentRepository attachmentRepository;
    @Value("${app.upload.path}")
    private String uploadDir;
    @Autowired
    private AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;
    @Value("${cloud.aws.s3.endpointUrl}")
    private String endpointUrl;

    @Autowired
    public AttachmentService(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    // 기본적인 CRUD
    @Transactional
    public Attachment create(Attachment attachment) {
        return attachmentRepository.save(attachment);
    }

    @Transactional(readOnly = true)
    public Attachment readOne(Long attachmentId) {
        return attachmentRepository.findById(attachmentId).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
    }

    @Transactional(readOnly = true)
    public List<Attachment> readAll() {
        return attachmentRepository.findAll();
    }

    @Transactional
    public String delete(Long attachmentId) {
        attachmentRepository.deleteById(attachmentId);
        return "ok";
    }

    @Transactional(readOnly = true)
    public List<Attachment> findByUser(User user) {
        return attachmentRepository.findByUser(user);
    }

    public List<Attachment> findByPost(Post post) {
        return attachmentRepository.findByPost(post);
    }

    // 추가 기능
    public <T> void addFiles(MultipartFile[] files, T entity) {
        if (files == null) return;
        for (MultipartFile multipartFile : files) {
//            if (!e.getKey().startsWith("upfile")) continue;     // name="upfile##" 인 경우에만 첨부파일 등록
            Attachment file = upload(multipartFile);   // 파일 물리적으로 저장
            if (file != null) {
                if(entity instanceof Car) {
                    file.setCar((Car) entity);
                } else if(entity instanceof Post) {
                    file.setPost((Post) entity);
                } else if(entity instanceof Product) {
                    file.setProduct((Product) entity);
                } else if(entity instanceof User) {
                    file.setUser((User) entity);
                } else {
                    throw new IllegalArgumentException("Unsupported entity type");
                }
                attachmentRepository.saveAndFlush(file);
            }
        }
    }

    public Attachment upload(MultipartFile multipartFile) {
        // 파일 원본 이름 가져오기
        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) return null;

        // 파일 이름과 소스 URL 생성
        String fileName = StringUtils.cleanPath(originalFilename);
        String source = endpointUrl + "/" + fileName;

        // S3에 업로드
        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucketName, fileName, inputStream, null));
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }

        // Attachment 객체 생성 및 반환
        return Attachment.builder()
                .filename(fileName)
                .source(source)
                .build();
    }

    public void setImage(List<Attachment> fileList) {
        String realPath = new File(uploadDir).getAbsolutePath();

        for (Attachment attachment : fileList) {
            BufferedImage imgData = null;
            File f = new File(realPath, attachment.getFilename());

            try {
                imgData = ImageIO.read(f);
            } catch (IOException e) {
                System.out.println("파일이 존재하지 않습니다.");
            }
        }
    }

    public void delFile(Attachment file) {
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

    public void addFile(MultipartFile file, User user) {
        if (file == null) return;

        Attachment attachment = upload(file);
        if (attachment != null) {
            attachment.setUser(user);
            attachmentRepository.saveAndFlush(attachment);
        }
    }
}