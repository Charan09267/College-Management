package com.CollegeHub.notification.dto.request;

import com.CollegeHub.notification.enums.RecipientRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTargetRequest {

    @NotNull(message = "Recipient role is required")
    private RecipientRole recipientRole;

    private String branch;

    private Integer year;

    private String section;

    private String batch;
}
