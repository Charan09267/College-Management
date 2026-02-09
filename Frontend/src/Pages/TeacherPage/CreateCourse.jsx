import { useState } from "react";
import { useSelector } from "react-redux";
import { createCourse } from "../../api/Api";

const CreateCourse = () => {
  const teacherProfile = useSelector((state) => state.teacher.profile);

  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [loading, setLoading] = useState(false);

  if (!teacherProfile) {
    return <p className="text-gray-600">Loading teacher profile...</p>;
  }

  const handleSubmit = async (e) => {
    e.preventDefault();

    const payload = {
      title,
      description,
      id: teacherProfile.id,
    };

    try {
      setLoading(true);
      await createCourse(payload);
      alert("Course created successfully!");

      setTitle("");
      setDescription("");
    } catch (err) {
      console.error(err);
      alert("Failed to create course");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-white p-6 rounded-xl shadow max-w-xl">
      <h1 className="text-2xl font-bold text-blue-800 mb-4">Create Course</h1>

      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Course Title"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          required
          className="w-full mb-3 px-4 py-2 border rounded"
        />

        <textarea
          placeholder="Course Description"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          required
          className="w-full mb-3 px-4 py-2 border rounded"
        />

        <button
          type="submit"
          disabled={loading}
          className="bg-blue-700 text-white px-6 py-2 rounded hover:bg-blue-800 disabled:opacity-50"
        >
          {loading ? "Creating..." : "Create"}
        </button>
      </form>
    </div>
  );
};

export default CreateCourse;
