package io.github.myin.phone.notification;

import android.content.Intent;
import lombok.Getter;

@Getter
public class NotificationChange {

    private static final String COUNT = "notificationCount";

    private final long notificationCount;

    public NotificationChange(long notificationCount) {
        this.notificationCount = notificationCount;
    }

    public NotificationChange(Intent intent) {
        this.notificationCount = intent.getLongExtra(COUNT, 0);
    }

    public Intent toIntent() {
        Intent intent = new Intent(NotificationService.NOTIFICATION_CHANGE);
        intent.putExtra(COUNT, notificationCount);
        return intent;
    }
}
