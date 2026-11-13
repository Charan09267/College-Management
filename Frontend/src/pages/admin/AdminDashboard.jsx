
import { Users, Megaphone, UserPlus } from "lucide-react";
import { Link } from "react-router-dom";

const AdminDashboard = () => {
    return (
        <div className="space-y-6">
            <div>
                <h1 className="text-2xl font-bold text-slate-800">
                    Welcome back, Admin!
                </h1>
                <p className="text-gray-500 mt-1">
                    Manage users and college announcements.
                </p>
            </div>

            {/* Quick actions */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-5">
                <Link
                    to="/admin/users"
                    className="bg-white p-6 rounded-xl shadow-sm hover:shadow-md transition"
                >
                    <div className="flex items-center gap-3">
                        <Users className="text-blue-600" size={26} />
                        <div>
                            <h2 className="font-semibold text-slate-800">
                                Manage Users
                            </h2>
                            <p className="text-sm text-gray-500 mt-1">
                                View and manage student and teacher accounts.
                            </p>
                        </div>
                    </div>
                </Link>

                <Link
                    to="/admin/announcements"
                    className="bg-white p-6 rounded-xl shadow-sm hover:shadow-md transition"
                >
                    <div className="flex items-center gap-3">
                        <Megaphone className="text-blue-600" size={26} />
                        <div>
                            <h2 className="font-semibold text-slate-800">
                                Manage Announcements
                            </h2>
                            <p className="text-sm text-gray-500 mt-1">
                                Publish and manage college announcements.
                            </p>
                        </div>
                    </div>
                </Link>
            </div>

            {/* Overview */}
            <section className="bg-white rounded-xl p-6 shadow-sm">
                <h2 className="text-lg font-semibold text-slate-800 mb-4">
                    Platform Overview
                </h2>

                <div className="text-center py-10 text-gray-500">
                    <UserPlus size={32} className="mx-auto mb-3 text-gray-400" />
                    <p>Platform statistics will appear here.</p>
                </div>
            </section>
        </div>
    );
};

export default AdminDashboard;
