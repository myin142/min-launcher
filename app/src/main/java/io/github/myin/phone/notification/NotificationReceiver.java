package io.github.myin.phone.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.annimon.stream.function.Consumer;

public class NotificationReceiver extends BroadcastReceiver {

    private Consumer<NotificationChange> onNotificationChange;

    public void setOnNotificationChange(Consumer<NotificationChange> onNotificationChange) {
        this.onNotificationChange = onNotificationChange;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }

}
