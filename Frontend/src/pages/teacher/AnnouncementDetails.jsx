
import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { ArrowLeft, Calendar, Paperclip, RefreshCw } from "lucide-react";
import api from "../../services/api";

const AnnouncementDetails = () => {
    const { notificationId } = useParams();

    const [announcement, setAnnouncement] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        const fetchAnnouncement = async () => {
            try {
                setLoading(true);
                setError("");

                const response = await api.get(
                    `/api/v1/notifications/manage/${notificationId}`
                );

                setAnnouncement(response.data);
            } catch (err) {
                setError(
                    err.response?.data?.message ||
                    "Failed to load announcement details."
                );
            } finally {
                setLoading(false);
            }
        };

        fetchAnnouncement();
    }, [notificationId]);

    const formatDate = (date) => {
        if (!date) return "Not available";
        return new Date(date).toLocaleString();
    };

    if (loading) {
        return (
            <div className="flex justify-center py-16">
                <RefreshCw className="animate-spin text-blue-600" size={28} />
            </div>
        );
    }

    if (error || !announcement) {
        return (
            <div className="rounded-xl bg-white p-8 text-center shadow-sm">
                <p className="text-red-600">
                    {error || "Announcement not found."}
                </p>
                <Link
                    to="/teacher/announcements"
                    className="mt-4 inline-block text-sm font-medium text-blue-600"
                >
                    Back to announcements
                </Link>
            </div>
        );
    }

    return (
        <div className="mx-auto max-w-4xl space-y-6">
            <Link
                to="/teacher/announcements"
                className="inline-flex items-center gap-2 text-sm text-gray-600 hover:text-blue-600"
            >
                <ArrowLeft size={17} />
                Back to Announcements
            </Link>

            <div className="rounded-xl bg-white p-6 shadow-sm sm:p-8">
                <div className="mb-5 flex flex-wrap gap-2">
                    <span className="rounded-full bg-gray-100 px-3 py-1 text-xs font-medium text-gray-700">
                        {announcement.category?.replaceAll("_", " ")}
                    </span>

                    <span className="rounded-full bg-blue-100 px-3 py-1 text-xs font-medium text-blue-700">
                        {announcement.priority}
                    </span>

                    <span className="rounded-full bg-purple-100 px-3 py-1 text-xs font-medium text-purple-700">
                        {announcement.status}
                    </span>
                </div>

                <h1 className="text-2xl font-semibold text-gray-800">
                    {announcement.title}
                </h1>

                <p className="mt-5 whitespace-pre-wrap leading-7 text-gray-700">
                    {announcement.message}
                </p>

                <div className="mt-8 grid gap-4 border-t border-gray-100 pt-6 sm:grid-cols-2">
                    <div>
                        <p className="text-xs text-gray-500">Created by</p>
                        <p className="mt-1 font-medium text-gray-800">
                            {announcement.createdByName || "Not available"}
                        </p>
                    </div>

                    <div>
                        <p className="text-xs text-gray-500">Created at</p>
                        <p className="mt-1 font-medium text-gray-800">
                            {formatDate(announcement.createdAt)}
                        </p>
                    </div>

                    <div>
                        <p className="text-xs text-gray-500">Published at</p>
                        <p className="mt-1 font-medium text-gray-800">
                            {formatDate(announcement.publishedAt)}
                        </p>
                    </div>

                    <div>
                        <p className="text-xs text-gray-500">Expires at</p>
                        <p className="mt-1 font-medium text-gray-800">
                            {formatDate(announcement.expiresAt)}
                        </p>
                    </div>
                </div>
            </div>

            <div className="rounded-xl bg-white p-6 shadow-sm">
                <h2 className="mb-4 font-semibold text-gray-800">
                    Recipients
                </h2>

                {announcement.targets?.length ? (
                    <div className="space-y-3">
                        {announcement.targets.map((target, index) => (
                            <div
                                key={target.id ?? index}
                                className="rounded-lg border border-gray-200 p-4"
                            >
                                <p className="font-medium text-gray-800">
                                    {target.recipientRole}
                                </p>

                                <div className="mt-2 flex flex-wrap gap-2 text-sm text-gray-600">
                                    {target.branch && (
                                        <span>Branch: {target.branch}</span>
                                    )}
                                    {target.year && (
                                        <span>Year: {target.year}</span>
                                    )}
                                    {target.section && (
                                        <span>Section: {target.section}</span>
                                    )}
                                    {target.batch && (
                                        <span>Batch: {target.batch}</span>
                                    )}
                                </div>
                            </div>
                        ))}
                    </div>
                ) : (
                    <p className="text-sm text-gray-500">
                        No recipient information available.
                    </p>
                )}
            </div>

            <div className="rounded-xl bg-white p-6 shadow-sm">
                <h2 className="mb-4 flex items-center gap-2 font-semibold text-gray-800">
                    <Paperclip size={18} />
                    Attachments
                </h2>

                {announcement.attachments?.length ? (
                    <div className="space-y-3">
                        {announcement.attachments.map((attachment, index) => (
                            <div
                                key={attachment.id ?? index}
                                className="flex items-center gap-3 rounded-lg border border-gray-200 p-3"
                            >
                                <Paperclip size={18} className="text-gray-500" />
                                <div>
                                    <p className="text-sm font-medium text-gray-800">
                                        {attachment.fileName || attachment.originalFileName || `Attachment ${index + 1}`}
                                    </p>
                                    <p className="text-xs text-gray-500">
                                        {attachment.contentType || "File"}
                                    </p>
                                </div>
                            </div>
                        ))}
                    </div>
                ) : (
                    <p className="text-sm text-gray-500">
                        No attachments.
                    </p>
                )}
            </div>
        </div>
    );
};

export default AnnouncementDetails;
