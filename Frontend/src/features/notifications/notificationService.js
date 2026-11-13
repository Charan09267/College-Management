import api from "../../services/api";

// Student notification APIs
export const getNotifications = async (page = 0, size = 10) => {
    const response = await api.get("/api/v1/student/notifications", {
        params: { page, size },
    });
    return response.data;
};

export const getUnreadCount = async () => {
    const response = await api.get(
        "/api/v1/student/notifications/unread-count"
    );
    return response.data;
};

export const markNotificationAsRead = async (notificationId) => {
    const response = await api.patch(
        `/api/v1/student/notifications/${notificationId}/read`
    );
    return response.data;
};

export const markAllNotificationsAsRead = async () => {
    const response = await api.patch(
        "/api/v1/student/notifications/read-all"
    );
    return response.data;
};

// Teacher notification creation
export const createTeacherNotification = async (request, attachments = []) => {
    const formData = new FormData();

    formData.append(
        "request",
        new Blob([JSON.stringify(request)], {
            type: "application/json",
        })
    );

    attachments.forEach((file) => {
        formData.append("attachments", file);
    });

    const response = await api.post(
        "/api/v1/teacher/notifications",
        formData
    );

    return response.data;
};