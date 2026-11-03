package com.CollegeHub.recording.repository;

import java.util.List;
import java.util.Optional;

import com.CollegeHub.recording.entity.ClassRecording;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ClassRecordingRepository
        extends MongoRepository<ClassRecording, String> {

    List<ClassRecording> findByUploadedByUserIdAndIsActiveTrue(
            Long uploadedByUserId
    );

    List<ClassRecording> findByYearAndIsActiveTrue(Integer year);

    Optional<ClassRecording> findByIdAndIsActiveTrue(String recordingId);
}