package staddle.com.staddle.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class MenuListModule implements Parcelable {

    private String id;
    private String name;
    private String price;
    private int count;


    protected MenuListModule(Parcel in) {
        id = in.readString();
        name = in.readString();
        price = in.readString();
        count = Integer.parseInt(in.readString());
    }

    public static final Creator<MenuListModule> CREATOR = new Creator<MenuListModule>() {
        @Override
        public MenuListModule createFromParcel(Parcel in) {
            return new MenuListModule(in);
        }

        @Override
        public MenuListModule[] newArray(int size) {
            return new MenuListModule[size];
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(price);
        parcel.writeString(String.valueOf(count));
    }
}
