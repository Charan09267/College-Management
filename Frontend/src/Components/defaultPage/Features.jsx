import { FaUniversity, FaBookReader, FaUsers } from "react-icons/fa";

const Features = () => {
  return (
    <section className="py-24 bg-gray-50">
      <div className="max-w-7xl mx-auto px-6">
        {/* Section Title */}
        <div className="text-center mb-12">
          <h2 className="text-3xl font-bold text-gray-800">
            Discover Our University
          </h2>
          <p className="text-gray-600 mt-2">
            Everything you need to shape your future
          </p>
        </div>

        {/* Cards */}
        <div className="grid md:grid-cols-3 gap-8">
          {/* Card 1 */}
          <div className="bg-white p-8 rounded-xl shadow hover:shadow-lg transition text-center">
            <div className="w-16 h-16 mx-auto mb-4 flex items-center justify-center rounded-full bg-blue-100">
              <FaUniversity className="text-3xl text-blue-600" />
            </div>
            <h3 className="text-xl font-semibold text-blue-600 mb-3">
              Admissions
            </h3>
            <p className="text-gray-600">
              Simple and transparent admission process with full guidance.
            </p>
          </div>

          {/* Card 2 */}
          <div className="bg-white p-8 rounded-xl shadow hover:shadow-lg transition text-center">
            <div className="w-16 h-16 mx-auto mb-4 flex items-center justify-center rounded-full bg-blue-100">
              <FaBookReader className="text-3xl text-blue-600" />
            </div>
            <h3 className="text-xl font-semibold text-blue-600 mb-3">
              Academics
            </h3>
            <p className="text-gray-600">
              Industry-focused curriculum with experienced faculty members.
            </p>
          </div>

          {/* Card 3 */}
          <div className="bg-white p-8 rounded-xl shadow hover:shadow-lg transition text-center">
            <div className="w-16 h-16 mx-auto mb-4 flex items-center justify-center rounded-full bg-blue-100">
              <FaUsers className="text-3xl text-blue-600" />
            </div>
            <h3 className="text-xl font-semibold text-blue-600 mb-3">
              Campus Life
            </h3>
            <p className="text-gray-600">
              Modern infrastructure, clubs, sports, and vibrant student life.
            </p>
          </div>
        </div>
      </div>
    </section>
  );
};

export default Features;
