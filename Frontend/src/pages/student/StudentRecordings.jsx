
import { useEffect, useState } from "react";
import { Video, PlayCircle, Calendar, BookOpen } from "lucide-react";
import { getStudentRecordings, streamStudentRecording } from "../../features/recordings/recordingService";
import api from "../../services/api";

const StudentRecordings = () => {
    const [recordings, setRecordings] = useState([]);
    const [selectedRecording, setSelectedRecording] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        const fetchRecordings = async () => {
            try {
                const data = await getStudentRecordings();
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

        fetchRecordings();
    }, []);

    const getStreamUrl = (recordingId) => {
        return `${api.defaults.baseURL}${streamStudentRecording(recordingId)}`;
    };

    if (loading) {
        return <p className="text-gray-500">Loading recordings...</p>;
    }

    if (error) {
        return <p className="text-red-600">{error}</p>;
    }

    return (
        <div className="space-y-6">
            <div>
                <h1 className="text-2xl font-bold text-slate-800">
                    Class Recordings
                </h1>
                <p className="text-gray-500 mt-1">
                    Access your available class recordings.
                </p>
            </div>

            {selectedRecording && (
                <div className="bg-white rounded-xl p-5 shadow-sm">
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
                </div>
            )}

            {recordings.length === 0 ? (
                <div className="bg-white rounded-xl p-12 text-center shadow-sm">
                    <Video size={40} className="mx-auto mb-3 text-gray-400" />
                    <p className="text-gray-500">
                        No recordings available yet.
                    </p>
                </div>
            ) : (
                <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-5">
                    {recordings.map((recording) => (
                        <div
                            key={recording.id}
                            className="bg-white rounded-xl p-5 shadow-sm"
                        >
                            <div className="flex items-start justify-between">
                                <div className="p-3 bg-blue-50 rounded-lg">
                                    <Video className="text-blue-600" size={24} />
                                </div>

                                <span className="text-xs text-gray-500">
                                    {recording.branch} · Year {recording.year}
                                </span>
                            </div>

                            <h2 className="font-semibold text-slate-800 mt-4">
                                {recording.title}
                            </h2>

                            <p className="text-sm text-gray-500 mt-2">
                                {recording.description || "No description provided."}
                            </p>

                            <div className="space-y-2 mt-4 text-sm text-gray-500">
                                <p className="flex items-center gap-2">
                                    <BookOpen size={16} />
                                    {recording.subject}
                                </p>

                                <p className="flex items-center gap-2">
                                    <Calendar size={16} />
                                    {recording.classDate}
                                </p>

                                <p>
                                    Period: {recording.periodNumber}
                                </p>
                            </div>

                            <button
                                onClick={() => setSelectedRecording(recording)}
                                className="w-full mt-5 flex items-center justify-center gap-2 bg-blue-600 text-white py-2.5 rounded-lg hover:bg-blue-700 transition"
                            >
                                <PlayCircle size={18} />
                                Watch Recording
                            </button>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default StudentRecordings;
