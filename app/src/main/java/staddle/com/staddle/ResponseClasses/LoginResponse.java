package staddle.com.staddle.ResponseClasses;

import java.util.ArrayList;

import staddle.com.staddle.bean.LoginInfoModel;

public class LoginResponse {

    private String status;

    private String message;

    private ArrayList<LoginInfoModel>  info;
   // =============================================
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

    public ArrayList<LoginInfoModel> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<LoginInfoModel> info) {
        this.info = info;
    }
}
