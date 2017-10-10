package clemson.edu.myipm.downloader;

import java.util.List;

import clemson.edu.myipm.downloader.entity.Notification;

public interface OnNotificationTaskCompleteListener {
    void onNotificationTaskComplete(List<Notification> notifications);
}
