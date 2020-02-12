package staddle.com.staddle.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FavouriteListModel implements Parcelable {
    private String business_name;
    private String cid;
    private String closing_time;
    private String commission;
    private String email;
    private String gender;
    private String image;
    private String like_status;
    private String location;
    private String mobile;
    private String opening_time;
    private String password;
    private String sid;
    private String subcat;
    private String sub_category;
    private String user_discount;
    private String id;
    private String rating;
    private String image1;
    private String image2;
    private String image3;

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getImage5() {
        return image5;
    }

    public void setImage5(String image5) {
        this.image5 = image5;
    }

    private String image4;
    private String image5;


    protected FavouriteListModel(Parcel in) {
        business_name = in.readString();
        cid = in.readString();
        closing_time = in.readString();
        commission = in.readString();
        email = in.readString();
        gender = in.readString();
        image = in.readString();
        like_status = in.readString();
        location = in.readString();
        mobile = in.readString();
        opening_time = in.readString();
        password = in.readString();
        sid = in.readString();
        subcat = in.readString();
        sub_category = in.readString();
        user_discount = in.readString();
        id = in.readString();
        rating = in.readString();
        image1 = in.readString();
        image2 = in.readString();
        image3 = in.readString();
        image4 = in.readString();
        image5 = in.readString();

    }

    public static final Creator<FavouriteListModel> CREATOR = new Creator<FavouriteListModel>() {
        @Override
        public FavouriteListModel createFromParcel(Parcel in) {
            return new FavouriteListModel(in);
        }

        @Override
        public FavouriteListModel[] newArray(int size) {
            return new FavouriteListModel[size];
        }
    };

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getClosing_time() {
        return closing_time;
    }

    public void setClosing_time(String closing_time) {
        this.closing_time = closing_time;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLike_status() {
        return like_status;
    }

    public void setLike_status(String like_status) {
        this.like_status = like_status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOpening_time() {
        return opening_time;
    }

    public void setOpening_time(String opening_time) {
        this.opening_time = opening_time;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSubcat() {
        return subcat;
    }

    public void setSubcat(String subcat) {
        this.subcat = subcat;
    }

    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public String getUser_discount() {
        return user_discount;
    }

    public void setUser_discount(String user_discount) {
        this.user_discount = user_discount;
    }

    public String getid() {
        return id;
    }

    public void setVid(String id) {
        this.id = id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(business_name);
        dest.writeString(cid);
        dest.writeString(closing_time);
        dest.writeString(commission);
        dest.writeString(email);
        dest.writeString(gender);
        dest.writeString(image);
        dest.writeString(like_status);
        dest.writeString(location);
        dest.writeString(mobile);
        dest.writeString(opening_time);
        dest.writeString(password);
        dest.writeString(sid);
        dest.writeString(subcat);
        dest.writeString(sub_category);
        dest.writeString(user_discount);
        dest.writeString(id);
        dest.writeString(rating);
        dest.writeString(image1);
        dest.writeString(image2);
        dest.writeString(image3);
        dest.writeString(image4);
        dest.writeString(image5);

    }
}