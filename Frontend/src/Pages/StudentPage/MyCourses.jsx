import { useEffect, useState } from "react";
import { getEnrolledCourses } from "../../api/Api";

const MyCourses = () => {
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getEnrolledCourses()
      .then((res) => setCourses(res.data))
      .catch((err) => console.error(err))
      .finally(() => setLoading(false));
  }, []);

  if (loading)
    return (
      <p className="text-center text-lg text-blue-900">
        Loading enrolled courses...
      </p>
    );

  if (courses.length === 0)
    return (
      <p className="text-center text-gray-200 text-lg">
        You have not enrolled in any courses yet.
      </p>
    );

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6 text-blue-900">
        My Enrolled Courses
      </h1>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {courses.map((course) => (
          <div
            key={course.id}
            className="bg-blue-700 text-white rounded-xl shadow-md p-5 hover:shadow-xl transition"
          >
            <h2 className="text-xl font-semibold mb-2">{course.title}</h2>
            <p className="text-white text-sm mb-3">{course.description}</p>

            <div className="text-sm space-y-1">
              <p>
                <span className="font-medium">Status:</span>{" "}
                {course.isActive ? "Active" : "Inactive"}
              </p>

              <p>
                <span className="font-medium">Enrolled On:</span>{" "}
                {new Date(course.createdAt).toLocaleDateString()}
              </p>
            </div>

            {/* Enrolled Badge */}
            <div className="mt-4 text-center">
              <span className="inline-block bg-green-100 text-green-700 px-4 py-1 rounded-full text-sm font-medium">
                Enrolled
              </span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default MyCourses;
