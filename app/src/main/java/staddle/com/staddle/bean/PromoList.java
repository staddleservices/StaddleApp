package staddle.com.staddle.bean;

public class PromoList {
    String promoname;
    String promovalue;
    String mprice;
    String description;

    public PromoList(String promoname, String promovalue, String mprice, String description) {
        this.promoname = promoname;
        this.promovalue = promovalue;
        this.mprice = mprice;
        this.description = description;
    }

    public String getPromoname() {
        return promoname;
    }

    public void setPromoname(String promoname) {
        this.promoname = promoname;
    }

    public String getPromovalue() {
        return promovalue;
    }

    public void setPromovalue(String promovalue) {
        this.promovalue = promovalue;
    }

    public String getMprice() {
        return mprice;
    }

    public void setMprice(String mprice) {
        this.mprice = mprice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
