package staddle.com.staddle.ResponseClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CheckFavoriteResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("info")
    @Expose
    private ArrayList<Info> info = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Info> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<Info> info) {
        this.info = info;
    }

    public class Info {
        @SerializedName("fav_status")
        @Expose
        private String favStatus;

        public String getFavStatus() {
            return favStatus;
        }

        public void setFavStatus(String favStatus) {
            this.favStatus = favStatus;
        }
    }
}
