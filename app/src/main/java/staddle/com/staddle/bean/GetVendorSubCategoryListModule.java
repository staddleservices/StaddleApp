package staddle.com.staddle.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class GetVendorSubCategoryListModule implements Parcelable {
    private String id;
    private String name;
    private String vid;

    protected GetVendorSubCategoryListModule(Parcel in) {
        id = in.readString();
        name = in.readString();
        vid = in.readString();
    }

    public static final Creator<GetVendorSubCategoryListModule> CREATOR = new Creator<GetVendorSubCategoryListModule>() {
        @Override
        public GetVendorSubCategoryListModule createFromParcel(Parcel in) {
            return new GetVendorSubCategoryListModule(in);
        }

        @Override
        public GetVendorSubCategoryListModule[] newArray(int size) {
            return new GetVendorSubCategoryListModule[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(vid);
    }
}

