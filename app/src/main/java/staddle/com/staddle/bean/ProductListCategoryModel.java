package staddle.com.staddle.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductListCategoryModel implements Parcelable {
    private String id;
    private String cid;
    private String product_name;
    private String sid;
    private String current_price;
    private String offer_price;
    private String product_details;
    private String offer_start_date;
    private String offer_end_date;
    private String qr_image;
    private String create_date;

    private String vid;
    private String vfname;
    private String vlname;
    private String vemail;
    private String vmobile;
    private String vlocation;
    private String vimage;
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String image5;
    private String rating;

    //image change
    private boolean isSelectedAccepted=false;

    private boolean isSelectedRejected=false;

    public boolean isSelectedAccepted() {
        return isSelectedAccepted;
    }

    public void setSelectedAccepted(boolean isSelectedAccept) {
        isSelectedAccepted = isSelectedAccept;
    }

    public boolean isSelectedRejected() {
        return isSelectedRejected;
    }

    public void setSelectedRejected(boolean isSelectedReject) {
        isSelectedRejected = isSelectedReject;
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

    public String getProduct_details() {
        return product_details;
    }

    public void setProduct_details(String product_details) {
        this.product_details = product_details;
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

    public String getQr_image() {
        return qr_image;
    }

    public void setQr_image(String qr_image) {
        this.qr_image = qr_image;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public static Creator<ProductListCategoryModel> getCREATOR() {
        return CREATOR;
    }

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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    protected ProductListCategoryModel(Parcel in) {
        id = in.readString();
        cid = in.readString();
        product_name = in.readString();
        sid = in.readString();
        current_price = in.readString();
        offer_price = in.readString();
        product_details = in.readString();
        offer_start_date = in.readString();
        offer_end_date = in.readString();
        qr_image = in.readString();
        create_date = in.readString();
        image1 = in.readString();
        image2 = in.readString();
        image3 = in.readString();
        image4 = in.readString();
        image5 = in.readString();
        rating = in.readString();
    }

    public static final Creator<ProductListCategoryModel> CREATOR = new Creator<ProductListCategoryModel>() {
        @Override
        public ProductListCategoryModel createFromParcel(Parcel in) {
            return new ProductListCategoryModel(in);
        }

        @Override
        public ProductListCategoryModel[] newArray(int size) {
            return new ProductListCategoryModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(cid);
        parcel.writeString(product_name);
        parcel.writeString(sid);
        parcel.writeString(current_price);
        parcel.writeString(offer_price);
        parcel.writeString(product_details);
        parcel.writeString(offer_start_date);
        parcel.writeString(offer_end_date);
        parcel.writeString(qr_image);
        parcel.writeString(create_date);
        parcel.writeString(image1);
        parcel.writeString(image2);
        parcel.writeString(image3);
        parcel.writeString(image4);
        parcel.writeString(image5);
        parcel.writeString(rating);
    }
}
