import { useEffect, useState } from "react";
import { getAllCourses, getEnrolledCourses, enrollCourse } from "../../api/Api";
import { useSelector } from "react-redux";

const AllCourses = () => {
  const [courses, setCourses] = useState([]);
  const [enrolledIds, setEnrolledIds] = useState(new Set());
  const [loading, setLoading] = useState(true);
  const [toast, setToast] = useState(null);

  const student = useSelector((state) => state.student.profile);

  useEffect(() => {
    const loadData = async () => {
      try {
        const [allRes, enrolledRes] = await Promise.all([
          getAllCourses(),
          getEnrolledCourses(),
        ]);

        setCourses(allRes.data);

        const ids = new Set(enrolledRes.data.map((c) => c.id));
        setEnrolledIds(ids);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, []);

  const handleEnroll = async (courseId) => {
    try {
      await enrollCourse(student.userId, courseId);

      setEnrolledIds((prev) => new Set(prev).add(courseId));

      setToast({
        type: "success",
        message: "Successfully enrolled 🎉",
      });

      setTimeout(() => setToast(null), 3000);
    } catch (err) {
      setToast({
        type: "error",
        message: "Enrollment failed ❌",
      });

      setTimeout(() => setToast(null), 3000);
    }
  };

  if (loading)
    return (
      <p className="text-center text-lg text-blue-900">Loading courses...</p>
    );

  return (
    <div className="relative">
      {/* TOAST */}
      {toast && (
        <div
          className={`fixed top-5 right-5 px-6 py-3 rounded-lg shadow-lg text-white
          ${toast.type === "success" ? "bg-blue-700" : "bg-blue-500"}`}
        >
          {toast.message}
        </div>
      )}

      <h1 className="text-2xl font-bold mb-6 text-blue-900">
        Available Courses
      </h1>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {courses.map((course) => {
          const isEnrolled = enrolledIds.has(course.id);

          return (
            <div
              key={course.id}
              className="bg-blue-700 text-white rounded-xl shadow-md p-5
                         hover:shadow-xl transition flex flex-col justify-between"
            >
              <div>
                <h2 className="text-xl font-semibold mb-2">{course.title}</h2>

                <p className="text-sm mb-3">{course.description}</p>

                <div className="text-sm space-y-1">
                  <p>
                    <span className="font-medium">Status:</span>{" "}
                    {course.isActive ? "Active" : "Inactive"}
                  </p>
                </div>
              </div>

              {/* BUTTON LOGIC */}
              {isEnrolled ? (
                <button
                  disabled
                  className="mt-5 w-full bg-white text-blue-700 py-2 rounded-lg
                             font-semibold  opacity-70"
                >
                  Enrolled ✓
                </button>
              ) : (
                <button
                  onClick={() => handleEnroll(course.id)}
                  className="mt-5 w-full bg-white text-blue-700 py-2 rounded-lg
                             hover:bg-blue-100 transition font-semibold cursor-pointer"
                >
                  Enroll
                </button>
              )}
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default AllCourses;
