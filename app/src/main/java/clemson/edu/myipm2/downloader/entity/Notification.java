package clemson.edu.myipm2.downloader.entity;

public class Notification {
    private String id;
    private String title;
    private String notificationTypeId;
    private String content;

    public Notification(String id, String notificationTypeId, String title, String content){
        this.id = id;
        this.title = title;
        this.content = content;
        this.notificationTypeId = notificationTypeId;
    }

    public int getId(){
        return Integer.parseInt(id);
    }

    public String getTitle(){
        return title;
    }

    public String getContent(){
        return content;
    }

    public int getType(){
        return (notificationTypeId.equals("1")) ?  1 : 2;
    }

    public String toString(){
        return id+" "+title+" "+content;
    }
}
