package com.example.photoapp.DTOs;

import com.example.photoapp.model.Photo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTOs {
    private String message;
    private List<Photo> data;
}
