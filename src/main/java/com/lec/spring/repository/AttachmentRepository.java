package com.lec.spring.repository;

import com.lec.spring.domain.Attachment;
import com.lec.spring.domain.Car;
import com.lec.spring.domain.Post;
import com.lec.spring.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findByPost(Post post);
    List<Attachment> findByProduct(Product product);
    List<Attachment> findByCar(Car car);
}