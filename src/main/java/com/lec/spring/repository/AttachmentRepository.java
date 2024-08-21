package com.lec.spring.repository;

import com.lec.spring.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findByPost(Post post);
    List<Attachment> findByProduct(Product product);
    List<Attachment> findByCar(Car car);
    List<Attachment> findByUser(User user);
}