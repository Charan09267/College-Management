import { useState } from "react";
import { uploadClassRecordingApi } from "../../api/Api";

const UploadClassRecording = () => {
  const [video, setVideo] = useState(null);
  const [form, setForm] = useState({
    title: "",
    description: "",
    subject: "",
    classDate: "",
    periodNumber: "",
    branch: "",
    year: "",
  });

  const [progress, setProgress] = useState(0);
  const [uploading, setUploading] = useState(false);
  const [message, setMessage] = useState("");

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleUpload = async () => {
    if (!video) {
      setMessage("Please select a video");
      return;
    }

    const formData = new FormData();
    formData.append("video", video);
    formData.append(
      "data",
      new Blob([JSON.stringify(form)], { type: "application/json" }),
    );

    try {
      setUploading(true);
      setProgress(0);
      setMessage("");

      await uploadClassRecordingApi(formData, setProgress);

      setMessage("✅ Upload successful");
    } catch (err) {
      console.error(err);
      setMessage("❌ Upload failed");
    } finally {
      setUploading(false);
    }
  };

  return (
    <div className="p-8 max-w-5xl mx-auto">
      <h2 className="text-2xl font-bold mb-6">🎥 Upload Class Recording</h2>

      <div className="bg-white rounded-lg shadow p-6 grid grid-cols-1 md:grid-cols-2 gap-6">
        {/* LEFT – Form */}
        <div>
          <h3 className="font-semibold mb-4 text-lg">Class Details</h3>

          <div className="space-y-3">
            <input
              name="title"
              placeholder="Class Title"
              onChange={handleChange}
              className="w-full border p-2 rounded"
            />

            <textarea
              name="description"
              placeholder="Description"
              rows={3}
              onChange={handleChange}
              className="w-full border p-2 rounded"
            />

            <div className="grid grid-cols-2 gap-3">
              <input
                name="subject"
                placeholder="Subject"
                onChange={handleChange}
                className="border p-2 rounded"
              />
              <input
                name="branch"
                placeholder="Branch"
                onChange={handleChange}
                className="border p-2 rounded"
              />
            </div>

            <div className="grid grid-cols-2 gap-3">
              <input
                type="date"
                name="classDate"
                onChange={handleChange}
                className="border p-2 rounded"
              />
              <input
                type="number"
                name="year"
                placeholder="Year"
                onChange={handleChange}
                className="border p-2 rounded"
              />
            </div>

            <input
              type="number"
              name="periodNumber"
              placeholder="Period Number"
              onChange={handleChange}
              className="w-full border p-2 rounded"
            />
          </div>
        </div>

        {/* RIGHT – Video Upload */}
        <div>
          <h3 className="font-semibold mb-4 text-lg">Video File</h3>

          <div className="border-2 border-dashed rounded p-6 text-center">
            <input
              type="file"
              accept="video/*"
              onChange={(e) => setVideo(e.target.files[0])}
              className="hidden"
              id="videoUpload"
            />
            <label
              htmlFor="videoUpload"
              className="cursor-pointer text-blue-600 font-medium"
            >
              Click to select video
            </label>

            {video && (
              <p className="mt-2 text-sm text-gray-600">
                Selected: {video.name}
              </p>
            )}
          </div>

          {/* Progress */}
          {uploading && (
            <div className="mt-4">
              <div className="w-full bg-gray-200 h-3 rounded">
                <div
                  className="bg-blue-600 h-3 rounded transition-all"
                  style={{ width: `${progress}%` }}
                />
              </div>
              <p className="text-sm mt-1">{progress}% uploaded</p>
            </div>
          )}

          <button
            onClick={handleUpload}
            disabled={uploading}
            className={`mt-6 w-full py-2 text-white rounded ${
              uploading ? "bg-gray-400" : "bg-blue-600 hover:bg-blue-700"
            }`}
          >
            {uploading ? "Uploading..." : "Upload Recording"}
          </button>

          {message && <p className="mt-3 text-center text-sm">{message}</p>}
        </div>
      </div>
    </div>
  );
};

export default UploadClassRecording;
