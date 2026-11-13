
import { useEffect, useState } from "react";
import {
    Video,
    Upload,
    PlayCircle,
    Trash2,
    Calendar,
} from "lucide-react";
import {
    getTeacherRecordings,
    uploadRecording,
    streamTeacherRecording,
    deleteRecording,
} from "../../features/recordings/recordingService";
import api from "../../services/api";

const initialFormData = {
    title: "",
    description: "",
    subject: "",
    classDate: "",
    periodNumber: "",
    branch: "",
    year: "",
    file: null,
};

const TeacherRecordings = () => {
    const [recordings, setRecordings] = useState([]);
    const [formData, setFormData] = useState(initialFormData);
    const [selectedRecording, setSelectedRecording] = useState(null);
    const [loading, setLoading] = useState(true);
    const [uploading, setUploading] = useState(false);
    const [error, setError] = useState("");
    const [message, setMessage] = useState("");

    const fetchRecordings = async () => {
        try {
            const data = await getTeacherRecordings();
            setRecordings(data);
        } catch (err) {
            setError(
                err.response?.data?.message ||
                "Failed to load recordings."
            );
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchRecordings();
    }, []);

    const handleChange = (e) => {
        const { name, value, files } = e.target;

        setFormData((previous) => ({
            ...previous,
            [name]: name === "file" ? files[0] : value,
        }));
    };

    const handleUpload = async (e) => {
        e.preventDefault();
        setError("");
        setMessage("");

        const data = new FormData();

        Object.entries(formData).forEach(([key, value]) => {
            if (value !== null && value !== "") {
                data.append(key, value);
            }
        });

        try {
            setUploading(true);
            await uploadRecording(data);

            setMessage("Recording uploaded successfully.");
            setFormData(initialFormData);
            e.target.reset();

            await fetchRecordings();
        } catch (err) {
            setError(
                err.response?.data?.message ||
                "Failed to upload recording."
            );
        } finally {
            setUploading(false);
        }
    };

    const handleDelete = async (recordingId) => {
        if (!window.confirm("Are you sure you want to delete this recording?")) {
            return;
        }

        setError("");
        setMessage("");

        try {
            await deleteRecording(recordingId);

            setRecordings((previous) =>
                previous.filter((recording) => recording.id !== recordingId)
            );

            if (selectedRecording?.id === recordingId) {
                setSelectedRecording(null);
            }

            setMessage("Recording deleted successfully.");
        } catch (err) {
            setError(
                err.response?.data?.message ||
                "Failed to delete recording."
            );
        }
    };

    const getStreamUrl = (recordingId) =>
        `${api.defaults.baseURL}${streamTeacherRecording(recordingId)}`;

    return (
        <div className="space-y-6">
            <div>
                <h1 className="text-2xl font-bold text-slate-800">
                    Manage Recordings
                </h1>
                <p className="text-gray-500 mt-1">
                    Upload and manage your class recordings.
                </p>
            </div>

            {error && (
                <p className="bg-red-50 text-red-600 p-3 rounded-lg">
                    {error}
                </p>
            )}

            {message && (
                <p className="bg-green-50 text-green-700 p-3 rounded-lg">
                    {message}
                </p>
            )}

            {/* Upload form */}
            <section className="bg-white rounded-xl p-6 shadow-sm">
                <div className="flex items-center gap-2 mb-5">
                    <Upload className="text-blue-600" size={22} />
                    <h2 className="text-lg font-semibold text-slate-800">
                        Upload Recording
                    </h2>
                </div>

                <form onSubmit={handleUpload} className="space-y-4">
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <input
                            name="title"
                            value={formData.title}
                            onChange={handleChange}
                            placeholder="Recording title"
                            required
                            className="border rounded-lg p-3 w-full"
                        />

                        <input
                            name="subject"
                            value={formData.subject}
                            onChange={handleChange}
                            placeholder="Subject"
                            required
                            className="border rounded-lg p-3 w-full"
                        />

                        <input
                            name="classDate"
                            type="date"
                            value={formData.classDate}
                            onChange={handleChange}
                            required
                            className="border rounded-lg p-3 w-full"
                        />

                        <input
                            name="periodNumber"
                            type="number"
                            min="1"
                            value={formData.periodNumber}
                            onChange={handleChange}
                            placeholder="Period number"
                            required
                            className="border rounded-lg p-3 w-full"
                        />

                        <input
                            name="branch"
                            value={formData.branch}
                            onChange={handleChange}
                            placeholder="Branch (e.g. CSE)"
                            required
                            className="border rounded-lg p-3 w-full"
                        />

                        <input
                            name="year"
                            type="number"
                            min="1"
                            value={formData.year}
                            onChange={handleChange}
                            placeholder="Academic year"
                            required
                            className="border rounded-lg p-3 w-full"
                        />
                    </div>

                    <textarea
                        name="description"
                        value={formData.description}
                        onChange={handleChange}
                        placeholder="Description (optional)"
                        rows="3"
                        className="border rounded-lg p-3 w-full"
                    />

                    <input
                        name="file"
                        type="file"
                        accept="video/*"
                        onChange={handleChange}
                        required
                        className="border rounded-lg p-3 w-full"
                    />

                    <button
                        type="submit"
                        disabled={uploading}
                        className="bg-blue-600 text-white px-5 py-2.5 rounded-lg hover:bg-blue-700 disabled:opacity-60"
                    >
                        {uploading ? "Uploading..." : "Upload Recording"}
                    </button>
                </form>
            </section>

            {/* Video player */}
            {selectedRecording && (
                <section className="bg-white rounded-xl p-5 shadow-sm">
                    <div className="flex justify-between items-center mb-4">
                        <h2 className="font-semibold text-slate-800">
                            {selectedRecording.title}
                        </h2>

                        <button
                            onClick={() => setSelectedRecording(null)}
                            className="text-sm text-gray-500 hover:text-red-600"
                        >
                            Close
                        </button>
                    </div>

                    <video
                        key={selectedRecording.id}
                        controls
                        autoPlay
                        crossOrigin="use-credentials"
                        className="w-full rounded-lg bg-black max-h-[500px]"
                        src={getStreamUrl(selectedRecording.id)}
                    >
                        Your browser does not support video playback.
                    </video>
                </section>
            )}

            {/* Uploaded recordings */}
            <section className="bg-white rounded-xl p-6 shadow-sm">
                <h2 className="text-lg font-semibold text-slate-800 mb-5">
                    My Uploaded Recordings
                </h2>

                {loading ? (
                    <p className="text-gray-500">Loading recordings...</p>
                ) : recordings.length === 0 ? (
                    <div className="text-center py-10 text-gray-500">
                        <Video size={36} className="mx-auto mb-3 text-gray-400" />
                        <p>You haven't uploaded any recordings yet.</p>
                    </div>
                ) : (
                    <div className="space-y-4">
                        {recordings.map((recording) => (
                            <div
                                key={recording.id}
                                className="border rounded-lg p-4 flex flex-col md:flex-row md:items-center justify-between gap-4"
                            >
                                <div>
                                    <h3 className="font-semibold text-slate-800">
                                        {recording.title}
                                    </h3>

                                    <p className="text-sm text-gray-500 mt-1">
                                        {recording.subject} · {recording.branch} ·
                                        Year {recording.year}
                                    </p>

                                    <p className="text-sm text-gray-500 mt-1 flex items-center gap-2">
                                        <Calendar size={15} />
                                        {recording.classDate} · Period {recording.periodNumber}
                                    </p>
                                </div>

                                <div className="flex gap-3">
                                    <button
                                        onClick={() => setSelectedRecording(recording)}
                                        className="flex items-center gap-2 text-blue-600 hover:text-blue-800"
                                    >
                                        <PlayCircle size={18} />
                                        Play
                                    </button>

                                    <button
                                        onClick={() => handleDelete(recording.id)}
                                        className="flex items-center gap-2 text-red-600 hover:text-red-800"
                                    >
                                        <Trash2 size={18} />
                                        Delete
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </section>
        </div>
    );
};

export default TeacherRecordings;
