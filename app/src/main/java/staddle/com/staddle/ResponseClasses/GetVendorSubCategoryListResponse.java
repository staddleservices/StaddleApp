package staddle.com.staddle.ResponseClasses;

import java.util.ArrayList;

import staddle.com.staddle.bean.GetVendorSubCategoryListModule;

public class GetVendorSubCategoryListResponse {
    private String message;

    private String status;

    private ArrayList<GetVendorSubCategoryListModule> info;

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

    public ArrayList<GetVendorSubCategoryListModule> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<GetVendorSubCategoryListModule> info) {
        this.info = info;
    }
}

