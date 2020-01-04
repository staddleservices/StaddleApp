package staddle.com.staddle.bean;

public class NotificationsDataModels {


    String content;
    String status;
    public NotificationsDataModels( String content,String status) {
        this.content = content;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
