import { Navigate, Outlet } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { useEffect, useRef, useState } from "react";
import {
  getCurrentUser,
  getStudentByEmail,
  getTeacherByEmail,
} from "../../api/Api";
import { loginSuccess, logout } from "../../features/auth/authSlice";
import { setStudent } from "../../features/student/studentSlice";
import { setTeacher } from "../../features/teacher/teacherSlice";
import { setAdmin } from "../../features/admin/adminSlice";

const ProtectedRoute = ({ allowedRole }) => {
  const dispatch = useDispatch();
  const { isAuthenticated, role } = useSelector((state) => state.auth);
  const [checkingAuth, setCheckingAuth] = useState(true);

  const sessionChecked = useRef(false);

  useEffect(() => {
    const restoreSession = async () => {
      if (sessionChecked.current) return;
      sessionChecked.current = true;

      try {
        const res = await getCurrentUser();

        if (!res?.data) throw new Error("No session");

        const email = res.data.username;
        const userRole = res.data.authorities[0].authority.replace("ROLE_", "");

        // Load role-specific data
        if (userRole === "STUDENT") {
          const s = await getStudentByEmail(email);
          dispatch(setStudent(s.data));
        } else if (userRole === "TEACHER") {
          const t = await getTeacherByEmail(email);
          dispatch(setTeacher(t.data));
        } else if (userRole === "ADMIN") {
          dispatch(setAdmin({ email, role: "ADMIN" }));
        }

        dispatch(loginSuccess({ email, role: userRole, token: "cookie" }));
      } catch (err) {
        console.log("Session expired or not logged in");
        dispatch(logout());
      } finally {
        setCheckingAuth(false);
      }
    };

    if (!isAuthenticated) {
      restoreSession();
    } else {
      setCheckingAuth(false);
    }
  }, [dispatch, isAuthenticated]);

  if (checkingAuth) return <div>Loading...</div>;

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (allowedRole && role !== allowedRole) {
    return <Navigate to="/login" replace />;
  }

  return <Outlet />;
};

export default ProtectedRoute;
