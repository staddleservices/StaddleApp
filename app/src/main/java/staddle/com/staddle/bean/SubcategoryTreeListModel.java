package staddle.com.staddle.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class SubcategoryTreeListModel implements Parcelable {
    private String id;
    private String cid;
    private String sub_name;

    protected SubcategoryTreeListModel(Parcel in) {
        id = in.readString();
        cid = in.readString();
        sub_name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(cid);
        dest.writeString(sub_name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SubcategoryTreeListModel> CREATOR = new Creator<SubcategoryTreeListModel>() {
        @Override
        public SubcategoryTreeListModel createFromParcel(Parcel in) {
            return new SubcategoryTreeListModel(in);
        }

        @Override
        public SubcategoryTreeListModel[] newArray(int size) {
            return new SubcategoryTreeListModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }
}
