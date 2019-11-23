package staddle.com.staddle.ResponseClasses;

import java.util.ArrayList;

import staddle.com.staddle.bean.GetVendorSubCategoryMenuListModule;

public class GetVendorSubCategoryMenuListResponse {
    private String message;

    private String status;

    private ArrayList<GetVendorSubCategoryMenuListModule> info;

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

    public ArrayList<GetVendorSubCategoryMenuListModule> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<GetVendorSubCategoryMenuListModule> info) {
        this.info = info;
    }
}
