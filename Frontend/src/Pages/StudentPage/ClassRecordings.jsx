import { useEffect, useRef, useState } from "react";
import { getStudentRecordings } from "../../api/Api";

const ClassRecordings = () => {
  const [filters, setFilters] = useState({
    date: "",
    subject: "",
    period: "",
  });

  const [recordings, setRecordings] = useState([]);
  const [error, setError] = useState("");
  const [selectedVideoId, setSelectedVideoId] = useState(null);

  const videoRef = useRef(null);

  useEffect(() => {
    fetchRecordings({});
  }, []);

  const fetchRecordings = async (filterData) => {
    try {
      const res = await getStudentRecordings(filterData);
      setRecordings(res.data);
      setError("");
    } catch (err) {
      console.error(err);
      setError("Failed to load recordings");
    }
  };

  const handleSearch = () => {
    if (filters.period && !filters.date && !filters.subject) {
      setError("Period alone is not allowed");
      return;
    }
    fetchRecordings(filters);
  };

  const closeVideo = () => {
    if (videoRef.current) {
      videoRef.current.pause();
    }
    setSelectedVideoId(null);
  };
  const groupedByDate = recordings.reduce((acc, rec) => {
    acc[rec.classDate] = acc[rec.classDate] || [];
    acc[rec.classDate].push(rec);
    return acc;
  }, {});

  return (
    <div className="p-8 max-w-7xl mx-auto relative">
      {/* HEADER */}
      <div className="mb-6">
        <h2 className="text-2xl font-bold">🎥 Class Recordings</h2>
      </div>

      {/* FILTERS */}
      <div className="bg-gray-50 p-4 rounded-lg mb-8 flex flex-wrap gap-4 items-end shadow-sm border">
        <div>
          <label className="block text-sm text-gray-600 mb-1">Date</label>
          <input
            type="date"
            value={filters.date}
            onChange={(e) => setFilters({ ...filters, date: e.target.value })}
            className="border p-2 rounded w-full"
          />
        </div>

        <div>
          <label className="block text-sm text-gray-600 mb-1">Subject</label>
          <input
            type="text"
            placeholder="e.g. Math"
            value={filters.subject}
            onChange={(e) =>
              setFilters({ ...filters, subject: e.target.value })
            }
            className="border p-2 rounded w-full"
          />
        </div>

        <div>
          <label className="block text-sm text-gray-600 mb-1">Period</label>
          <input
            type="number"
            placeholder="No."
            value={filters.period}
            onChange={(e) => setFilters({ ...filters, period: e.target.value })}
            className="border p-2 rounded w-20"
          />
        </div>

        <button
          type="button"
          onClick={handleSearch}
          className="bg-blue-600 text-white px-6 py-2 rounded hover:bg-blue-700 h-11 mb-[1px]"
        >
          Search
        </button>
      </div>

      {error && <p className="text-red-600 mb-6">{error}</p>}

      {/* RECORDINGS GRID */}
      {Object.keys(groupedByDate).length === 0 ? (
        <p className="text-gray-600 text-center mt-10">No recordings found.</p>
      ) : (
        Object.entries(groupedByDate).map(([date, recs]) => (
          <div key={date} className="mb-10">
            {/* DATE HEADER */}
            <h3 className="text-lg font-semibold mb-4 border-b pb-2 text-gray-700">
              📅 {date}
            </h3>

            {/* GRID LAYOUT */}
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5">
              {recs
                .sort((a, b) => a.periodNumber - b.periodNumber)
                .map((rec) => (
                  <div
                    key={rec.id}
                    className="bg-white rounded-xl shadow p-4 flex flex-col justify-between border border-gray-100"
                  >
                    <div>
                      <div className="flex justify-between items-start">
                        <span className="bg-gray-100 text-gray-600 text-xs font-bold px-2 py-1 rounded">
                          Period {rec.periodNumber}
                        </span>
                      </div>

                      <h4 className="text-blue-700 font-bold text-lg mt-3">
                        {rec.subject}
                      </h4>
                      <p className="text-gray-600 text-sm mt-1 mb-4">
                        {rec.title}
                      </p>
                    </div>

                    <button
                      type="button"
                      onClick={() => setSelectedVideoId(rec.id)}
                      className="w-full border border-blue-600 text-blue-600 py-2 rounded hover:bg-blue-50 transition-colors flex items-center justify-center gap-2"
                    >
                      ▶ Watch Class
                    </button>
                  </div>
                ))}
            </div>
          </div>
        ))
      )}

      {/* VIDEO MODAL (Overlay) */}
      {selectedVideoId && (
        <div
          className="fixed inset-0 bg-black bg-opacity-70 flex items-center justify-center z-50"
          onClick={closeVideo}
        >
          <div
            className="bg-white rounded-lg w-full max-w-4xl p-4 relative shadow-2xl"
            onClick={(e) => e.stopPropagation()}
          >
            <button
              type="button"
              onClick={closeVideo}
              className="absolute top-3 right-3 text-xl font-bold bg-gray-100 hover:bg-gray-200 w-8 h-8 rounded-full flex items-center justify-center"
            >
              ✕
            </button>

            <div className="mt-8">
              <video
                ref={videoRef}
                controls
                autoPlay
                className="w-full rounded shadow-lg"
                src={`http://localhost:8080/api/v1/students/recordings/${selectedVideoId}/video`}
              />
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ClassRecordings;
