package com.CollegeHub.notification.dto.request;

import com.CollegeHub.notification.enums.NotificationCategory;
import com.CollegeHub.notification.enums.NotificationPriority;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateNotificationRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title cannot exceed 200 characters")
    private String title;

    @NotBlank(message = "Message is required")
    private String message;

    @NotNull(message = "Category is required")
    private NotificationCategory category;

    private NotificationPriority priority;

    private LocalDateTime expiresAt;

    private Map<String, Object> data;

    @NotEmpty(message = "At least one target is required")
    @Valid
    private List<NotificationTargetRequest> targets;
}