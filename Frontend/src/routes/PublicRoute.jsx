
import { Navigate, Outlet } from "react-router-dom";
import { useSelector } from "react-redux";

const PublicRoute = () => {
    const { isAuthenticated, user, initialized } = useSelector(
        (state) => state.auth
    );

    if (!initialized) {
        return <div>Loading...</div>;
    }

    if (isAuthenticated) {
        return (
            <Navigate
                to={`/${user.role.toLowerCase()}/dashboard`}
                replace
            />
        );
    }

    return <Outlet />;
};

export default PublicRoute;
