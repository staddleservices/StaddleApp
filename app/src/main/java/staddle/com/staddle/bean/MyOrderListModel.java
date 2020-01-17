package staddle.com.staddle.bean;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class MyOrderListModel implements Parcelable {
    private String id;
    private String uid;
    private String vid;
    private String order_list;
    private String order_price;
    private String discount;
    private String discount_price;
    private String commission;
    private String status;
    private String payment;
    private String create_date;
    private String rating;
    private String vName;
    private String booked_date;
    private String booking_slot;
    private String order_otp;

    private ArrayList<Data> data;

    protected MyOrderListModel(Parcel in) {
        id = in.readString();
        uid = in.readString();
        vid = in.readString();
        order_list = in.readString();
        order_price = in.readString();
        discount = in.readString();
        discount_price = in.readString();
        commission = in.readString();
        status = in.readString();
        payment = in.readString();
        create_date = in.readString();
        rating = in.readString();
        vName = in.readString();
        booked_date = in.readString();
        booking_slot = in.readString();
        order_otp = in.readString();
    }


    public static final Creator<MyOrderListModel> CREATOR = new Creator<MyOrderListModel>() {
        @Override
        public MyOrderListModel createFromParcel(Parcel in) {
            return new MyOrderListModel(in);
        }

        @Override
        public MyOrderListModel[] newArray(int size) {
            return new MyOrderListModel[size];
        }
    };

    public String getOrder_otp() {
        return order_otp;
    }

    public void setOrder_otp(String order_otp) {
        this.order_otp = order_otp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBooked_date() {
        return booked_date;
    }

    public void setBooked_date(String booked_date) {
        this.booked_date = booked_date;
    }

    public String getBooking_slot() {
        return booking_slot;
    }

    public void setBooking_slot(String booking_slot) {
        this.booking_slot = booking_slot;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getOrder_list() {
        return order_list;
    }

    public void setOrder_list(String order_list) {
        this.order_list = order_list;
    }

    public String getOrder_price() {
        return order_price;
    }

    public void setOrder_price(String order_price) {
        this.order_price = order_price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(String discount_price) {
        this.discount_price = discount_price;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getvName() {
        return vName;
    }

    public void setvName(String vName) {
        this.vName = vName;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(uid);
        dest.writeString(vid);
        dest.writeString(order_list);
        dest.writeString(order_price);
        dest.writeString(discount);
        dest.writeString(discount_price);
        dest.writeString(commission);
        dest.writeString(status);
        dest.writeString(payment);
        dest.writeString(create_date);
        dest.writeString(rating);
        dest.writeString(vName);
        dest.writeString(booked_date);
        dest.writeString(booking_slot);
    }

    public static class Data implements Parcelable {
        private String count;
        private String id;
        private String idd;
        private String menu_name;
        private String menu_Oprice;
        private String menu_price;
        private String vid;

        protected Data(Parcel in) {
            count = in.readString();
            id = in.readString();
            idd = in.readString();
            menu_name = in.readString();
            menu_Oprice = in.readString();
            menu_price = in.readString();
            vid = in.readString();
        }

        public static final Creator<Data> CREATOR = new Creator<Data>() {
            @Override
            public Data createFromParcel(Parcel in) {
                return new Data(in);
            }

            @Override
            public Data[] newArray(int size) {
                return new Data[size];
            }
        };

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIdd() {
            return idd;
        }

        public void setIdd(String idd) {
            this.idd = idd;
        }

        public String getMenu_name() {
            return menu_name;
        }

        public void setMenu_name(String menu_name) {
            this.menu_name = menu_name;
        }

        public String getMenu_Oprice() {
            return menu_Oprice;
        }

        public void setMenu_Oprice(String menu_Oprice) {
            this.menu_Oprice = menu_Oprice;
        }

        public String getMenu_price() {
            return menu_price;
        }

        public void setMenu_price(String menu_price) {
            this.menu_price = menu_price;
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
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(count);
            dest.writeString(id);
            dest.writeString(idd);
            dest.writeString(menu_name);
            dest.writeString(menu_Oprice);
            dest.writeString(menu_price);
            dest.writeString(vid);
        }
    }

}