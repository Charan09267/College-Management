import DefaultPage from "./Pages/DefaultPage";
import { Route, Routes } from "react-router-dom";
import LoginPage from "./Pages/LoginPage";
import Layout from "./Components/StudentPage/Layout";
import Dashboard from "./Pages/StudentPage/DashBoard";

import MyCourses from "./Pages/StudentPage/MyCourses";
import AllCourses from "./Pages/StudentPage/AllCourses";
import ProtectedRoute from "./Components/StudentPage/ProtectedRoute";

import TeacherLayout from "./Components/teacherPage/TeacherLayout";
import TeacherDashboard from "./Pages/TeacherPage/TeacherDashboard";
import CreateCourse from "./Pages/TeacherPage/CreateCourse";
import TeacherCourses from "./Pages/TeacherPage/TeacherCourses";
import AdminLayout from "./Pages/AdminPage/AdminLayout";
import AdminDashboard from "./Components/AdminPage/AdminDashboard";
import UserManagement from "./Components/AdminPage/UserManagement";
import ClassRecordings from "./Pages/StudentPage/ClassRecordings";
import UploadClassRecording from "./Pages/TeacherPage/UploadClassRecording";

import TeacherRecordings from "./Pages/TeacherPage/TeacherRecordings";

// import AdminDashboard from "./Pages/AdminPage/AdminDashboard";
// import AdminCourse from "./Pages/AdminPage/AdminCourse";

const App = () => {
  return (
    <>
      <Routes>
        <Route path="/" element={<DefaultPage />} />
        <Route path="/login" element={<LoginPage />} />

        {/* 🔒 STUDENT PROTECTED ROUTES */}
        <Route element={<ProtectedRoute allowedRole="STUDENT" />}>
          <Route path="/student" element={<Layout />}>
            <Route index element={<Dashboard />} />
            <Route path="dashboard" element={<Dashboard />} />
            <Route path="recordings" element={<ClassRecordings />} />
            <Route path="courses/my" element={<MyCourses />} />
            <Route path="courses/all" element={<AllCourses />} />
          </Route>
        </Route>

        {/* 🔒 TEACHER PROTECTED ROUTES */}
        <Route element={<ProtectedRoute allowedRole="TEACHER" />}>
          <Route path="/teacher" element={<TeacherLayout />}>
            <Route index element={<TeacherDashboard />} />
            <Route path="dashboard" element={<TeacherDashboard />} />
            <Route
              path="recordings/upload"
              element={<UploadClassRecording />}
            />
            <Route path="recordings" element={<TeacherRecordings />} />
            <Route path="courses/create" element={<CreateCourse />} />
            <Route path="courses/my" element={<TeacherCourses />} />
          </Route>
        </Route>

        {/* 🔒 Admin PROTECTED ROUTES */}
        <Route element={<ProtectedRoute allowedRole="ADMIN" />}>
          <Route path="/admin" element={<AdminLayout />}>
            <Route index element={<AdminDashboard />} />
            <Route path="users" element={<UserManagement />} />
          </Route>
        </Route>
      </Routes>
    </>
  );
};

export default App;
