package com.employeetracker.notification.controller;

import com.employeetracker.notification.entity.Notification;
import com.employeetracker.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository
            notificationRepository;

    @GetMapping
    public List<Notification> getAll() {

        return notificationRepository.findAll();
    }

    @GetMapping("/{id}")
    public Notification getById(
            @PathVariable Long id
    ) {

        return notificationRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Notification not found"
                        )
                );
    }
}