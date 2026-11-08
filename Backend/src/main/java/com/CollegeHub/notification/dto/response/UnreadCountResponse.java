package com.CollegeHub.notification.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnreadCountResponse {

    private long unreadCount;
}