package com.example.photoapp.controller;

import com.example.photoapp.model.Photo;
import com.example.photoapp.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

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
    public List<Photo> uploadPhotos(
            @RequestParam("files") List<MultipartFile> files,
            @PathVariable String qrCode) throws IOException {
        return photoService.savePhotos(files, qrCode);
    }

    @GetMapping("/{qrCode}")
    public List<Photo> getPhotosByQrCode(@PathVariable String qrCode) {
        return photoService.getPhotosByQrCode(qrCode);
    }
}
