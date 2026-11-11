package com.CollegeHub.recording.service;

import com.CollegeHub.recording.dto.RecordingDetailsResponse;
import com.CollegeHub.recording.entity.ClassRecording;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Defines the operations for managing class recordings, including uploading,
 * retrieving, streaming, and deleting recordings.
 */
public interface ClassRecordingService {

    /**
     * Uploads a class recording along with its metadata.
     *
     * @param title          title of the recording
     * @param description    description of the class recording
     * @param subject        subject covered in the recording
     * @param classDate      date on which the class was conducted
     * @param periodNumber   period during which the class was conducted
     * @param branch         branch for which the recording is intended
     * @param year           academic year of the intended students
     * @param teacherUserId  ID of the teacher uploading the recording
     * @param file           video file to be uploaded
     * @return recording details after successful upload
     * @throws IOException if an error occurs while uploading the video file
     */
    RecordingDetailsResponse uploadRecording(
            String title,
            String description,
            String subject,
            LocalDate classDate,
            Integer periodNumber,
            String branch,
            Integer year,
            Long teacherUserId,
            MultipartFile file
    ) throws IOException;

    /**
     * Retrieves recordings available to a student based on their academic details.
     *
     * @param studentUserId ID of the student requesting the recordings
     * @return list of recordings accessible to the student
     */
    List<RecordingDetailsResponse> getRecordingsForStudent(Long studentUserId);

    /**
     * Retrieves all recordings uploaded by a specific teacher.
     *
     * @param teacherUserId ID of the teacher requesting their recordings
     * @return list of recordings uploaded by the teacher
     */
    List<RecordingDetailsResponse> getRecordingsForTeacher(Long teacherUserId);

    /**
     * Streams a recording after validating the student's access to it.
     *
     * @param recordingId  ID of the recording to be streamed
     * @param studentUserId ID of the student requesting access
     * @return resource containing the recording video
     */
    Resource streamRecording(String recordingId, Long studentUserId);

    /**
     * Streams a recording uploaded by the specified teacher after validating ownership.
     *
     * @param recordingId  ID of the recording to be streamed
     * @param teacherUserId ID of the teacher requesting access
     * @return resource containing the recording video
     */
    Resource streamTeacherRecording(String recordingId, Long teacherUserId);

    /**
     * Deletes a recording after verifying that it belongs to the specified teacher.
     *
     * @param recordingId  ID of the recording to be deleted
     * @param teacherUserId ID of the teacher requesting deletion
     * @throws IOException if an error occurs while deleting the video file
     */
    void deleteRecording(String recordingId, Long teacherUserId) throws IOException;
}