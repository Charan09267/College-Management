
import { useState } from "react";
import { useNavigate } from "react-router-dom";

import {
    createTeacherNotification,
    createAdminNotification,
} from "../../features/notifications/notificationService";

const categories = [
    "GENERAL",
    "HOMEWORK",
    "ASSIGNMENT",
    "EXAM",
    "RESULT",
    "EVENT",
    "HOLIDAY",
    "TIMETABLE",
    "CLASS_UPDATE",
    "RECORDING",
    "PLACEMENT",
    "IMPORTANT_NOTICE",
];

const priorities = ["LOW", "NORMAL", "HIGH", "URGENT"];
const recipientRoles = ["STUDENT", "TEACHER", "ALL"];

const CreateAnnouncement = ({ role = "TEACHER" }) => {
    const navigate = useNavigate();

    const [form, setForm] = useState({
        title: "",
        message: "",
        category: "GENERAL",
        priority: "NORMAL",
        expiresAt: "",
    });

    const [targets, setTargets] = useState([
        {
            recipientRole: "STUDENT",
            branch: "",
            year: "",
            section: "",
            batch: "",
        },
    ]);

    const [attachments, setAttachments] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const handleFormChange = (event) => {
        const { name, value } = event.target;
        setForm((previous) => ({ ...previous, [name]: value }));
    };

    const handleTargetChange = (index, event) => {
        const { name, value } = event.target;

        setTargets((previous) =>
            previous.map((target, i) =>
                i === index ? { ...target, [name]: value } : target
            )
        );
    };

    const addTarget = () => {
        setTargets((previous) => [
            ...previous,
            {
                recipientRole: "STUDENT",
                branch: "",
                year: "",
                section: "",
                batch: "",
            },
        ]);
    };

    const removeTarget = (index) => {
        setTargets((previous) =>
            previous.filter((_, i) => i !== index)
        );
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        setError("");

        const request = {
            ...form,
            expiresAt: form.expiresAt || null,
            targets: targets.map((target) => ({
                recipientRole: target.recipientRole,
                branch: target.branch || null,
                year: target.year ? Number(target.year) : null,
                section: target.section || null,
                batch: target.batch || null,
            })),
        };

        try {
            setLoading(true);
            if (role === "ADMIN") {
    await createAdminNotification(request, attachments);
    navigate("/admin/announcements");
} else {
    await createTeacherNotification(request, attachments);
    navigate("/teacher/announcements");
}
        } catch (err) {
            setError(
                err.response?.data?.message ||
                "Failed to create announcement."
            );
        } finally {
            setLoading(false);
        }
    };

    const inputClass =
        "w-full rounded-lg border border-gray-300 px-3 py-2 outline-none focus:border-blue-500";

    return (
        <div className="mx-auto max-w-4xl">
            <div className="mb-6">
                <h1 className="text-2xl font-semibold text-gray-800">
                    Create Announcement
                </h1>
                <p className="mt-1 text-sm text-gray-500">
                    Share an announcement with your recipients.
                </p>
            </div>

            <form onSubmit={handleSubmit} className="space-y-6">
                <section className="space-y-4 rounded-xl bg-white p-6 shadow-sm">
                    <div>
                        <label className="mb-1 block text-sm font-medium">
                            Title
                        </label>
                        <input
                            name="title"
                            value={form.title}
                            onChange={handleFormChange}
                            maxLength={200}
                            required
                            className={inputClass}
                        />
                    </div>

                    <div>
                        <label className="mb-1 block text-sm font-medium">
                            Message
                        </label>
                        <textarea
                            name="message"
                            value={form.message}
                            onChange={handleFormChange}
                            rows={5}
                            required
                            className={inputClass}
                        />
                    </div>

                    <div className="grid gap-4 sm:grid-cols-2">
                        <div>
                            <label className="mb-1 block text-sm font-medium">
                                Category
                            </label>
                            <select
                                name="category"
                                value={form.category}
                                onChange={handleFormChange}
                                className={inputClass}
                            >
                                {categories.map((category) => (
                                    <option key={category} value={category}>
                                        {category.replaceAll("_", " ")}
                                    </option>
                                ))}
                            </select>
                        </div>

                        <div>
                            <label className="mb-1 block text-sm font-medium">
                                Priority
                            </label>
                            <select
                                name="priority"
                                value={form.priority}
                                onChange={handleFormChange}
                                className={inputClass}
                            >
                                {priorities.map((priority) => (
                                    <option key={priority} value={priority}>
                                        {priority}
                                    </option>
                                ))}
                            </select>
                        </div>
                    </div>

                    <div>
                        <label className="mb-1 block text-sm font-medium">
                            Expiry date and time (optional)
                        </label>
                        <input
                            type="datetime-local"
                            name="expiresAt"
                            value={form.expiresAt}
                            onChange={handleFormChange}
                            className={inputClass}
                        />
                    </div>

                    <div>
                        <label className="mb-1 block text-sm font-medium">
                            Attachments (optional)
                        </label>
                        <input
                            type="file"
                            multiple
                            onChange={(event) =>
                                setAttachments(Array.from(event.target.files || []))
                            }
                            className="w-full text-sm"
                        />
                    </div>
                </section>

                <section className="space-y-4 rounded-xl bg-white p-6 shadow-sm">
                    <div className="flex items-center justify-between">
                        <h2 className="font-semibold text-gray-800">
                            Recipients
                        </h2>
                        <button
                            type="button"
                            onClick={addTarget}
                            className="text-sm font-medium text-blue-600 hover:text-blue-700"
                        >
                            + Add recipient
                        </button>
                    </div>

                    {targets.map((target, index) => (
                        <div
                            key={index}
                            className="space-y-3 rounded-lg border border-gray-200 p-4"
                        >
                            <div className="flex items-center justify-between">
                                <h3 className="text-sm font-medium">
                                    Recipient {index + 1}
                                </h3>
                                {targets.length > 1 && (
                                    <button
                                        type="button"
                                        onClick={() => removeTarget(index)}
                                        className="text-sm text-red-600"
                                    >
                                        Remove
                                    </button>
                                )}
                            </div>

                            <div className="grid gap-3 sm:grid-cols-2">
                                <select
                                    name="recipientRole"
                                    value={target.recipientRole}
                                    onChange={(event) =>
                                        handleTargetChange(index, event)
                                    }
                                    className={inputClass}
                                >
                                    {recipientRoles.map((role) => (
                                        <option key={role} value={role}>
                                            {role}
                                        </option>
                                    ))}
                                </select>

                                <input
                                    name="branch"
                                    value={target.branch}
                                    onChange={(event) =>
                                        handleTargetChange(index, event)
                                    }
                                    placeholder="Branch (optional)"
                                    className={inputClass}
                                />

                                <input
                                    type="number"
                                    name="year"
                                    value={target.year}
                                    onChange={(event) =>
                                        handleTargetChange(index, event)
                                    }
                                    placeholder="Year (optional)"
                                    min="1"
                                    className={inputClass}
                                />

                                <input
                                    name="section"
                                    value={target.section}
                                    onChange={(event) =>
                                        handleTargetChange(index, event)
                                    }
                                    placeholder="Section (optional)"
                                    className={inputClass}
                                />

                                <input
                                    name="batch"
                                    value={target.batch}
                                    onChange={(event) =>
                                        handleTargetChange(index, event)
                                    }
                                    placeholder="Batch (optional)"
                                    className={inputClass}
                                />
                            </div>
                        </div>
                    ))}
                </section>

                {error && (
                    <p className="text-sm text-red-600">{error}</p>
                )}

                <div className="flex justify-end gap-3">
                    <button
                        type="button"
                        onClick={() => navigate(-1)}
                        className="rounded-lg border border-gray-300 px-5 py-2 text-sm"
                    >
                        Cancel
                    </button>

                    <button
                        type="submit"
                        disabled={loading}
                        className="rounded-lg bg-blue-600 px-5 py-2 text-sm font-medium text-white hover:bg-blue-700 disabled:opacity-60"
                    >
                        {loading ? "Publishing..." : "Publish Announcement"}
                    </button>
                </div>
            </form>
        </div>
    );
};

export default CreateAnnouncement;
