package com.CollegeHub.storage;



import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.exception.SdkException;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3VideoStorageService implements VideoStorageService {

    private final S3Client s3Client;

    @Value("${aws.bucket.name}")
    private String bucketName;

    private static final String FOLDER = "class-recordings/";

    // Upload video to S3
    @Override
    public String storeVideo(MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Video file cannot be empty");
        }

        String originalFileName = file.getOriginalFilename();

        String extension = getFileExtension(originalFileName);

        // Generate a unique object key
        String key = FOLDER + UUID.randomUUID() + extension;

        String contentType = file.getContentType();

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(contentType)
                .build();

        try (InputStream inputStream = file.getInputStream()) {

            s3Client.putObject(
                    request,
                    RequestBody.fromInputStream(
                            inputStream,
                            file.getSize()
                    )
            );

            return key;

        } catch (SdkException exception) {

            throw new IOException(
                    "Failed to upload video to S3",
                    exception
            );
        }
    }

    // Retrieve video from S3
    @Override
    public Resource loadVideo(String videoPath) {

        if (videoPath == null || !videoPath.startsWith(FOLDER)) {
            throw new IllegalArgumentException("Invalid video reference");
        }

        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(videoPath)
                .build();

        try {

            ResponseInputStream<GetObjectResponse> inputStream =
                    s3Client.getObject(request);

            long contentLength = inputStream.response().contentLength();

            return new InputStreamResource(inputStream) {

                @Override
                public String getDescription() {
                    return "S3 video resource: " + videoPath;
                }

                @Override
                public long contentLength() {
                    return contentLength;
                }
            };

        } catch (S3Exception exception) {

            throw new IllegalStateException(
                    "Unable to retrieve video from S3",
                    exception
            );
        }
    }


    // Delete video from S3
    @Override
    public void deleteVideo(String videoPath) {

        if (videoPath == null || !videoPath.startsWith(FOLDER)) {
            throw new IllegalArgumentException("Invalid video reference");
        }

        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(videoPath)
                .build();

        try {

            s3Client.deleteObject(request);

        } catch (S3Exception exception) {

            throw new IllegalStateException(
                    "Unable to delete video from S3",
                    exception
            );
        }
    }

    // Extract only a safe file extension
    private String getFileExtension(String fileName) {

        if (fileName == null) {
            return "";
        }

        int dotIndex = fileName.lastIndexOf(".");

        if (dotIndex < 0) {
            return "";
        }

        String extension = fileName.substring(dotIndex);

        return extension.matches("\\.[a-zA-Z0-9]{1,10}")
                ? extension
                : "";
    }
}
