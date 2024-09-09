package com.example.notification.Services;

import com.example.notification.Models.Notification;
import com.example.notification.Repos.NotificationRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationListener {

    private final NotificationRepository notificationRepository;

    public NotificationListener(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @RabbitListener(queues = "firstStepQueue")
    public void handleNotificationMessage(String message) {
        System.out.println("Received notification: " + message);

        Notification notification = new Notification();
        notification.setMessage(message);

        notificationRepository.save(notification);
        System.out.println("Notification saved to MongoDB");
    }

}
