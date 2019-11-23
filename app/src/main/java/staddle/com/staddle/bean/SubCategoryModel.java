package staddle.com.staddle.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class SubCategoryModel implements Parcelable {

    private String sid;

    private String Category;

    private String sub_name;

    private String cid;

    protected SubCategoryModel(Parcel in) {
        sid = in.readString();
        Category = in.readString();
        sub_name = in.readString();
        cid = in.readString();
    }

    public static final Creator<SubCategoryModel> CREATOR = new Creator<SubCategoryModel>() {
        @Override
        public SubCategoryModel createFromParcel(Parcel in) {
            return new SubCategoryModel(in);
        }

        @Override
        public SubCategoryModel[] newArray(int size) {
            return new SubCategoryModel[size];
        }
    };

    //========================
    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(sid);
        parcel.writeString(Category);
        parcel.writeString(sub_name);
        parcel.writeString(cid);
    }
}
