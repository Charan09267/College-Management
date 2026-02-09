import { useSelector } from "react-redux";

const TeacherDashboard = () => {
  const teacherProfile = useSelector((state) => state.teacher.profile);

  if (!teacherProfile) {
    return (
      <div className="bg-white p-6 rounded-xl shadow">
        <h1 className="text-2xl font-bold text-blue-800 mb-2">
          Teacher Dashboard
        </h1>
        <p className="text-gray-600">Loading profile...</p>
      </div>
    );
  }

  const avatarLetter = teacherProfile.email[0].toUpperCase();

  return (
    <div className="bg-white p-6 rounded-xl shadow">
      <div className="flex items-center gap-4 mb-4">
        {/* Profile avatar */}
        <div className="w-12 h-12 bg-blue-700 text-white flex items-center justify-center rounded-full text-lg font-bold">
          {avatarLetter}
        </div>
        <h1 className="text-2xl font-bold text-blue-800">
          Welcome, {teacherProfile.email.split("@")[0]}!
        </h1>
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div className="bg-blue-50 p-4 rounded-lg shadow-sm">
          <h2 className="font-semibold text-blue-700">Department</h2>
          <p>{teacherProfile.department}</p>
        </div>

        <div className="bg-blue-50 p-4 rounded-lg shadow-sm">
          <h2 className="font-semibold text-blue-700">Teacher ID</h2>
          <p>{teacherProfile.teacherId}</p>
        </div>

        <div className="bg-blue-50 p-4 rounded-lg shadow-sm">
          <h2 className="font-semibold text-blue-700">Role</h2>
          <p>{teacherProfile.role}</p>
        </div>

        <div className="bg-blue-50 p-4 rounded-lg shadow-sm">
          <h2 className="font-semibold text-blue-700">Email</h2>
          <p>{teacherProfile.email}</p>
        </div>
      </div>
    </div>
  );
};

export default TeacherDashboard;
