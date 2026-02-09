import { useState } from "react";
import { NavLink, Outlet, useNavigate } from "react-router-dom";
import {
  FaHome,
  FaBook,
  FaChevronDown,
  FaPlusCircle,
  FaVideo,
  FaList,
  FaSignOutAlt,
} from "react-icons/fa";
import { logoutApi } from "../../api/Api";

const TeacherLayout = () => {
  const [coursesOpen, setCoursesOpen] = useState(false);
  const [recordingsOpen, setRecordingsOpen] = useState(false);
  const navigate = useNavigate();

  const activeClass = "bg-white text-blue-700 font-semibold";
  const normalClass = "text-white hover:bg-blue-600";

  const handleLogout = async () => {
    const confirmLogout = window.confirm("Are you sure you want to logout?");
    if (!confirmLogout) return;

    try {
      await logoutApi();
      navigate("/login", { replace: true });
    } catch (err) {
      console.error("Logout failed", err);
      alert("Logout failed. Try again.");
    }
  };

  return (
    <div className="flex min-h-screen">
      {/* Sidebar */}
      <div className="w-64 bg-blue-700 text-white p-4 shadow-lg flex flex-col">
        <h2 className="text-2xl font-bold text-center mb-8">Teacher Panel</h2>

        <nav className="flex flex-col gap-2 flex-1">
          {/* Dashboard */}
          <NavLink
            to="dashboard"
            end
            className={({ isActive }) =>
              `flex items-center gap-2 px-4 py-2 rounded ${
                isActive ? activeClass : normalClass
              }`
            }
          >
            <FaHome /> Dashboard
          </NavLink>

          {/* Courses */}
          <button
            onClick={() => setCoursesOpen(!coursesOpen)}
            className={`flex items-center justify-between px-4 py-2 rounded ${normalClass}`}
          >
            <div className="flex items-center gap-2">
              <FaBook /> Courses
            </div>
            <FaChevronDown
              className={`transition-transform ${
                coursesOpen ? "rotate-180" : ""
              }`}
            />
          </button>

          {coursesOpen && (
            <div className="ml-6 flex flex-col gap-1">
              <NavLink
                to="courses/create"
                className={({ isActive }) =>
                  `flex items-center gap-2 px-3 py-1 rounded ${
                    isActive ? activeClass : normalClass
                  }`
                }
              >
                <FaPlusCircle /> Create Course
              </NavLink>

              <NavLink
                to="courses/my"
                className={({ isActive }) =>
                  `px-3 py-1 rounded ${isActive ? activeClass : normalClass}`
                }
              >
                My Courses
              </NavLink>
            </div>
          )}

          {/* Class Recordings */}
          <button
            onClick={() => setRecordingsOpen(!recordingsOpen)}
            className={`flex items-center justify-between px-4 py-2 rounded ${normalClass}`}
          >
            <div className="flex items-center gap-2">
              <FaVideo /> Class Recordings
            </div>
            <FaChevronDown
              className={`transition-transform ${
                recordingsOpen ? "rotate-180" : ""
              }`}
            />
          </button>

          {recordingsOpen && (
            <div className="ml-6 flex flex-col gap-1">
              <NavLink
                to="recordings"
                end
                className={({ isActive }) =>
                  `flex items-center gap-2 px-3 py-1 rounded ${
                    isActive ? activeClass : normalClass
                  }`
                }
              >
                <FaList /> My Recordings
              </NavLink>

              <NavLink
                to="recordings/upload"
                className={({ isActive }) =>
                  `flex items-center gap-2 px-3 py-1 rounded ${
                    isActive ? activeClass : normalClass
                  }`
                }
              >
                <FaPlusCircle /> Upload Recording
              </NavLink>
            </div>
          )}
        </nav>

        {/* Logout */}
        <button
          onClick={handleLogout}
          className="mt-6 flex items-center gap-2 px-4 py-2 rounded bg-red-600 hover:bg-red-700 text-white"
        >
          <FaSignOutAlt /> Logout
        </button>
      </div>

      {/* Main Content */}
      <div className="flex-1 bg-blue-50 p-6">
        <Outlet />
      </div>
    </div>
  );
};

export default TeacherLayout;
