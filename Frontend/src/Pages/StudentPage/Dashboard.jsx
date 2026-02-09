import { useSelector } from "react-redux";

const Dashboard = () => {
  const student = useSelector((state) => state.student.profile);

  if (!student) {
    return (
      <div className="text-center text-xl text-gray-500">
        Loading profile...
      </div>
    );
  }

  return (
    <div className="max-w-5xl mx-auto">
      <h1 className="text-3xl font-bold text-blue-900 mb-6">
        Student Dashboard
      </h1>

      <div className="bg-white rounded-2xl shadow-xl p-8 flex gap-8 items-center">
        {/* Profile Photo */}
        <div className="w-36 h-36 rounded-full bg-blue-200 flex items-center justify-center text-4xl font-bold text-blue-900">
          {student.name.charAt(0).toUpperCase()}
        </div>

        {/* Student Info */}
        <div className="grid grid-cols-2 gap-x-12 gap-y-4 text-lg">
          <div>
            <span className="font-semibold text-gray-600">Name</span>
            <p className="text-xl">{student.name}</p>
          </div>

          <div>
            <span className="font-semibold text-gray-600">Roll Number</span>
            <p className="text-xl">{student.rollNumber}</p>
          </div>

          <div>
            <span className="font-semibold text-gray-600">Branch</span>
            <p className="text-xl">{student.branch}</p>
          </div>

          <div>
            <span className="font-semibold text-gray-600">Year</span>
            <p className="text-xl">{student.year}</p>
          </div>

          <div>
            <span className="font-semibold text-gray-600">Email</span>
            <p className="text-xl">{student.email}</p>
          </div>

          <div>
            <span className="font-semibold text-gray-600">Role</span>
            <p className="text-xl">{student.role}</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
