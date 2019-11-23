package staddle.com.staddle.ResponseClasses;

import java.util.ArrayList;

import staddle.com.staddle.bean.VendorListModel;
import staddle.com.staddle.bean.VendorOfferListModel;

public class VendorOfferListResponse {

    private String message;
    private String status;
    private ArrayList<VendorOfferListModel> info;

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

    public ArrayList<VendorOfferListModel> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<VendorOfferListModel> info) {
        this.info = info;
    }
}
