import { useSelector } from "react-redux";

const AdminDashboard = () => {
  const { email, role } = useSelector((state) => state.admin);

  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold text-blue-900 mb-4">Admin Dashboard</h1>

      <div className="bg-white shadow rounded-xl p-6 max-w-md">
        <p className="text-gray-700">
          <strong>Email:</strong> {email}
        </p>
        <p className="text-gray-700 mt-2">
          <strong>Role:</strong> {role}
        </p>
      </div>
    </div>
  );
};

export default AdminDashboard;
