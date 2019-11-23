package staddle.com.staddle.ResponseClasses;

import java.util.ArrayList;

import staddle.com.staddle.bean.MyHelpList;
import staddle.com.staddle.bean.MyOrderListModel;

public class MyHelpListResponse {
    private String message;

    private String status;

    private ArrayList<MyHelpList> info;

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

    public ArrayList<MyHelpList> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<MyHelpList> info) {
        this.info = info;
    }
}
