package com.example.photoapp.service;

import com.example.photoapp.DTOs.PhotoReqDto;
import com.example.photoapp.DTOs.ResponseDTOs;
import com.example.photoapp.model.Photo;
import com.example.photoapp.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    public List<Photo> getAllPhotos() {
        return photoRepository.findAll();
    }

    public ResponseDTOs savePhotos(List<MultipartFile> files, String qrCode) throws Exception {
        List<Photo> savedPhotos = new ArrayList<>();
        Path uploadPath = Paths.get("uploads");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            if (fileName == null || fileName.isBlank()) {
                continue;
            }

            String uniqueFileName = UUID.randomUUID() + "_" + fileName;
            Path filePath = uploadPath.resolve(uniqueFileName);
            Files.write(filePath, file.getBytes());

            Photo photo = new Photo();
            photo.setFileName(uniqueFileName);
            photo.setQrCode(qrCode); // ✅ save qrCode to DB

            Photo saved = photoRepository.save(photo);

            String downloadUrl = "http://localhost:8080/photos/download/" + saved.getId();
            String qrFileName = "qr-" + saved.getId() + ".png";
            String qrFilePath = "uploads/qrcodes/" + qrFileName;

            Files.createDirectories(Paths.get("uploads/qrcodes/"));
            GenerateQrService.generateQRCodeImage(downloadUrl, qrFilePath);

            saved.setQrCode(qrFileName);
            photoRepository.save(saved);

            savedPhotos.add(saved);
        }

        return new ResponseDTOs("Photos saved successfully!", savedPhotos);
    }


    public List<Photo> getPhotosByQrCode(String qrCode) {
        return photoRepository.findByQrCode(qrCode);
    }

    public void getPhotoById(Long photoId) {
        Photo photoFound = photoRepository.findById(photoId).orElseThrow(() -> new NoSuchElementException("No photo found"));


    }
}
