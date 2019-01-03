package com.everyone.crowd.controller;

import com.everyone.crowd.entity.exception.InternalErrorException;
import com.everyone.crowd.entity.exception.NotFoundException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayOutputStream;

@Controller
public class ResController {
    @Value("${com.everyone.crowd.upload.path}")
    private String uploadPath;

    @GetMapping("/uploads/{fileName}")
    public ResponseEntity<Resource> serveImageFile(@PathVariable String fileName) {
        Resource file = new FileSystemResource(uploadPath + fileName);
        if (file.exists() && file.isReadable()) {
            ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
            if (fileName.endsWith(".png")) {
                builder.contentType(MediaType.IMAGE_PNG);
            } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
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

    @GetMapping("/get/qr")
    public ResponseEntity<Resource> generateQRCode(@RequestParam("content") String content,
                                                   @RequestParam(value = "h", defaultValue = "200") int height,
                                                   @RequestParam(value = "w", defaultValue = "200") int width) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            Resource img = new ByteArrayResource(outputStream.toByteArray());

            ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
            builder.contentType(MediaType.IMAGE_PNG);
            return builder.body(img);
        } catch (Exception e) {
            throw new InternalErrorException("生成二维码时出现错误");
        }
    }
}
