package com.example.photoapp.controller;

import com.example.photoapp.DTOs.PhotoReqDto;
import com.example.photoapp.DTOs.ResponseDTOs;
import com.example.photoapp.model.Photo;
import com.example.photoapp.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/photos")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @GetMapping
    public List<Photo> getAllPhotos() {
        return photoService.getAllPhotos();
    }

    @PostMapping(value = "/{qrCode}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDTOs uploadPhotos(
            @RequestParam("files") List<MultipartFile> files,
            @PathVariable String qrCode) throws Exception {
        return photoService.savePhotos(files, qrCode);
    }


    @GetMapping("/{qrCode}")
    public List<Photo> getPhotosByQrCode(@PathVariable String qrCode) {
        return photoService.getPhotosByQrCode(qrCode);
    }

    @GetMapping("/qr/{photoId}")
    public ResponseEntity<Resource> getQRCodeImage(@PathVariable Long photoId) throws IOException {
        String fileName = "qr-" + photoId + ".png";
        Path path = Paths.get("uploads/qrcodes", fileName);

        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }

        Resource file = new UrlResource(path.toUri());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(file);
    }


}
