import { useState } from "react";
import { NavLink, Outlet, useNavigate } from "react-router-dom";
import { FaHome, FaBook, FaChevronDown, FaSignOutAlt } from "react-icons/fa";
import { logoutApi } from "../../api/Api";

const Layout = () => {
  const [coursesOpen, setCoursesOpen] = useState(false);
  const navigate = useNavigate();

  const activeClass = "bg-blue-900 text-white";
  const normalClass = "text-white hover:bg-blue-700";

  const handleLogout = async () => {
    try {
      await logoutApi();
      localStorage.clear();
      navigate("/login");
    } catch (err) {
      console.error("Logout failed", err);
    }
  };

  return (
    <div className="flex min-h-screen">
      {/* Sidebar */}
      <div className="w-64 bg-blue-800 shadow-lg p-4 flex flex-col">
        <h2 className="text-2xl font-bold text-white mb-6 text-center">
          Portal
        </h2>

        <nav className="flex flex-col gap-2 flex-1">
          {/* Dashboard */}
          <NavLink
            to="dashboard"
            className={({ isActive }) =>
              `flex items-center gap-2 px-4 py-2 rounded transition ${
                isActive ? activeClass : normalClass
              }`
            }
          >
            <FaHome /> Dashboard
          </NavLink>

          {/* Courses */}
          <button
            onClick={() => setCoursesOpen(!coursesOpen)}
            className={`flex items-center justify-between px-4 py-2 rounded transition ${
              coursesOpen ? activeClass : normalClass
            }`}
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
            <div className="flex flex-col ml-6 gap-1">
              <NavLink
                to="courses/my"
                className={({ isActive }) =>
                  `px-3 py-1 rounded transition ${
                    isActive
                      ? "bg-blue-900 text-white"
                      : "text-blue-100 hover:bg-blue-700"
                  }`
                }
              >
                My Courses
              </NavLink>

              <NavLink
                to="courses/all"
                className={({ isActive }) =>
                  `px-3 py-1 rounded transition ${
                    isActive
                      ? "bg-blue-900 text-white"
                      : "text-blue-100 hover:bg-blue-700"
                  }`
                }
              >
                All Courses
              </NavLink>
            </div>
          )}

          <NavLink
            to="recordings"
            className={({ isActive }) =>
              `flex items-center gap-2 px-4 py-2 rounded transition ${
                isActive ? activeClass : normalClass
              }`
            }
          >
            🎥 Class Recordings
          </NavLink>
        </nav>

        {/* Logout Button */}
        <button
          onClick={handleLogout}
          className="mt-4 flex items-center gap-2 px-4 py-2 rounded text-white bg-red-600 hover:bg-red-700 transition"
        >
          <FaSignOutAlt /> Logout
        </button>
      </div>

      {/* Main Content */}
      <div className="flex-1 p-6 bg-gray-100">
        <Outlet />
      </div>
    </div>
  );
};

export default Layout;
