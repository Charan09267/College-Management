package com.CollegeHub.notification.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationAttachmentResponse {

    private Long id;

    private String fileName;

    private String contentType;

    private Long fileSize;

    private LocalDateTime createdAt;
}
