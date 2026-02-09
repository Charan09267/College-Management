import { useState } from "react";
import { createBaseUser, createStudent, createTeacher } from "../../api/Api";

const UserManagement = () => {
  const [role, setRole] = useState("STUDENT");

  const [baseUser, setBaseUser] = useState({
    email: "",
    password: "",
    role: "STUDENT",
  });

  const [student, setStudent] = useState({
    name: "",
    rollNumber: "",
    branch: "",
    year: "",
  });

  const [teacher, setTeacher] = useState({
    name: "",
    department: "",
    teacherId: "",
  });

  const handleCreateUser = async () => {
    try {
      const res = await createBaseUser(baseUser);

      const userId = res.data.id;
      const message = res.data.message;

      if (role === "STUDENT") {
        await createStudent({ ...student, userId });
      } else {
        await createTeacher({ ...teacher, userId });
      }

      alert(message);
    } catch (err) {
      console.error(err);
      alert("User creation failed");
    }
  };

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold text-blue-900 mb-6">User Management</h1>

      <div className="flex gap-4 mb-6">
        {["STUDENT", "TEACHER"].map((r) => (
          <button
            key={r}
            onClick={() => {
              setRole(r);
              setBaseUser({ ...baseUser, role: r });
            }}
            className={`px-4 py-2 rounded ${
              role === r
                ? "bg-blue-700 text-white"
                : "bg-blue-100 text-blue-800"
            }`}
          >
            {r}
          </button>
        ))}
      </div>

      <div className="bg-white p-6 rounded-xl shadow mb-6 max-w-lg">
        <h2 className="font-semibold mb-4">Base User</h2>

        <input
          placeholder="Email"
          className="border p-2 w-full mb-3"
          onChange={(e) => setBaseUser({ ...baseUser, email: e.target.value })}
        />

        <input
          placeholder="Password"
          type="password"
          className="border p-2 w-full"
          onChange={(e) =>
            setBaseUser({ ...baseUser, password: e.target.value })
          }
        />
      </div>
      {role === "STUDENT" && (
        <div className="bg-white p-6 rounded-xl shadow max-w-lg">
          <h2 className="font-semibold mb-4">Student Details</h2>

          <input
            placeholder="Name"
            className="border p-2 w-full mb-2"
            onChange={(e) => setStudent({ ...student, name: e.target.value })}
          />
          <input
            placeholder="Roll Number"
            className="border p-2 w-full mb-2"
            onChange={(e) =>
              setStudent({ ...student, rollNumber: e.target.value })
            }
          />
          <input
            placeholder="Branch"
            className="border p-2 w-full mb-2"
            onChange={(e) => setStudent({ ...student, branch: e.target.value })}
          />
          <input
            placeholder="Year"
            type="number"
            className="border p-2 w-full"
            onChange={(e) => setStudent({ ...student, year: e.target.value })}
          />
        </div>
      )}
      {role === "TEACHER" && (
        <div className="bg-white p-6 rounded-xl shadow max-w-lg">
          <h2 className="font-semibold mb-4">Teacher Details</h2>

          <input
            placeholder="Name"
            className="border p-2 w-full mb-2"
            onChange={(e) => setTeacher({ ...teacher, name: e.target.value })}
          />
          <input
            placeholder="Department"
            className="border p-2 w-full mb-2"
            onChange={(e) =>
              setTeacher({ ...teacher, department: e.target.value })
            }
          />
          <input
            placeholder="Teacher ID"
            className="border p-2 w-full"
            onChange={(e) =>
              setTeacher({ ...teacher, teacherId: e.target.value })
            }
          />
        </div>
      )}

      <button
        onClick={handleCreateUser}
        className="mt-6 bg-green-600 text-white px-6 py-3 rounded"
      >
        Create User
      </button>
    </div>
  );
};

export default UserManagement;
