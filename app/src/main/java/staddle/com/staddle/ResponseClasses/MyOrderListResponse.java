package staddle.com.staddle.ResponseClasses;

import java.util.ArrayList;

import staddle.com.staddle.bean.MyOrderListModel;

public class MyOrderListResponse {
    private String message;

    private String status;

    private ArrayList<MyOrderListModel> info;


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

    public ArrayList<MyOrderListModel> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<MyOrderListModel> info) {
        this.info = info;
    }

}
