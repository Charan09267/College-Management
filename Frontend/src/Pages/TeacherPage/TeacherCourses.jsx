import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { FaUsers, FaTrash } from "react-icons/fa";
import { getTeacherCourses, deleteTeacherCourse } from "../../api/Api";

const TeacherCourses = () => {
  const teacherProfile = useSelector((state) => state.teacher.profile);

  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!teacherProfile?.id) return;

    const fetchCourses = async () => {
      try {
        const res = await getTeacherCourses(teacherProfile.id);
        setCourses(res.data);
      } catch (err) {
        console.error(err);
        setError("Failed to load courses");
      } finally {
        setLoading(false);
      }
    };

    fetchCourses();
  }, [teacherProfile]);

  const handleDelete = async (courseId) => {
    const confirmDelete = window.confirm(
      "Are you sure you want to delete this course?\nThis action cannot be undone.",
    );

    if (!confirmDelete) return;

    try {
      await deleteTeacherCourse(courseId);

      setCourses((prev) => prev.filter((c) => c.id !== courseId));
    } catch (err) {
      console.error(err);
      alert("Failed to delete course");
    }
  };

  if (!teacherProfile) return <p>Loading teacher profile...</p>;
  if (loading) return <p>Loading courses...</p>;
  if (error) return <p className="text-red-600">{error}</p>;

  return (
    <div>
      <h1 className="text-2xl font-bold text-blue-800 mb-4">My Courses</h1>

      {courses.length === 0 ? (
        <p className="text-gray-600">You haven’t created any courses yet.</p>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {courses.map((course) => (
            <div
              key={course.id}
              className="bg-white p-5 rounded-xl shadow relative"
            >
              <button
                onClick={() => handleDelete(course.id)}
                className="absolute top-3 right-3 text-red-500 hover:text-red-700"
                title="Delete course"
              >
                <FaTrash />
              </button>

              <h2 className="text-lg font-semibold text-blue-700">
                {course.title}
              </h2>
              <p className="text-gray-600 mt-2 line-clamp-3">
                {course.description}
              </p>

              <div className="flex justify-between items-center mt-4 text-sm text-gray-500">
                <span>
                  Created: {new Date(course.createdAt).toLocaleDateString()}
                </span>
                <div className="flex items-center gap-3">
                  <div className="flex items-center gap-1 bg-blue-50 text-blue-700 px-2 py-1 rounded">
                    <FaUsers className="text-sm" />
                    <span className="font-medium">
                      {course.likesCount ?? 0}
                    </span>
                  </div>
                  <span
                    className={`px-2 py-1 rounded ${course.isActive ? "bg-green-100 text-green-700" : "bg-red-100 text-red-700"}`}
                  >
                    {course.isActive ? "Active" : "Inactive"}
                  </span>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default TeacherCourses;
