package io.github.myin.phone.notification;

import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class NotificationService extends NotificationListenerService {

    public static final String NOTIFICATION_CHANGE = "io.github.myin.phone.NOTIFICATION_CHANGE";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        onNotificationChange();
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        onNotificationChange();
    }

    private void onNotificationChange() {
        Intent intent = new NotificationChange(getActiveNotifications().length).toIntent();
        sendBroadcast(intent);
    }
}
