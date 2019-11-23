package staddle.com.staddle.ResponseClasses;

import java.util.ArrayList;

import staddle.com.staddle.bean.UserInfoModule;

public class UserInfoResponse {
    private String message;
    private String status;
    private ArrayList<UserInfoModule> info;

    // ====================

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
    // ==========================

    public ArrayList<UserInfoModule> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<UserInfoModule> info) {
        this.info = info;
    }
}
