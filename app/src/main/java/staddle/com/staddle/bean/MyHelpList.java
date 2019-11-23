package staddle.com.staddle.bean;

public class MyHelpList {
    private String id;
    private String sender_id;
    private String received_id;
    private String comment;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReceived_id() {
        return received_id;
    }

    public void setReceived_id(String received_id) {
        this.received_id = received_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
