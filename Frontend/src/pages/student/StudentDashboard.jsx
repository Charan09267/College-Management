
import { AlertTriangle, Megaphone, Video } from "lucide-react";

const StudentDashboard = () => {
    return (
        <div className="space-y-6">

            <div>
                <h1 className="text-2xl font-bold text-slate-800">
                    Welcome back!
                </h1>
                <p className="text-gray-500 mt-1">
                    Here's what's happening in your learning space.
                </p>
            </div>

            <div className="grid grid-cols-1 xl:grid-cols-3 gap-6">

                {/* Main section */}
                <section className="xl:col-span-2 bg-white rounded-xl p-6 shadow-sm">
                    <div className="flex items-center gap-2 mb-5">
                        <Video className="text-blue-600" size={22} />
                        <h2 className="text-lg font-semibold text-slate-800">
                            Recent Recordings
                        </h2>
                    </div>

                    <div className="text-center py-12 text-gray-500">
                        <Video size={36} className="mx-auto mb-3 text-gray-400" />
                        <p>No recent recordings available.</p>
                    </div>
                </section>

                {/* Announcements section */}
                <section className="space-y-5">

                    {/* Emergency announcements */}
                    <div className="bg-white rounded-xl p-5 shadow-sm">
                        <div className="flex items-center gap-2 mb-4">
                            <AlertTriangle className="text-red-600" size={21} />
                            <h2 className="font-semibold text-slate-800">
                                Emergency Announcements
                            </h2>
                        </div>

                        <div className="border border-red-200 bg-red-50 rounded-lg p-4">
                            <p className="text-sm text-gray-500">
                                No emergency announcements.
                            </p>
                        </div>
                    </div>

                    {/* Regular announcements */}
                    <div className="bg-white rounded-xl p-5 shadow-sm">
                        <div className="flex items-center gap-2 mb-4">
                            <Megaphone className="text-blue-600" size={21} />
                            <h2 className="font-semibold text-slate-800">
                                Regular Announcements
                            </h2>
                        </div>

                        <div className="text-center py-8 text-gray-500">
                            <Megaphone size={30} className="mx-auto mb-3 text-gray-400" />
                            <p className="text-sm">
                                No new announcements.
                            </p>
                        </div>
                    </div>

                </section>
            </div>
        </div>
    );
};

export default StudentDashboard;
