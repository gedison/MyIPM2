package clemson.edu.myipm2.downloader;

import java.util.List;

import clemson.edu.myipm2.downloader.entity.Notification;

public interface OnNotificationTaskCompleteListener {
    void onNotificationTaskComplete(List<Notification> notifications);
}
