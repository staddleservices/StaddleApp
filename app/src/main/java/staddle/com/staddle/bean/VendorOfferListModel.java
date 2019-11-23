package staddle.com.staddle.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class VendorOfferListModel implements Parcelable {
    private String id;
    private String cid;
    private String offer_name;
    private String sid;
    private String current_price;
    private String offer_price;
    private String image;
    private String product_details;
    private String create_date;
    private String offer_start_date;
    private String offer_end_date;
    private String status;
    private String vid;

    protected VendorOfferListModel(Parcel in) {
        id = in.readString();
        cid = in.readString();
        offer_name = in.readString();
        sid = in.readString();
        current_price = in.readString();
        offer_price = in.readString();
        image = in.readString();
        product_details = in.readString();
        create_date = in.readString();
        offer_start_date = in.readString();
        offer_end_date = in.readString();
        status = in.readString();
        vid = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(cid);
        dest.writeString(offer_name);
        dest.writeString(sid);
        dest.writeString(current_price);
        dest.writeString(offer_price);
        dest.writeString(image);
        dest.writeString(product_details);
        dest.writeString(create_date);
        dest.writeString(offer_start_date);
        dest.writeString(offer_end_date);
        dest.writeString(status);
        dest.writeString(vid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VendorOfferListModel> CREATOR = new Creator<VendorOfferListModel>() {
        @Override
        public VendorOfferListModel createFromParcel(Parcel in) {
            return new VendorOfferListModel(in);
        }

        @Override
        public VendorOfferListModel[] newArray(int size) {
            return new VendorOfferListModel[size];
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

    public String getOffer_name() {
        return offer_name;
    }

    public void setOffer_name(String offer_name) {
        this.offer_name = offer_name;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getCurrent_price() {
        return current_price;
    }

    public void setCurrent_price(String current_price) {
        this.current_price = current_price;
    }

    public String getOffer_price() {
        return offer_price;
    }

    public void setOffer_price(String offer_price) {
        this.offer_price = offer_price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProduct_details() {
        return product_details;
    }

    public void setProduct_details(String product_details) {
        this.product_details = product_details;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getOffer_start_date() {
        return offer_start_date;
    }

    public void setOffer_start_date(String offer_start_date) {
        this.offer_start_date = offer_start_date;
    }

    public String getOffer_end_date() {
        return offer_end_date;
    }

    public void setOffer_end_date(String offer_end_date) {
        this.offer_end_date = offer_end_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }
}
