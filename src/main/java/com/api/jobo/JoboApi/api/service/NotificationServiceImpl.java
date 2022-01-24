package com.api.jobo.JoboApi.api.service;

import com.api.jobo.JoboApi.api.domain.Models.NotificationModels;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.api.jobo.JoboApi.config.FirestoreBatchConfig.firebaseDatabase;
import static com.api.jobo.JoboApi.globals.GlobalRepo.notificationRepo;
import static com.api.jobo.JoboApi.globals.GlobalVariables.MESSAGES;


@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {



    @Override
    public NotificationModels saveNotification(NotificationModels notificationModels) {
        return notificationRepo.save(notificationModels);
    }

    @Override
    public List<NotificationModels> getUserNotifications(String uid) {
        return notificationRepo.findAll().stream().filter(i -> i.getUid() != null && i.getUid().equals(uid)).collect(Collectors.toList());
    }

    @Override
    public List<NotificationModels> getUnseenUserNotifications(String uid) {
        return notificationRepo.findAll().stream().filter(i -> i.getUid() != null && i.getUid().equals(uid) && !i.isNotified()).collect(Collectors.toList());
    }

    @Override
    public boolean postNotification(NotificationModels notificationModels) {
        try {
            if (saveNotification(notificationModels) != null) {
                log.info("Notification has been posted successfully , {}",notificationModels.getUid());
                return true;
            } else {
                log.info("Failed to post notification , {}",notificationModels.getUid());
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Failed to post notification , {}",notificationModels.getUid());
            return false;
        }
    }


    @Override
    public void deleteAllNotifications() {
        notificationRepo.deleteAll();
    }

    @Override
    public void deleteAllChats() {
        firebaseDatabase.getReference(MESSAGES).removeValue((databaseError, databaseReference) -> System.out.println("Database deleted"));
    }
}

