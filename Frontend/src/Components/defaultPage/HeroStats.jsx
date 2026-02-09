import {
  FaBookOpen,
  FaChalkboardTeacher,
  FaUserGraduate,
} from "react-icons/fa";
import { TbPercentage90 } from "react-icons/tb";

const HeroStats = () => {
  return (
    <div className="absolute bottom-0 left-0 w-full translate-y-1/2">
      <div className="max-w-6xl mx-auto bg-white rounded-xl shadow-lg px-6 py-6">
        <div className="grid grid-cols-2 md:grid-cols-4 text-center">
          {/* Item 1 */}
          <div className="flex flex-col items-center gap-2 px-4 border-dashed md:border-r">
            <FaBookOpen className="text-3xl text-blue-600" />
            <h3 className="text-2xl font-bold text-blue-600">120+</h3>
            <p className="text-sm text-gray-600">Programs Offered</p>
          </div>

          {/* Item 2 */}
          <div className="flex flex-col items-center gap-2 px-4 border-dashed md:border-r">
            <FaUserGraduate className="text-3xl text-blue-600" />
            <h3 className="text-2xl font-bold text-blue-600">15k+</h3>
            <p className="text-sm text-gray-600">Students Enrolled</p>
          </div>

          {/* Item 3 */}
          <div className="flex flex-col items-center gap-2 px-4 border-dashed md:border-r">
            <FaChalkboardTeacher className="text-3xl text-blue-600" />
            <h3 className="text-2xl font-bold text-blue-600">300+</h3>
            <p className="text-sm text-gray-600">Faculty Members</p>
          </div>

          {/* Item 4 */}
          <div className="flex flex-col items-center gap-2 px-4">
            <TbPercentage90 className="text-3xl text-blue-600" />
            <h3 className="text-2xl font-bold text-blue-600">95%</h3>
            <p className="text-sm text-gray-600">Career Placement</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default HeroStats;
