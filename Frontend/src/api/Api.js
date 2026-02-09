import axios from "axios";

export const loginApi = (email, password, role) => {
  return axios.post(
    "http://localhost:8080/login",
    { email, password, role: role.toUpperCase() },
    { withCredentials: true } 
  );
};

export const getStudentByEmail = (email) => {
  return axios.get(
    `http://localhost:8080/api/v1/students?email=${email}`,
    { withCredentials: true }
  );
};

export const getAllCourses = () => {
  return axios.get("http://localhost:8080/api/v1/students/courses", {
    withCredentials: true,
  });
};

export const getEnrolledCourses = () => {
  return axios.get("http://localhost:8080/api/v1/students/courses/enrolled", {
    withCredentials: true,
  });
};


export const enrollCourse = (studentId , courseId) =>{
  return axios.post(
    "http://localhost:8080/api/v1/students/courses/enroll",
    {
      studentId,
      courseId,
    },
    { withCredentials: true }
  );
};

export const getCurrentUser = () => {
  return axios.get(
    "http://localhost:8080/api/v1/auth/me",
    { withCredentials: true }
  );
};

export const getTeacherByEmail = (email) => {
  return axios.get(
    `http://localhost:8080/api/v1/teacher?email=${email}`,
    { withCredentials: true }
  );
};

export const createCourse = (courseData) =>{
  return axios.post(
    "http://localhost:8080/api/v1/teacher/courses",
    courseData,
    {withCredentials:true}
  );
};

export const getTeacherCourses = (teacherId) =>{
  return axios.get(
    `http://localhost:8080/api/v1/teacher/courses/${teacherId}`,
    {withCredentials:true}
  )
}

export const deleteTeacherCourse = (courseId) =>{
  return axios.delete(
    `http://localhost:8080/api/v1/teacher/courses/${courseId}`,
    {withCredentials:true}
  )
}

const API = axios.create({
  baseURL: "http://localhost:8080",
  withCredentials: true,
});

export const createBaseUser = (data) =>
  API.post("/api/v1/admin/users", data);

export const createStudent = (data) =>
  API.post("/api/v1/admin/users/student", data);

export const createTeacher = (data) =>
  API.post("/api/v1/admin/users/teacher", data);



export const getStudentRecordings = ({ date , subject , period }) =>{
  const params = {};

  if(date) params.date = date;
   if (subject) params.subject = subject;
  if (period) params.period = period;

  return API.get("/api/v1/students/recordings", { params });
};

export const uploadClassRecordingApi = (formData , onProgress) => {
  return API.post("/api/v1/teacher/recordings/upload" , formData , {
    headers:{
      "Content-Type" : "multipart/form-data",
    },
    onUploadProgress : (progressEvent) =>{
      if(progressEvent.total){
         const percent = Math.round(
          (progressEvent.loaded * 100) / progressEvent.total
        );
        onProgress(percent);
      }
    }
  })
}

export const getTeacherRecordings = (filters = {}) => {
  return axios.get(
    "http://localhost:8080/api/v1/teacher/recordings",
    {
      params: {
        date: filters.date || null,
        subject: filters.subject || null,
        period: filters.period || null,
      },
      withCredentials: true,
    }
  );
};

export const deleteTeacherRecording = (id) => API.delete(`api/v1/teacher/recordings/${id}`);

export const logoutApi = () =>
  API.post("/logout",{},{withCredentials:true});