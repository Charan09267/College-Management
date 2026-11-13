
import api from "../../services/api";

export const getNotifications = async (page = 0, size = 10) => {
    const response = await api.get("/api/v1/notifications", {
        params: { page, size },
    });

    return response.data;
};

export const getUnreadCount = async () => {
    const response = await api.get(
        "/api/v1/notifications/unread-count"
    );

    return response.data;
};

export const markNotificationAsRead = async (notificationId) => {
    const response = await api.patch(
        `/api/v1/notifications/${notificationId}/read`
    );

    return response.data;
};

export const markAllNotificationsAsRead = async () => {
    const response = await api.patch(
        "/api/v1/notifications/read-all"
    );

    return response.data;
};
