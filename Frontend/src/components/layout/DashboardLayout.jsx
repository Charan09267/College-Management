
import { NavLink, Outlet } from "react-router-dom";
import { useSelector } from "react-redux";
import {
    LayoutDashboard,
    Video,
    Megaphone,
    UserCircle,
    Bell,
    Menu,
} from "lucide-react";
import LogoutButton from "../common/LogoutButton";

const navigation = {
    STUDENT: [
        { name: "Dashboard", path: "dashboard", icon: LayoutDashboard },
        { name: "Recordings", path: "recordings", icon: Video },
        { name: "Announcements", path: "announcements", icon: Megaphone },
        { name: "Profile", path: "profile", icon: UserCircle },
    ],
    TEACHER: [
        { name: "Dashboard", path: "dashboard", icon: LayoutDashboard },
        { name: "Recordings", path: "recordings", icon: Video },
        { name: "Announcements", path: "announcements", icon: Megaphone },
        {
            name: "Create Announcement",
            path: "announcements/create",
            icon: Megaphone,
        },
        { name: "Profile", path: "profile", icon: UserCircle },
    ],
    ADMIN: [
        { name: "Dashboard", path: "dashboard", icon: LayoutDashboard },
        { name: "Users", path: "users", icon: UserCircle },
        { name: "Announcements", path: "announcements", icon: Megaphone },
    ],
};

const DashboardLayout = () => {
    const { user } = useSelector((state) => state.auth);

    const role = user?.role;
    const menuItems = navigation[role] || [];

    const basePath = `/${role?.toLowerCase()}`;

    return (
        <div className="min-h-screen bg-gray-100 flex">

            {/* Sidebar */}
            <aside className="w-64 bg-slate-900 text-white flex flex-col">
                <div className="px-6 py-5 border-b border-slate-700">
                    <h1 className="text-xl font-bold">
                        Lecture Platform
                    </h1>
                </div>

                <nav className="flex-1 p-4 space-y-2">
                    {menuItems.map((item) => {
                        const Icon = item.icon;

                        return (
                            <NavLink
                                key={item.path}
                                to={`${basePath}/${item.path}`}
                                end={item.path === "dashboard"}
                                className={({ isActive }) =>
                                    `flex items-center gap-3 px-4 py-3 rounded-lg transition ${
                                        isActive
                                            ? "bg-blue-600 text-white"
                                            : "text-slate-300 hover:bg-slate-800"
                                    }`
                                }
                            >
                                <Icon size={20} />
                                <span>{item.name}</span>
                            </NavLink>
                        );
                    })}
                </nav>

                <div className="p-4 border-t border-slate-700">
                    <LogoutButton />
                </div>
            </aside>

            {/* Main area */}
            <div className="flex-1 min-w-0">

                {/* Header */}
                <header className="h-16 bg-white border-b flex items-center justify-between px-6">
                    <button className="text-gray-600">
                        <Menu size={22} />
                    </button>

                    <div className="flex items-center gap-5">
                        <button className="relative text-gray-600">
                            <Bell size={22} />
                        </button>

                        <div className="text-right">
                            <p className="text-sm font-semibold">
                                {user?.email}
                            </p>
                            <p className="text-xs text-gray-500">
                                {role}
                            </p>
                        </div>
                    </div>
                </header>

                {/* Page content */}
                <main className="p-6">
                    <Outlet />
                </main>

            </div>
        </div>
    );
};

export default DashboardLayout;
