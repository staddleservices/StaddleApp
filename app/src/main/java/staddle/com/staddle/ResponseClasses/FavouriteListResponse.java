package staddle.com.staddle.ResponseClasses;

import java.util.ArrayList;

import staddle.com.staddle.bean.FavouriteListModel;

public class FavouriteListResponse {
    private String status;
    private String message;
    private ArrayList<FavouriteListModel> info;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<FavouriteListModel> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<FavouriteListModel> info) {
        this.info = info;
    }
}
