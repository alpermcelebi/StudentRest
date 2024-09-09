package com.example.notification.Services;

import com.example.notification.Models.Notification;
import com.example.notification.Repos.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }
    public List<Notification> getAllNotification(){
        return notificationRepository.findAll();
    }
}
