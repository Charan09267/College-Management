
import { useCallback, useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Plus, Pencil, Trash2, Eye, RefreshCw } from "lucide-react";
import api from "../../services/api";

const TeacherAnnouncements = () => {
    const [announcements, setAnnouncements] = useState([]);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const fetchAnnouncements = useCallback(async () => {
        try {
            setLoading(true);
            setError("");

            const response = await api.get(
                "/api/v1/notifications/manage",
                {
                    params: { page, size: 10 },
                }
            );

            setAnnouncements(response.data.content || []);
            setTotalPages(response.data.totalPages || 0);
        } catch (err) {
            setError(
                err.response?.data?.message ||
                "Failed to load announcements."
            );
        } finally {
            setLoading(false);
        }
    }, [page]);

    useEffect(() => {
        fetchAnnouncements();
    }, [fetchAnnouncements]);

    const handleDelete = async (announcementId) => {
        const confirmed = window.confirm(
            "Are you sure you want to delete this announcement?"
        );

        if (!confirmed) return;

        try {
            await api.delete(
                `/api/v1/notifications/manage/${announcementId}`
            );

            // Refresh the current page after deletion.
            if (announcements.length === 1 && page > 0) {
                setPage((previous) => previous - 1);
            } else {
                fetchAnnouncements();
            }
        } catch (err) {
            setError(
                err.response?.data?.message ||
                "Failed to delete announcement."
            );
        }
    };

    const formatDate = (date) => {
        if (!date) return "Not published";

        return new Date(date).toLocaleString();
    };

    const getPriorityStyle = (priority) => {
        switch (priority) {
            case "URGENT":
                return "bg-red-100 text-red-700";
            case "HIGH":
                return "bg-orange-100 text-orange-700";
            case "LOW":
                return "bg-gray-100 text-gray-700";
            default:
                return "bg-blue-100 text-blue-700";
        }
    };

    if (loading) {
        return (
            <div className="flex justify-center py-16">
                <RefreshCw className="animate-spin text-blue-600" size={28} />
            </div>
        );
    }

    return (
        <div className="space-y-6">
            <div className="flex flex-wrap items-center justify-between gap-4">
                <div>
                    <h1 className="text-2xl font-semibold text-gray-800">
                        My Announcements
                    </h1>
                    <p className="mt-1 text-sm text-gray-500">
                        Create and manage your announcements.
                    </p>
                </div>

                <Link
                    to="/teacher/announcements/create"
                    className="inline-flex items-center gap-2 rounded-lg bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700"
                >
                    <Plus size={18} />
                    Create Announcement
                </Link>
            </div>

            {error && (
                <div className="flex items-center justify-between rounded-lg bg-red-50 p-3 text-sm text-red-700">
                    <span>{error}</span>
                    <button
                        onClick={fetchAnnouncements}
                        className="font-medium underline"
                    >
                        Retry
                    </button>
                </div>
            )}

            {!error && announcements.length === 0 ? (
                <div className="rounded-xl border border-dashed border-gray-300 bg-white py-16 text-center">
                    <p className="font-medium text-gray-700">
                        No announcements yet
                    </p>
                    <p className="mt-1 text-sm text-gray-500">
                        Your created announcements will appear here.
                    </p>
                </div>
            ) : (
                <div className="space-y-4">
                    {announcements.map((announcement) => (
                        <div
                            key={announcement.id}
                            className="rounded-xl border border-gray-200 bg-white p-5 shadow-sm"
                        >
                            <div className="flex flex-wrap items-start justify-between gap-4">
                                <div className="min-w-0 flex-1">
                                    <div className="mb-2 flex flex-wrap items-center gap-2">
                                        <span className="rounded-full bg-gray-100 px-3 py-1 text-xs font-medium text-gray-700">
                                            {announcement.category?.replaceAll("_", " ")}
                                        </span>

                                        <span
                                            className={`rounded-full px-3 py-1 text-xs font-medium ${getPriorityStyle(announcement.priority)}`}
                                        >
                                            {announcement.priority}
                                        </span>

                                        <span className="rounded-full bg-purple-100 px-3 py-1 text-xs font-medium text-purple-700">
                                            {announcement.status}
                                        </span>
                                    </div>

                                    <h2 className="text-lg font-semibold text-gray-800">
                                        {announcement.title}
                                    </h2>

                                    <p className="mt-2 whitespace-pre-wrap text-sm text-gray-600">
                                        {announcement.message}
                                    </p>

                                    <div className="mt-4 space-y-1 text-xs text-gray-500">
                                        <p>
                                            Created: {formatDate(announcement.createdAt)}
                                        </p>
                                        <p>
                                            Published: {formatDate(announcement.publishedAt)}
                                        </p>
                                        {announcement.expiresAt && (
                                            <p>
                                                Expires: {formatDate(announcement.expiresAt)}
                                            </p>
                                        )}
                                    </div>
                                </div>

                                <div className="flex shrink-0 items-center gap-2">
                                    <Link
                                        to={`/teacher/announcements/${announcement.id}`}
                                        title="View announcement"
                                        className="rounded-lg border border-gray-200 p-2 text-gray-600 hover:bg-gray-50"
                                    >
                                        <Eye size={17} />
                                    </Link>

                                    <Link
                                        to={`/teacher/announcements/${announcement.id}/edit`}
                                        title="Edit announcement"
                                        className="rounded-lg border border-gray-200 p-2 text-blue-600 hover:bg-blue-50"
                                    >
                                        <Pencil size={17} />
                                    </Link>

                                    <button
                                        onClick={() => handleDelete(announcement.id)}
                                        title="Delete announcement"
                                        className="rounded-lg border border-gray-200 p-2 text-red-600 hover:bg-red-50"
                                    >
                                        <Trash2 size={17} />
                                    </button>
                                </div>
                            </div>

                            {announcement.targets?.length > 0 && (
                                <div className="mt-4 border-t border-gray-100 pt-3">
                                    <p className="mb-2 text-xs font-medium text-gray-500">
                                        Recipients
                                    </p>
                                    <div className="flex flex-wrap gap-2">
                                        {announcement.targets.map((target, index) => (
                                            <span
                                                key={target.id ?? index}
                                                className="rounded-md bg-gray-100 px-2 py-1 text-xs text-gray-600"
                                            >
                                                {[
                                                    target.recipientRole,
                                                    target.branch,
                                                    target.year ? `Year ${target.year}` : null,
                                                    target.section ? `Section ${target.section}` : null,
                                                    target.batch,
                                                ]
                                                    .filter(Boolean)
                                                    .join(" · ")}
                                            </span>
                                        ))}
                                    </div>
                                </div>
                            )}

                            {announcement.attachments?.length > 0 && (
                                <p className="mt-3 text-xs text-gray-500">
                                    {announcement.attachments.length} attachment(s)
                                </p>
                            )}
                        </div>
                    ))}
                </div>
            )}

            {totalPages > 1 && (
                <div className="flex items-center justify-between border-t border-gray-200 pt-4">
                    <p className="text-sm text-gray-500">
                        Page {page + 1} of {totalPages}
                    </p>

                    <div className="flex gap-2">
                        <button
                            disabled={page === 0}
                            onClick={() => setPage((previous) => previous - 1)}
                            className="rounded-lg border border-gray-300 px-4 py-2 text-sm disabled:cursor-not-allowed disabled:opacity-40"
                        >
                            Previous
                        </button>

                        <button
                            disabled={page >= totalPages - 1}
                            onClick={() => setPage((previous) => previous + 1)}
                            className="rounded-lg border border-gray-300 px-4 py-2 text-sm disabled:cursor-not-allowed disabled:opacity-40"
                        >
                            Next
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default TeacherAnnouncements;
