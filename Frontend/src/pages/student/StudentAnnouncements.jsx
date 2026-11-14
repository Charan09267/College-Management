
import { useEffect, useState } from "react";
import {
    Bell,
    AlertTriangle,
    Megaphone,
    CheckCheck,
} from "lucide-react";
import { Link } from "react-router-dom";
import {
    getNotifications,
    getUnreadCount,
    markNotificationAsRead,
    markAllNotificationsAsRead,
} from "../../features/notifications/notificationService";

const StudentAnnouncements = () => {
    const [notifications, setNotifications] = useState([]);
    const [unreadCount, setUnreadCount] = useState(0);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [filter, setFilter] = useState("ALL");
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const fetchNotifications = async () => {
        try {
            setLoading(true);
            setError("");

            const [data, unreadData] = await Promise.all([
                getNotifications(page, 10),
                getUnreadCount(),
            ]);

            setNotifications(data.content || []);
            setTotalPages(data.totalPages || 0);
            setUnreadCount(unreadData.unreadCount || 0);
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
        fetchNotifications();
    }, [page]);

    const handleMarkAsRead = async (notificationId) => {
        try {
            await markNotificationAsRead(notificationId);

            setNotifications((previous) =>
                previous.map((notification) =>
                    notification.notificationId === notificationId
                        ? { ...notification, read: true }
                        : notification
                )
            );

            setUnreadCount((previous) => Math.max(0, previous - 1));
        } catch (err) {
            setError("Failed to mark announcement as read.");
        }
    };

    const handleMarkAllAsRead = async () => {
        try {
            await markAllNotificationsAsRead();

            setNotifications((previous) =>
                previous.map((notification) => ({
                    ...notification,
                    read: true,
                }))
            );

            setUnreadCount(0);
        } catch (err) {
            setError("Failed to mark all announcements as read.");
        }
    };

    const filteredNotifications = notifications.filter((notification) => {
        if (filter === "URGENT") {
            return notification.priority === "URGENT";
        }

        if (filter === "REGULAR") {
            return notification.priority !== "URGENT";
        }

        return true;
    });

    return (
        <div className="space-y-6">
            <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
                <div>
                    <h1 className="text-2xl font-bold text-slate-800">
                        Announcements
                    </h1>
                    <p className="text-gray-500 mt-1">
                        Stay updated with college announcements.
                    </p>
                </div>

                <div className="flex items-center gap-2 text-sm text-gray-600">
                    <Bell size={18} />
                    {unreadCount} unread
                </div>
            </div>

            <div className="flex flex-wrap items-center justify-between gap-3">
                <div className="flex gap-2">
                    {["ALL", "URGENT", "REGULAR"].map((item) => (
                        <button
                            key={item}
                            onClick={() => setFilter(item)}
                            className={`px-4 py-2 rounded-lg text-sm ${
                                filter === item
                                    ? "bg-blue-600 text-white"
                                    : "bg-white text-gray-600 border"
                            }`}
                        >
                            {item === "ALL"
                                ? "All"
                                : item === "URGENT"
                                  ? "URGENT"
                                  : "Regular"}
                        </button>
                    ))}
                </div>

                {unreadCount > 0 && (
                    <button
                        onClick={handleMarkAllAsRead}
                        className="flex items-center gap-2 text-sm text-blue-600 hover:text-blue-800"
                    >
                        <CheckCheck size={17} />
                        Mark all as read
                    </button>
                )}
            </div>

            {error && (
                <p className="bg-red-50 text-red-600 p-3 rounded-lg">
                    {error}
                </p>
            )}

            {loading ? (
                <p className="text-gray-500">Loading announcements...</p>
            ) : filteredNotifications.length === 0 ? (
                <div className="bg-white rounded-xl p-12 text-center shadow-sm">
                    <Megaphone
                        size={36}
                        className="mx-auto mb-3 text-gray-400"
                    />
                    <p className="text-gray-500">
                        No announcements available.
                    </p>
                </div>
            ) : (
                <div className="space-y-4">
                    {filteredNotifications.map((notification) => {
                        const emergency =
                            notification.priority === "URGENT";

                        return (
                            <article
                                key={notification.notificationId}
                                className={`bg-white rounded-xl p-5 shadow-sm border-l-4 ${
                                    emergency
                                        ? "border-red-500"
                                        : "border-blue-500"
                                } ${!notification.read ? "bg-blue-50/30" : ""}`}
                            >
                                <div className="flex flex-col sm:flex-row sm:justify-between gap-3">
                                    <div className="flex items-start gap-3">
                                        {emergency ? (
                                            <AlertTriangle
                                                className="text-red-600 mt-1"
                                                size={21}
                                            />
                                        ) : (
                                            <Megaphone
                                                className="text-blue-600 mt-1"
                                                size={21}
                                            />
                                        )}

                                        <div>
                                            <h2 className="font-semibold text-slate-800">
                                                {notification.title}
                                            </h2>

                                            <p className="text-sm text-gray-500 mt-1">
                                                {notification.category} ·
                                                {notification.createdByName}
                                            </p>
                                        </div>
                                    </div>

                                    {!notification.read && (
                                        <span className="text-xs font-medium text-blue-600">
                                            New
                                        </span>
                                    )}
                                </div>

                                <p className="text-gray-700 text-sm mt-4 whitespace-pre-wrap">
                                    {notification.message}
                                </p>

                                
<div className="flex flex-wrap justify-between items-center gap-3 mt-5 pt-4 border-t">
    <span className="text-xs text-gray-500">
        {notification.publishedAt
            ? new Date(notification.publishedAt).toLocaleString()
            : ""}
    </span>

    <div className="flex items-center gap-4">
        <Link
            to={`/student/announcements/${notification.notificationId}`}
            state={{ notification }}
            className="text-sm font-medium text-blue-600 hover:text-blue-800"
        >
            View Details
        </Link>

        {!notification.read && (
            <button
                onClick={() =>
                    handleMarkAsRead(notification.notificationId)
                }
                className="text-sm text-blue-600 hover:text-blue-800"
            >
                Mark as read
            </button>
        )}
    </div>
</div>

                            </article>
                        );
                    })}
                </div>
            )}

            {!loading && totalPages > 1 && (
                <div className="flex justify-center items-center gap-4">
                    <button
                        disabled={page === 0}
                        onClick={() => setPage((previous) => previous - 1)}
                        className="px-4 py-2 border rounded-lg disabled:opacity-40"
                    >
                        Previous
                    </button>

                    <span className="text-sm text-gray-600">
                        Page {page + 1} of {totalPages}
                    </span>

                    <button
                        disabled={page >= totalPages - 1}
                        onClick={() => setPage((previous) => previous + 1)}
                        className="px-4 py-2 border rounded-lg disabled:opacity-40"
                    >
                        Next
                    </button>
                </div>
            )}
        </div>
    );
};

export default StudentAnnouncements;
