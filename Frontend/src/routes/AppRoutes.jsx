
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import ProtectedRoute from "./ProtectedRoute";
import Login from "../pages/auth/Login";

const AppRoutes = () => {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/login" element={<Login />} />

                <Route element={<ProtectedRoute />}>
                    <Route
                        path="/dashboard"
                        element={<h1>Dashboard</h1>}
                    />
                </Route>

                <Route path="*" element={<Navigate to="/login" replace />} />
            </Routes>
        </BrowserRouter>
    );
};

export default AppRoutes;
