import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { deleteNotification } from "../../features/notifications/notificationService";

const AdminAnnouncements = () => {
    const [announcements, setAnnouncements] = useState([]);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const fetchAnnouncements = async () => {
        try {
            setLoading(true);
            setError("");

            const response = await (
                await import("../../services/api")
            ).default.get("/api/v1/notifications/manage", {
                params: { page, size: 10 },
            });

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
    };

    useEffect(() => {
        fetchAnnouncements();
    }, [page]);

    const handleDelete = async (id) => {
        if (!window.confirm("Are you sure you want to delete this announcement?")) {
            return;
        }

        try {
            await deleteNotification(id);
            fetchAnnouncements();
        } catch (err) {
            setError(
                err.response?.data?.message ||
                "Failed to delete announcement."
            );
        }
    };

    if (loading) {
        return <p className="p-6 text-gray-500">Loading announcements...</p>;
    }

    return (
        <div className="space-y-6">
            <div className="flex items-center justify-between">
                <div>
                    <h1 className="text-2xl font-bold text-gray-800">
                        Announcements
                    </h1>
                    <p className="mt-1 text-sm text-gray-500">
                        Manage platform announcements.
                    </p>
                </div>

                <Link
                    to="/admin/announcements/create"
                    className="rounded-lg bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700"
                >
                    Create Announcement
                </Link>
            </div>

            {error && (
                <p className="rounded-lg bg-red-50 p-3 text-sm text-red-600">
                    {error}
                </p>
            )}

            {announcements.length === 0 ? (
                <div className="rounded-xl border border-gray-200 bg-white p-8 text-center">
                    <p className="text-gray-500">
                        No announcements found.
                    </p>
                </div>
            ) : (
                <div className="space-y-4">
                    {announcements.map((item) => (
                        <div
                            key={item.id}
                            className="rounded-xl border border-gray-200 bg-white p-5 shadow-sm"
                        >
                            <div className="flex flex-wrap items-start justify-between gap-4">
                                <div className="min-w-0 flex-1">
                                    <h2 className="font-semibold text-gray-800">
                                        {item.title}
                                    </h2>

                                    <p className="mt-2 whitespace-pre-wrap text-sm text-gray-600">
                                        {item.message}
                                    </p>

                                    <div className="mt-3 flex flex-wrap gap-2 text-xs">
                                        <span className="rounded-full bg-blue-50 px-3 py-1 text-blue-700">
                                            {item.category}
                                        </span>
                                        <span className="rounded-full bg-gray-100 px-3 py-1 text-gray-700">
                                            {item.priority}
                                        </span>
                                        <span className="rounded-full bg-gray-100 px-3 py-1 text-gray-700">
                                            {item.status}
                                        </span>
                                    </div>

                                    <p className="mt-3 text-xs text-gray-400">
                                        Created by: {item.createdByName || "Admin"}
                                    </p>
                                </div>

                                <div className="flex gap-3 text-sm">
                                    <Link
                                        to={`/admin/announcements/${item.id}`}
                                        className="font-medium text-blue-600 hover:underline"
                                    >
                                        View
                                    </Link>

                                    <Link
                                        to={`/admin/announcements/${item.id}/edit`}
                                        className="font-medium text-amber-600 hover:underline"
                                    >
                                        Edit
                                    </Link>

                                    <button
                                        onClick={() => handleDelete(item.id)}
                                        className="font-medium text-red-600 hover:underline"
                                    >
                                        Delete
                                    </button>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            )}

            {totalPages > 1 && (
                <div className="flex items-center justify-center gap-4">
                    <button
                        disabled={page === 0}
                        onClick={() => setPage((current) => current - 1)}
                        className="rounded-lg border px-4 py-2 text-sm disabled:opacity-40"
                    >
                        Previous
                    </button>

                    <span className="text-sm text-gray-600">
                        Page {page + 1} of {totalPages}
                    </span>

                    <button
                        disabled={page >= totalPages - 1}
                        onClick={() => setPage((current) => current + 1)}
                        className="rounded-lg border px-4 py-2 text-sm disabled:opacity-40"
                    >
                        Next
                    </button>
                </div>
            )}
        </div>
    );
};

export default AdminAnnouncements;