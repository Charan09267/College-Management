
import api from "../../services/api";

export const login = async (credentials) => {
    const response = await api.post("/login", credentials);
    return response.data;
};

export const getCurrentUser = async () => {
    const response = await api.get("/api/v1/auth/me");
    return response.data;
};

export const logout = async () => {
    const response = await api.post("/logout");
    return response.data;
};
