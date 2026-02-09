import HeroStats from "./HeroStats";

const Hero = () => {
  return (
    <section
      className="h-screen bg-cover bg-center relative"
      style={{ backgroundImage: "url('/College Background.jpg')" }}
    >
      {/* Overlay */}
      <div className="absolute inset-0 bg-black/50"></div>

      {/* Content */}
      <div className="relative z-10 h-full flex items-center ">
        <div className="max-w-7xl mx-auto px-6 text-white">
          <h1 className="text-4xl md:text-5xl font-bold mb-4">
            Welcome to Your Future
          </h1>
          <p className="text-lg mb-6 max-w-xl">
            Empowering Tomorrow’s Leaders Through Quality Education
          </p>

          <div className="flex gap-4">
            <button className="px-6 py-3 bg-blue-600 rounded-md hover:bg-blue-700">
              Explore Programs
            </button>
            <button className="px-6 py-3 bg-white text-blue-600 rounded-md hover:bg-gray-200">
              Schedule a Visit
            </button>
          </div>
        </div>
      </div>

      {/* Stats Section */}
      <HeroStats />
    </section>
  );
};

export default Hero;
