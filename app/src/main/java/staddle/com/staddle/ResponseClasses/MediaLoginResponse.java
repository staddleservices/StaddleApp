package staddle.com.staddle.ResponseClasses;

import java.util.ArrayList;

import staddle.com.staddle.bean.MediaInfoModule;

public class MediaLoginResponse {
    private String message;
    private String status;
    ArrayList<MediaInfoModule> info;

    // =================================================

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // =================================================

    public ArrayList<MediaInfoModule> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<MediaInfoModule> info) {
        this.info = info;
    }
}
