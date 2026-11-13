
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

import AdminDashboard from "../pages/admin/AdminDashboard";

import StudentRecordings from "../pages/student/StudentRecordings";

import TeacherDashboard from "../pages/teacher/TeacherDashboard";

import ProtectedRoute from "./ProtectedRoute";
import PublicRoute from "./PublicRoute";

import AuthLayout from "../components/layout/AuthLayout";
import DashboardLayout from "../components/layout/DashboardLayout";
import TeacherRecordings from "../pages/teacher/TeacherRecordings";

import Login from "../pages/auth/Login";
import StudentDashboard from "../pages/student/StudentDashboard";

const AppRoutes = () => {
    return (
        <BrowserRouter>
            <Routes>

                {/* Public routes */}
                <Route element={<PublicRoute />}>
                    <Route element={<AuthLayout />}>
                        <Route path="/login" element={<Login />} />
                    </Route>
                </Route>

                {/* Student routes */}
                <Route element={<ProtectedRoute allowedRoles={["STUDENT"]} />}>
    <Route element={<DashboardLayout />}>
        <Route
            path="/student/dashboard"
            element={<StudentDashboard />}
        />
        <Route
            path="/student/recordings"
            element={<StudentRecordings />}
        />
    </Route>
</Route>

                {/* Teacher routes */}
                <Route element={<ProtectedRoute allowedRoles={["TEACHER"]} />}>
    <Route element={<DashboardLayout />}>
        <Route
            path="/teacher/dashboard"
            element={<TeacherDashboard />}
        />
        <Route
            path="/teacher/recordings"
            element={<TeacherRecordings />}
        />
    </Route>
</Route>

                {/* Admin routes */}
                <Route element={<ProtectedRoute allowedRoles={["ADMIN"]} />}>
                    <Route element={<DashboardLayout />}>
                        <Route
                            path="/admin/dashboard"
                            element={<AdminDashboard />}
                        />
                    </Route>
                </Route>

                <Route path="/unauthorized" element={<h1>Access Denied</h1>} />
                <Route path="/" element={<Navigate to="/login" replace />} />
                <Route path="*" element={<Navigate to="/" replace />} />

            </Routes>
        </BrowserRouter>
    );
};

export default AppRoutes;
