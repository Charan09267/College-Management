
import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { ArrowLeft, RefreshCw } from "lucide-react";
import api from "../../services/api";

const EditAnnouncement = () => {
    const { notificationId } = useParams();
    const navigate = useNavigate();

    const [form, setForm] = useState({
        title: "",
        message: "",
    });

    const [announcement, setAnnouncement] = useState(null);
    const [attachments, setAttachments] = useState([]);
    const [loading, setLoading] = useState(true);
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState("");

    useEffect(() => {
        const fetchAnnouncement = async () => {
            try {
                const response = await api.get(
                    `/api/v1/notifications/manage/${notificationId}`
                );

                const data = response.data;

                setAnnouncement(data);
                setForm({
                    title: data.title || "",
                    message: data.message || "",
                });
            } catch (err) {
                setError(
                    err.response?.data?.message ||
                    "Failed to load announcement."
                );
            } finally {
                setLoading(false);
            }
        };

        fetchAnnouncement();
    }, [notificationId]);

    const handleChange = (event) => {
        const { name, value } = event.target;

        setForm((previous) => ({
            ...previous,
            [name]: value,
        }));
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        setError("");

        const formData = new FormData();

        formData.append(
            "request",
            new Blob([JSON.stringify(form)], {
                type: "application/json",
            })
        );

        // If files are selected, they replace the existing attachments.
        // If no files are selected, the existing attachments are retained.
        attachments.forEach((file) => {
            formData.append("attachments", file);
        });

        try {
            setSubmitting(true);

            await api.put(
                `/api/v1/notifications/manage/${notificationId}`,
                formData
            );

            navigate(`/teacher/announcements/${notificationId}`);
        } catch (err) {
            setError(
                err.response?.data?.message ||
                "Failed to update announcement."
            );
        } finally {
            setSubmitting(false);
        }
    };

    const inputClass =
        "w-full rounded-lg border border-gray-300 px-3 py-2 outline-none focus:border-blue-500";

    if (loading) {
        return (
            <div className="flex justify-center py-16">
                <RefreshCw className="animate-spin text-blue-600" size={28} />
            </div>
        );
    }

    if (!announcement) {
        return (
            <div className="rounded-xl bg-white p-8 text-center">
                <p className="text-red-600">
                    {error || "Announcement not found."}
                </p>
                <Link
                    to="/teacher/announcements"
                    className="mt-4 inline-block text-blue-600"
                >
                    Back to announcements
                </Link>
            </div>
        );
    }

    return (
        <div className="mx-auto max-w-3xl space-y-6">
            <Link
                to={`/teacher/announcements/${notificationId}`}
                className="inline-flex items-center gap-2 text-sm text-gray-600 hover:text-blue-600"
            >
                <ArrowLeft size={17} />
                Back to announcement
            </Link>

            <div>
                <h1 className="text-2xl font-semibold text-gray-800">
                    Edit Announcement
                </h1>
                <p className="mt-1 text-sm text-gray-500">
                    Update the announcement title, message, or attachments.
                </p>
            </div>

            <form
                onSubmit={handleSubmit}
                className="space-y-6 rounded-xl bg-white p-6 shadow-sm"
            >
                <div>
                    <label className="mb-1 block text-sm font-medium text-gray-700">
                        Title
                    </label>
                    <input
                        name="title"
                        value={form.title}
                        onChange={handleChange}
                        maxLength={200}
                        required
                        className={inputClass}
                    />
                </div>

                <div>
                    <label className="mb-1 block text-sm font-medium text-gray-700">
                        Message
                    </label>
                    <textarea
                        name="message"
                        value={form.message}
                        onChange={handleChange}
                        rows={7}
                        required
                        className={inputClass}
                    />
                </div>

                <div className="border-t border-gray-100 pt-5">
                    <h2 className="font-medium text-gray-800">
                        Existing attachments
                    </h2>

                    {announcement.attachments?.length > 0 ? (
                        <ul className="mt-2 space-y-2">
                            {announcement.attachments.map((attachment, index) => (
                                <li
                                    key={attachment.id ?? index}
                                    className="text-sm text-gray-600"
                                >
                                    {attachment.fileName ||
                                        attachment.originalFileName ||
                                        `Attachment ${index + 1}`}
                                </li>
                            ))}
                        </ul>
                    ) : (
                        <p className="mt-2 text-sm text-gray-500">
                            No attachments.
                        </p>
                    )}
                </div>

                <div>
                    <label className="mb-1 block text-sm font-medium text-gray-700">
                        Replace attachments (optional)
                    </label>

                    <input
                        type="file"
                        multiple
                        onChange={(event) =>
                            setAttachments(
                                Array.from(event.target.files || [])
                            )
                        }
                        className="w-full text-sm"
                    />

                    <p className="mt-1 text-xs text-gray-500">
                        Selecting files replaces all existing attachments.
                        Leave empty to keep the current attachments.
                    </p>
                </div>

                <div className="rounded-lg bg-gray-50 p-4 text-sm text-gray-600">
                    <p>Category: {announcement.category}</p>
                    <p>Priority: {announcement.priority}</p>
                    <p>Status: {announcement.status}</p>
                    <p>
                        Expiry:{" "}
                        {announcement.expiresAt
                            ? new Date(announcement.expiresAt).toLocaleString()
                            : "No expiry"}
                    </p>
                    <p className="mt-2 text-xs">
                        These fields and recipient targeting cannot be changed
                        through the current update request.
                    </p>
                </div>

                {error && (
                    <p className="text-sm text-red-600">{error}</p>
                )}

                <div className="flex justify-end gap-3">
                    <button
                        type="button"
                        onClick={() =>
                            navigate(`/teacher/announcements/${notificationId}`)
                        }
                        className="rounded-lg border border-gray-300 px-5 py-2 text-sm"
                    >
                        Cancel
                    </button>

                    <button
                        type="submit"
                        disabled={submitting}
                        className="rounded-lg bg-blue-600 px-5 py-2 text-sm font-medium text-white hover:bg-blue-700 disabled:opacity-60"
                    >
                        {submitting ? "Saving..." : "Save Changes"}
                    </button>
                </div>
            </form>
        </div>
    );
};

export default EditAnnouncement;
