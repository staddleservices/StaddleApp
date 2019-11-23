package staddle.com.staddle.ResponseClasses;

import java.util.ArrayList;

import staddle.com.staddle.bean.ProductListCategoryModel;

public class ProductListCategoryResponse {
    private String message;

    private String status;

    private ArrayList<ProductListCategoryModel> info;

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

    public ArrayList<ProductListCategoryModel> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<ProductListCategoryModel> info) {
        this.info = info;
    }
}
