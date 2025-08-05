package com.example.photoapp.repository;

import com.example.photoapp.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByQrCode(String qrCode);
}
