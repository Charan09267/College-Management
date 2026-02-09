import { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  FaUserShield,
  FaChalkboardTeacher,
  FaUserGraduate,
} from "react-icons/fa";
import { getStudentByEmail, getTeacherByEmail, loginApi } from "../api/Api";
import { loginSuccess, loginFailure } from "../features/auth/authSlice";
import { useNavigate } from "react-router-dom";
import { setStudent } from "../features/student/studentSlice";
import { setTeacher } from "../features/teacher/teacherSlice";
import { setAdmin } from "../features/admin/adminSlice";

const LoginPage = () => {
  const [role, setRole] = useState("student");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const dispatch = useDispatch();
  const navigate = useNavigate();

  const { isAuthenticated, role: userRole } = useSelector(
    (state) => state.auth,
  );

  const currentStudent = useSelector((state) => state.student?.currentStudent);
  const currentTeacher = useSelector((state) => state.teacher?.currentTeacher);
  const adminEmail = useSelector((state) => state.admin?.email);

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      // 1. Authenticate
      const res = await loginApi(email, password, role);
      const userEmail = res.data.email;
      const upperRole = role.toUpperCase();

      // 2. Fetch Data & Navigate IMMEDIATELY based on role
      if (upperRole === "STUDENT") {
        const studentRes = await getStudentByEmail(userEmail);
        dispatch(setStudent(studentRes.data));
        dispatch(
          loginSuccess({ email: userEmail, role: upperRole, token: "cookie" }),
        );
        navigate("/student");
      } else if (upperRole === "TEACHER") {
        const teacherRes = await getTeacherByEmail(userEmail);
        dispatch(setTeacher(teacherRes.data));
        dispatch(
          loginSuccess({ email: userEmail, role: upperRole, token: "cookie" }),
        );
        navigate("/teacher");
      } else if (upperRole === "ADMIN") {
        dispatch(setAdmin({ email: userEmail, role: upperRole }));
        dispatch(
          loginSuccess({ email: userEmail, role: upperRole, token: "cookie" }),
        );
        navigate("/admin");
      }
    } catch (loginErr) {
      console.error(
        "Login Error:",
        loginErr.response?.data || loginErr.message,
      );
      dispatch(loginFailure());
      alert("Login failed. Check console for details.");
    }
  };
  const roles = [
    { name: "admin", icon: <FaUserShield /> },
    { name: "teacher", icon: <FaChalkboardTeacher /> },
    { name: "student", icon: <FaUserGraduate /> },
  ];

  return (
    <div className="min-h-screen flex items-center justify-center bg-linear-to-br from-blue-100 to-blue-300">
      <div className="bg-white rounded-2xl shadow-2xl w-full max-w-lg p-8">
        <h2 className="text-3xl font-bold text-blue-900 text-center mb-8">
          University Portal Login
        </h2>

        <div className="flex justify-center gap-4 mb-8">
          {roles.map((r) => (
            <button
              key={r.name}
              onClick={() => setRole(r.name)}
              className={`px-6 py-3 rounded-xl flex items-center gap-2 ${
                role === r.name
                  ? "bg-blue-700 text-white"
                  : "bg-blue-100 text-blue-800"
              }`}
            >
              {r.icon}
              {r.name}
            </button>
          ))}
        </div>

        <form onSubmit={handleLogin} className="flex flex-col gap-4">
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="Email"
            required
            className="px-4 py-3 border rounded-lg"
          />
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="Password"
            required
            className="px-4 py-3 border rounded-lg"
          />
          <button
            type="submit"
            className="bg-blue-700 text-white py-3 rounded-lg"
          >
            Login
          </button>
        </form>
      </div>
    </div>
  );
};

export default LoginPage;
