package staddle.com.staddle.ResponseClasses;

import java.util.ArrayList;

import staddle.com.staddle.bean.CategorySubCategoryListModel;


public class CategorySubCategoryListResponse {

    private String message;

    private String status;

    private ArrayList<CategorySubCategoryListModel> info;

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
      //====================================
    public ArrayList<CategorySubCategoryListModel> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<CategorySubCategoryListModel> info) {
        this.info = info;
    }
}
