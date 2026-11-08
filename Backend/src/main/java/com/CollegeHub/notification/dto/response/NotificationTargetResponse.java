package com.CollegeHub.notification.dto.response;

import com.CollegeHub.notification.enums.RecipientRole;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTargetResponse {

    private RecipientRole recipientRole;

    private String branch;

    private Integer year;

    private String section;

    private String batch;
}
