import { Navigate, useNavigate } from "react-router-dom";

const Navbar = () => {
  const navigate = useNavigate();
  return (
    <nav className="fixed top-0 w-full z-50 bg-black/60 backdrop-blur-md">
      <div className="max-w-7xl mx-auto px-6 py-4 flex items-center justify-between text-white">
        {/* Logo */}
        <div className="text-xl font-bold">
          Narsimha Reddy Engineering College
        </div>

        {/* Links */}
        <ul className="hidden md:flex gap-6 text-sm">
          <li className="cursor-pointer hover:text-blue-400">About</li>
          <li className="cursor-pointer hover:text-blue-400">Academics</li>
          <li className="cursor-pointer hover:text-blue-400">Admissions</li>
          <li className="cursor-pointer hover:text-blue-400">Campus Life</li>
          <li className="cursor-pointer hover:text-blue-400">Contact</li>
        </ul>

        {/* Single Login Button */}
        <div>
          <button
            className="px-5 py-2 bg-linear-to-r from-blue-500 to-purple-600 text-white font-semibold rounded-md shadow-lg hover:scale-105 transition-transform duration-200 cursor-auto"
            onClick={() => navigate("/login")}
          >
            Login
          </button>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
