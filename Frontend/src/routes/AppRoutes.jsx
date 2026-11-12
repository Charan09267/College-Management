
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import ProtectedRoute from "./ProtectedRoute";
import Login from "../pages/auth/Login";

const AppRoutes = () => {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/login" element={<Login />} />

                <Route element={<ProtectedRoute allowedRoles={["STUDENT"]} />}>
                    <Route
                        path="/student/dashboard"
                        element={<h1>Student Dashboard</h1>}
                    />
                </Route>

                <Route element={<ProtectedRoute allowedRoles={["TEACHER"]} />}>
                    <Route
                        path="/teacher/dashboard"
                        element={<h1>Teacher Dashboard</h1>}
                    />
                </Route>

                <Route element={<ProtectedRoute allowedRoles={["ADMIN"]} />}>
                    <Route
                        path="/admin/dashboard"
                        element={<h1>Admin Dashboard</h1>}
                    />
                </Route>

                <Route
                    path="/unauthorized"
                    element={<h1>Access Denied</h1>}
                />

                <Route path="/" element={<Navigate to="/login" replace />} />
                <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
        </BrowserRouter>
    );
};

export default AppRoutes;
