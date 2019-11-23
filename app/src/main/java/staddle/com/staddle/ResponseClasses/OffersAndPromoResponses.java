package staddle.com.staddle.ResponseClasses;

import java.util.ArrayList;

import staddle.com.staddle.bean.OfferAndPromoModel;

public class OffersAndPromoResponses {
    private String status;
    private String message;
    private ArrayList<OfferAndPromoModel> info;

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

    public ArrayList<OfferAndPromoModel> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<OfferAndPromoModel> info) {
        this.info = info;
    }
}
