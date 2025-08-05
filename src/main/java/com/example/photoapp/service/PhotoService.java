package com.example.photoapp.service;

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

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    public List<Photo> getAllPhotos() {
        return photoRepository.findAll();
    }

    public List<Photo> savePhotos(List<MultipartFile> files, String qrCode) throws IOException {
        List<Photo> savedPhotos = new ArrayList<>();
        Path uploadPath = Paths.get("uploads");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            Path path = uploadPath.resolve(fileName);
            Files.write(path, file.getBytes());

            Photo photo = new Photo();
            photo.setFileName(fileName);
            photo.setQrCode(qrCode);
            savedPhotos.add(photoRepository.save(photo));
        }
        return savedPhotos;
    }

    public List<Photo> getPhotosByQrCode(String qrCode) {
        return photoRepository.findByQrCode(qrCode);
    }
}
