package staddle.com.staddle.ResponseClasses;

import java.util.ArrayList;

import staddle.com.staddle.bean.SubcategoryTreeListModel;

public class SubcategoryTreeListResponse {
    private String message;

    private String status;

    private ArrayList<SubcategoryTreeListModel> info;

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

    public ArrayList<SubcategoryTreeListModel> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<SubcategoryTreeListModel> info) {
        this.info = info;
    }
}
