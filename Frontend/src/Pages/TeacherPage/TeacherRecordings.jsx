import { useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getTeacherRecordings, deleteTeacherRecording } from "../../api/Api";

const TeacherRecordings = () => {
  const [recordings, setRecordings] = useState([]);
  const [error, setError] = useState("");
  const [selectedVideoId, setSelectedVideoId] = useState(null);

  const navigate = useNavigate();
  const videoRef = useRef(null);

  useEffect(() => {
    loadRecordings({});
  }, []);

  const loadRecordings = async (filterData) => {
    try {
      const res = await getTeacherRecordings(filterData);
      setRecordings(res.data);
    } catch (err) {
      console.error(err);
      setError("Failed to load recordings");
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Delete this recording?")) return;

    try {
      await deleteTeacherRecording(id);
      setRecordings((prev) => prev.filter((r) => r.id !== id));
    } catch {
      alert("Delete failed");
    }
  };

  const closeVideo = () => {
    if (videoRef.current) {
      videoRef.current.pause();
    }
    setSelectedVideoId(null);
  };

  const handleGoBack = () => {
    navigate(-1);
  };

  const grouped = recordings.reduce((acc, rec) => {
    acc[rec.classDate] = acc[rec.classDate] || [];
    acc[rec.classDate].push(rec);
    return acc;
  }, {});

  return (
    <div className="p-8 max-w-7xl mx-auto relative">
      {/* HEADER WITH CLOSE BUTTON */}
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-bold">🎥 Class Recordings</h2>

        {/* This is the Main Page Cross Button */}
        <button
          type="button"
          onClick={handleGoBack}
          className="text-gray-500 hover:text-red-600 transition-colors"
          title="Go Back"
        >
          {/* Large SVG Icon for X */}
          <svg
            xmlns="http://www.w3.org/2000/svg"
            className="h-8 w-8"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2}
              d="M6 18L18 6M6 6l12 12"
            />
          </svg>
        </button>
      </div>

      {error && <p className="text-red-600">{error}</p>}

      {Object.keys(grouped).length === 0 ? (
        <p className="text-gray-600">No recordings found</p>
      ) : (
        Object.entries(grouped).map(([date, recs]) => (
          <div key={date} className="mb-10">
            {/* DATE HEADER */}
            <h3 className="text-lg font-semibold mb-4 border-b pb-2">
              📅 {date}
            </h3>

            {/* GRID OF CLASSES */}
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5">
              {recs
                .sort((a, b) => a.periodNumber - b.periodNumber)
                .map((rec) => (
                  <div
                    key={rec.id}
                    className="bg-white rounded-xl shadow p-4 flex flex-col justify-between"
                  >
                    {/* CLASS INFO */}
                    <div>
                      <p className="text-sm text-gray-500">
                        Branch:{" "}
                        <span className="font-semibold">{rec.branch}</span>
                      </p>

                      <p className="font-semibold text-lg mt-1">
                        Period {rec.periodNumber}
                      </p>

                      <p className="text-blue-700 font-medium">{rec.subject}</p>

                      <p className="text-gray-600 text-sm mt-1">{rec.title}</p>
                    </div>

                    {/* ACTIONS */}
                    <div className="flex gap-3 mt-4">
                      <button
                        type="button"
                        onClick={() => setSelectedVideoId(rec.id)}
                        className="flex-1 border border-blue-600 text-blue-600 py-1 rounded hover:bg-blue-50"
                      >
                        ▶ Play
                      </button>

                      <button
                        type="button" // Important: Prevent form submit
                        onClick={() => handleDelete(rec.id)}
                        className="flex-1 border border-red-600 text-red-600 py-1 rounded hover:bg-red-50"
                      >
                        🗑 Delete
                      </button>
                    </div>
                  </div>
                ))}
            </div>
          </div>
        ))
      )}

      {/* VIDEO MODAL */}
      {selectedVideoId && (
        <div
          className="fixed inset-0 bg-black bg-opacity-70 flex items-center justify-center z-50"
          onClick={closeVideo}
        >
          <div
            className="bg-white rounded-lg w-full max-w-4xl p-4 relative"
            onClick={(e) => e.stopPropagation()}
          >
            <button
              type="button"
              onClick={closeVideo}
              className="absolute top-3 right-3 text-xl font-bold bg-gray-200 hover:bg-gray-300 w-8 h-8 rounded-full flex items-center justify-center"
            >
              ✕
            </button>

            <video
              ref={videoRef}
              controls
              autoPlay
              className="w-full rounded mt-6"
              src={`http://localhost:8080/api/v1/teacher/recordings/${selectedVideoId}/video`}
            />
          </div>
        </div>
      )}
    </div>
  );
};

export default TeacherRecordings;
