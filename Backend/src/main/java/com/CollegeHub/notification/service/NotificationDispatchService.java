package com.CollegeHub.notification.service;


/**
 * Defines the operation responsible for distributing a notification
 * to all eligible recipients based on its configured targeting criteria.
 */
public interface NotificationDispatchService {

    /**
     * Resolves the notification's target audience and creates
     * recipient records for the eligible users.
     *
     * @param notificationId ID of the notification to dispatch
     */
    void dispatchNotification(Long notificationId);
}
