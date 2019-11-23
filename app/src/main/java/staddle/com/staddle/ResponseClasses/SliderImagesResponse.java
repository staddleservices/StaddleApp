package staddle.com.staddle.ResponseClasses;

import java.util.ArrayList;

import staddle.com.staddle.bean.SliderImagesModule;

public class SliderImagesResponse {
    private String message;

    private String status;

    private ArrayList<SliderImagesModule> info;

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

    public ArrayList<SliderImagesModule> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<SliderImagesModule> info) {
        this.info = info;
    }
}
