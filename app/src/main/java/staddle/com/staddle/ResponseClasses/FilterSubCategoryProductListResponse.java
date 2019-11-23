package staddle.com.staddle.ResponseClasses;

import java.util.ArrayList;

import staddle.com.staddle.bean.FilterSubCategoryProductListModel;

public class FilterSubCategoryProductListResponse {
    private String status;
    private String message;
    private ArrayList<FilterSubCategoryProductListModel> info;

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

    public ArrayList<FilterSubCategoryProductListModel> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<FilterSubCategoryProductListModel> info) {
        this.info = info;
    }
}
