package staddle.com.staddle.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class GetVendorSubCategoryMenuListModule implements Parcelable {
    private String vender_sub_catgoey;
    private ArrayList<MenuList> menu;

    protected GetVendorSubCategoryMenuListModule(Parcel in) {
        vender_sub_catgoey = in.readString();
    }

    public static final Creator<GetVendorSubCategoryMenuListModule> CREATOR = new Creator<GetVendorSubCategoryMenuListModule>() {
        @Override
        public GetVendorSubCategoryMenuListModule createFromParcel(Parcel in) {
            return new GetVendorSubCategoryMenuListModule(in);
        }

        @Override
        public GetVendorSubCategoryMenuListModule[] newArray(int size) {
            return new GetVendorSubCategoryMenuListModule[size];
        }
    };

    public String getVender_sub_catgoey() {
        return vender_sub_catgoey;
    }

    public void setVender_sub_catgoey(String vender_sub_catgoey) {
        this.vender_sub_catgoey = vender_sub_catgoey;
    }

    public ArrayList<MenuList> getMenu() {
        return menu;
    }

    public void setMenu(ArrayList<MenuList> menu) {
        this.menu = menu;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(vender_sub_catgoey);
    }

    public static class MenuList {
        private String id;
        private String vid;
        private double menu_price;

        private String menu_name;
        private int count = 0;
        private  double totalPrice;

        public MenuList() {

        }

        public MenuList(String id, String vid, double menu_price, String menu_name, int count, double totalPrice) {
            this.id = id;
            this.vid = vid;
            this.menu_price = menu_price;
            this.menu_name = menu_name;
            this.count = count;
            this.totalPrice = totalPrice;

        }



        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getVid() {
            return vid;
        }

        public void setVid(String vid) {
            this.vid = vid;
        }

        public Double getMenu_price() {
            return menu_price;
        }

        public void setMenu_price(Double menu_price) {
            this.menu_price = menu_price;
        }

        public String getMenu_name() {
            return menu_name;
        }

        public void setMenu_name(String menu_name) {
            this.menu_name = menu_name;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public double getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(double totalPrice) {
            this.totalPrice = totalPrice;
        }
    }
}

