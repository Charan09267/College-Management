package com.CollegeHub.Repository;

import com.CollegeHub.Entity.ClassRecording;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ClassRecordingRepository
        extends MongoRepository<ClassRecording, String> {

    List<ClassRecording> findByUploadedByUserIdAndIsActiveTrue(
            Long uploadedByUserId
    );
}