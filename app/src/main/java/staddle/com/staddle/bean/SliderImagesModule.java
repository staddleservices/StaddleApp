package staddle.com.staddle.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class SliderImagesModule implements Parcelable {
    private String image;
    private String cid;

    protected SliderImagesModule(Parcel in) {
        image = in.readString();
        cid = in.readString();
    }

    public static final Creator<SliderImagesModule> CREATOR = new Creator<SliderImagesModule>() {
        @Override
        public SliderImagesModule createFromParcel(Parcel in) {
            return new SliderImagesModule(in);
        }

        @Override
        public SliderImagesModule[] newArray(int size) {
            return new SliderImagesModule[size];
        }
    };

    public String getImage() {
        return image;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(image);
        parcel.writeString(cid);
    }
}
