package staddle.com.staddle.ResponseClasses;

import java.util.ArrayList;

import staddle.com.staddle.bean.MenuListModule;

public class MenuListResponse {
    private String message;
    private String status;
    ArrayList<MenuListModule> info;

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

    public ArrayList<MenuListModule> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<MenuListModule> info) {
        this.info = info;
    }
}
