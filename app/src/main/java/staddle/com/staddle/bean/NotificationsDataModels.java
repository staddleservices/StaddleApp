package staddle.com.staddle.bean;

public class NotificationsDataModels {

    String title;
    String content;
    String code;// offers and normal information=0 and orders notification =1

    public NotificationsDataModels(String title, String content, String code) {
        this.title = title;
        this.content = content;
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
