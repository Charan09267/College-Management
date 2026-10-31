package com.CollegeHub.Services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface VideoStorageService {

    // Store an uploaded video and return its storage reference.
    String storeVideo(MultipartFile file) throws IOException;

    // Retrieve a video using its storage reference.
    Resource loadVideo(String videoPath);

    // Delete a video using its storage reference.
    void deleteVideo(String videoPath) throws IOException;
}
