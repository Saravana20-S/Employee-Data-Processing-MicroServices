package com.employeetracker.notification.service;

import com.employeetracker.notification.dto.EmployeeCreatedEvent;
import com.employeetracker.notification.entity.Notification;
import com.employeetracker.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository
            notificationRepository;

    public Notification createNotification(
            EmployeeCreatedEvent event
    ) {

        Notification notification =
                Notification.builder()
                        .employeeId(
                                event.getEmployeeId()
                        )
                        .email(
                                event.getEmail()
                        )
                        .message(
                                "Employee "
                                        + event.getName()
                                        + " profile created successfully."
                        )
                        .status("CREATED")
                        .build();

        return notificationRepository.save(
                notification
        );
    }
}