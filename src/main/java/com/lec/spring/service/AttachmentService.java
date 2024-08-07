package com.lec.spring.service;

import com.lec.spring.domain.Attachment;
import com.lec.spring.domain.Post;
import com.lec.spring.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AttachmentService {
    private AttachmentRepository attachmentRepository;

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

    @Autowired
    public void setAttachmentRepository(AttachmentRepository attachmentRepository){
        this.attachmentRepository = attachmentRepository;
    }

    public Attachment findById(Long id) {
        return attachmentRepository.findById(id).orElse(null);
    }

    // 추가 기능
    // TODO
}