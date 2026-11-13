
import api from "../../services/api";

// Student APIs
export const getStudentRecordings = async () => {
    const response = await api.get("/api/v1/student/recordings");
    return response.data;
};

export const streamStudentRecording = (recordingId) => {
    return `/api/v1/student/recordings/${recordingId}/stream`;
};

// Teacher APIs
export const getTeacherRecordings = async () => {
    const response = await api.get("/api/v1/teacher/recordings");
    return response.data;
};

export const uploadRecording = async (formData) => {
    const response = await api.post(
        "/api/v1/teacher/recordings",
        formData
    );
    return response.data;
};

export const streamTeacherRecording = (recordingId) => {
    return `/api/v1/teacher/recordings/${recordingId}/stream`;
};

export const deleteRecording = async (recordingId) => {
    await api.delete(`/api/v1/teacher/recordings/${recordingId}`);
};
