package com.CollegeHub.recording.service;


import com.CollegeHub.recording.dto.RecordingDetailsResponse;
import com.CollegeHub.recording.entity.ClassRecording;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecordingMapper {

    public RecordingDetailsResponse toResponse(ClassRecording recording) {

        return RecordingDetailsResponse.builder()
                .id(recording.getId())
                .title(recording.getTitle())
                .description(recording.getDescription())
                .subject(recording.getSubject())
                .classDate(recording.getClassDate())
                .periodNumber(recording.getPeriodNumber())
                .branch(recording.getBranch())
                .year(recording.getYear())
                .uploadedByUserId(recording.getUploadedByUserId())
                .originalFileName(recording.getOriginalFileName())
                .contentType(recording.getContentType())
                .fileSize(recording.getFileSize())
                .createdAt(recording.getCreatedAt())
                .build();
    }

    public List<RecordingDetailsResponse> toResponseList(
            List<ClassRecording> recordings
    ) {
        return recordings.stream()
                .map(this::toResponse)
                .toList();
    }
}

