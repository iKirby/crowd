package com.everyone.crowd.controller;

import com.everyone.crowd.entity.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FileController {
    @Value("${com.everyone.crowd.upload.path}")
    private String uploadPath;

    @GetMapping("/uploads/{fileName}")
    public ResponseEntity<Resource> serveImageFile(@PathVariable String fileName) {
        Resource file = new FileSystemResource(uploadPath + fileName);
        if (file.exists() && file.isReadable()) {
            ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
            if (fileName.endsWith(".png")) {
                builder.contentType(MediaType.IMAGE_PNG);
            } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")){
                builder.contentType(MediaType.IMAGE_JPEG);
            } else {
                builder.header(
                        "Content-Disposition",
                        "attachment; filename=" + fileName.substring(fileName.indexOf("_") + 1)
                );
            }
            return builder.body(file);
        }
        throw new NotFoundException("找不到请求的文件");
    }
}
