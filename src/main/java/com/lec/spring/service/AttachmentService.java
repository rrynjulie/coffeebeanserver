package com.lec.spring.service;

import com.lec.spring.domain.Attachment;
import com.lec.spring.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AttachmentService {
    private final AttachmentRepository attachmentRepository;

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
    public Attachment update(Attachment attachment) {
        Attachment attachmentEntity = attachmentRepository.findById(attachment.getAttachmentId()).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
        // TODO
        return attachmentEntity;
    }

    @Transactional
    public String delete(Long attachmentId) {
        attachmentRepository.deleteById(attachmentId);
        return "ok";
    }

    // 추가 기능
    // TODO
}