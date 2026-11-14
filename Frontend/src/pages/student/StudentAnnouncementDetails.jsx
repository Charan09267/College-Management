
import { Link, useLocation, useNavigate } from "react-router-dom";
import { ArrowLeft, Megaphone, Paperclip } from "lucide-react";

const StudentAnnouncementDetails = () => {
    const { state } = useLocation();
    const navigate = useNavigate();

    const notification = state?.notification;

    if (!notification) {
        return (
            <div className="rounded-xl bg-white p-8 text-center shadow-sm">
                <p className="text-gray-600">
                    Announcement details are unavailable.
                </p>
                <Link
                    to="/student/announcements"
                    className="mt-4 inline-block text-blue-600 hover:underline"
                >
                    Back to announcements
                </Link>
            </div>
        );
    }

    const urgent = notification.priority === "URGENT";

    return (
        <div className="max-w-4xl space-y-6">
            <button
                onClick={() => navigate("/student/announcements")}
                className="flex items-center gap-2 text-sm text-gray-600 hover:text-blue-600"
            >
                <ArrowLeft size={17} />
                Back to announcements
            </button>

            <article className="rounded-xl border border-gray-200 bg-white p-6 shadow-sm">
                <div className="flex items-start gap-3">
                    <div className={`rounded-lg p-3 ${
                        urgent ? "bg-red-50" : "bg-blue-50"
                    }`}>
                        <Megaphone
                            size={22}
                            className={urgent ? "text-red-600" : "text-blue-600"}
                        />
                    </div>

                    <div className="min-w-0 flex-1">
                        <div className="flex flex-wrap gap-2">
                            <span className="rounded-full bg-blue-50 px-3 py-1 text-xs text-blue-700">
                                {notification.category}
                            </span>

                            <span className={`rounded-full px-3 py-1 text-xs ${
                                urgent
                                    ? "bg-red-50 text-red-700"
                                    : "bg-gray-100 text-gray-700"
                            }`}>
                                {notification.priority}
                            </span>

                            <span className="rounded-full bg-gray-100 px-3 py-1 text-xs text-gray-700">
                                {notification.read ? "Read" : "Unread"}
                            </span>
                        </div>

                        <h1 className="mt-4 text-2xl font-bold text-slate-800">
                            {notification.title}
                        </h1>

                        <p className="mt-2 text-sm text-gray-500">
                            Published by {notification.createdByName}
                        </p>

                        <p className="mt-1 text-xs text-gray-400">
                            {notification.publishedAt
                                ? new Date(notification.publishedAt).toLocaleString()
                                : "Publication date unavailable"}
                        </p>
                    </div>
                </div>

                <div className="my-6 border-t border-gray-100" />

                <div className="whitespace-pre-wrap leading-7 text-gray-700">
                    {notification.message}
                </div>

                {notification.attachments?.length > 0 && (
                    <div className="mt-8 border-t border-gray-100 pt-5">
                        <h2 className="mb-4 flex items-center gap-2 font-semibold text-gray-800">
                            <Paperclip size={18} />
                            Attachments
                        </h2>

                        <div className="space-y-3">
                            {notification.attachments.map((attachment) => (
                                <div
                                    key={attachment.id}
                                    className="flex items-center justify-between gap-3 rounded-lg border border-gray-200 p-4"
                                >
                                    <div className="min-w-0">
                                        <p className="truncate text-sm font-medium text-gray-800">
                                            {attachment.fileName}
                                        </p>

                                        <p className="mt-1 text-xs text-gray-500">
                                            {attachment.contentType} ·{" "}
                                            {attachment.fileSize != null
                                                ? `${(attachment.fileSize / 1024).toFixed(1)} KB`
                                                : "Size unavailable"}
                                        </p>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                )}
            </article>
        </div>
    );
};

export default StudentAnnouncementDetails;
