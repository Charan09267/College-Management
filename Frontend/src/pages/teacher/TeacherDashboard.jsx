
import { Video, Megaphone, PlusCircle } from "lucide-react";
import { Link } from "react-router-dom";

const TeacherDashboard = () => {
    return (
        <div className="space-y-6">
            <div>
                <h1 className="text-2xl font-bold text-slate-800">
                    Welcome back, Teacher!
                </h1>
                <p className="text-gray-500 mt-1">
                    Manage your recordings and announcements.
                </p>
            </div>

            {/* Quick actions */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-5">
                <Link
                    to="/teacher/recordings"
                    className="bg-white p-6 rounded-xl shadow-sm hover:shadow-md transition"
                >
                    <div className="flex items-center gap-3">
                        <Video className="text-blue-600" size={26} />
                        <div>
                            <h2 className="font-semibold text-slate-800">
                                Manage Recordings
                            </h2>
                            <p className="text-sm text-gray-500 mt-1">
                                Upload and manage class recordings.
                            </p>
                        </div>
                    </div>
                </Link>

                <Link
                    to="/teacher/announcements"
                    className="bg-white p-6 rounded-xl shadow-sm hover:shadow-md transition"
                >
                    <div className="flex items-center gap-3">
                        <Megaphone className="text-blue-600" size={26} />
                        <div>
                            <h2 className="font-semibold text-slate-800">
                                Manage Announcements
                            </h2>
                            <p className="text-sm text-gray-500 mt-1">
                                Create and manage college announcements.
                            </p>
                        </div>
                    </div>
                </Link>
            </div>

            {/* Recent activity */}
            <section className="bg-white rounded-xl p-6 shadow-sm">
                <h2 className="text-lg font-semibold text-slate-800 mb-4">
                    Recent Activity
                </h2>

                <div className="text-center py-10 text-gray-500">
                    <PlusCircle size={32} className="mx-auto mb-3 text-gray-400" />
                    <p>No recent activity available.</p>
                </div>
            </section>
        </div>
    );
};

export default TeacherDashboard;
