package com.lec.spring.controller;

import com.lec.spring.domain.Attachment;
import com.lec.spring.service.AttachmentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
@RestController
public class AttachmentController {
    @Value("${app.upload.path}")
    private String uploadDir;
    private AttachmentService attachmentService;
    @RequestMapping("/download")
    public ResponseEntity<?> download(Long id){
        if(id == null) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); // 400

        Attachment file = attachmentService.readOne(id);
        if(file == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // 404

        String sourceName = file.getSource();
        String fileName = file.getFilename();
        String path = new File(uploadDir, fileName).getAbsolutePath();
        try {
            String mimeType = Files.probeContentType(Paths.get(path));
            if(mimeType == null){
                mimeType = "application/octet-stream";
            }
            Path filePath = Paths.get(path);
            Resource resource = new InputStreamResource(Files.newInputStream(filePath));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename(URLEncoder.encode(sourceName, "utf-8")).build());
            headers.setCacheControl("no-cache");
            headers.setContentType(MediaType.parseMediaType(mimeType));
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(null, null, HttpStatus.CONFLICT);  // 409
        }
    }
}