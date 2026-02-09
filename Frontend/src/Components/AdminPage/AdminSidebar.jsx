import { NavLink, useNavigate } from "react-router-dom";
import { FaUsers, FaTachometerAlt, FaSignOutAlt } from "react-icons/fa";
import { logoutApi } from "../../api/Api";

const AdminSidebar = () => {
  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      await logoutApi();
      navigate("/login");
    } catch (err) {
      console.error("Logout failed", err);
    }
  };

  return (
    <div className="w-64 min-h-screen bg-blue-900 text-white p-5 flex flex-col">
      <h1 className="text-2xl font-bold mb-8">Admin Panel</h1>

      <nav className="flex flex-col gap-4 flex-1">
        <NavLink
          to="/admin"
          end
          className={({ isActive }) =>
            isActive ? "text-yellow-300" : "text-white"
          }
        >
          <FaTachometerAlt className="inline mr-2" />
          Dashboard
        </NavLink>

        <NavLink
          to="/admin/users"
          className={({ isActive }) =>
            isActive ? "text-yellow-300" : "text-white"
          }
        >
          <FaUsers className="inline mr-2" />
          User Management
        </NavLink>
      </nav>

      {/* Logout Button */}
      <button
        onClick={handleLogout}
        className="mt-auto flex items-center gap-2 text-red-300 hover:text-red-500 transition"
      >
        <FaSignOutAlt />
        Logout
      </button>
    </div>
  );
};

export default AdminSidebar;
