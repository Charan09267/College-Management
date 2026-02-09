import { Outlet } from "react-router-dom";
import AdminSidebar from "../../Components/AdminPage/AdminSidebar";

const AdminLayout = () => {
  return (
    <div className="flex">
      <AdminSidebar />
      <div className="flex-1 bg-gray-100 min-h-screen">
        <Outlet />
      </div>
    </div>
  );
};

export default AdminLayout;
