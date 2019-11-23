package staddle.com.staddle.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class FilterSubCategoryProductListModel implements Parcelable {
    private String id;
    private String cid;
    private String vid;
    private String vfname;
    private String vlname;
    private String vemail;
    private String vmobile;
    private String vlocation;
    private String vimage;
    private String product_name;
    private String sid;
    private String current_price;
    private String offer_price;
    private String image;
    private String product_details;
    private String create_date;
    private String qr_image;
    private String offer_start_date;
    private String offer_end_date;


    protected FilterSubCategoryProductListModel(Parcel in) {
        id = in.readString();
        cid = in.readString();
        vid = in.readString();
        vfname = in.readString();
        vlname = in.readString();
        vemail = in.readString();
        vmobile = in.readString();
        vlocation = in.readString();
        vimage = in.readString();
        product_name = in.readString();
        sid = in.readString();
        current_price = in.readString();
        offer_price = in.readString();
        image = in.readString();
        product_details = in.readString();
        create_date = in.readString();
        qr_image = in.readString();
        offer_start_date = in.readString();
        offer_end_date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(cid);
        dest.writeString(vid);
        dest.writeString(vfname);
        dest.writeString(vlname);
        dest.writeString(vemail);
        dest.writeString(vmobile);
        dest.writeString(vlocation);
        dest.writeString(vimage);
        dest.writeString(product_name);
        dest.writeString(sid);
        dest.writeString(current_price);
        dest.writeString(offer_price);
        dest.writeString(image);
        dest.writeString(product_details);
        dest.writeString(create_date);
        dest.writeString(qr_image);
        dest.writeString(offer_start_date);
        dest.writeString(offer_end_date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FilterSubCategoryProductListModel> CREATOR = new Creator<FilterSubCategoryProductListModel>() {
        @Override
        public FilterSubCategoryProductListModel createFromParcel(Parcel in) {
            return new FilterSubCategoryProductListModel(in);
        }

        @Override
        public FilterSubCategoryProductListModel[] newArray(int size) {
            return new FilterSubCategoryProductListModel[size];
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

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getVfname() {
        return vfname;
    }

    public void setVfname(String vfname) {
        this.vfname = vfname;
    }

    public String getVlname() {
        return vlname;
    }

    public void setVlname(String vlname) {
        this.vlname = vlname;
    }

    public String getVemail() {
        return vemail;
    }

    public void setVemail(String vemail) {
        this.vemail = vemail;
    }

    public String getVmobile() {
        return vmobile;
    }

    public void setVmobile(String vmobile) {
        this.vmobile = vmobile;
    }

    public String getVlocation() {
        return vlocation;
    }

    public void setVlocation(String vlocation) {
        this.vlocation = vlocation;
    }

    public String getVimage() {
        return vimage;
    }

    public void setVimage(String vimage) {
        this.vimage = vimage;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
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

    public String getQr_image() {
        return qr_image;
    }

    public void setQr_image(String qr_image) {
        this.qr_image = qr_image;
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
}
