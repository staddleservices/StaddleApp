package staddle.com.staddle.bean;

public class CurrentOrderMetaData {

    String CRORDISCOUNT;
    String CRORTAG;
    String CRORCAT;
    String CROR_CID;
    String CROR_VID;
    String CROR_VNAME;
    String CROR_COMMISION;

    public CurrentOrderMetaData(String CRORDISCOUNT, String CRORTAG, String CRORCAT, String CROR_CID, String CROR_VID, String CROR_VNAME, String CROR_COMMISION) {
        this.CRORDISCOUNT = CRORDISCOUNT;
        this.CRORTAG = CRORTAG;
        this.CRORCAT = CRORCAT;
        this.CROR_CID = CROR_CID;
        this.CROR_VID = CROR_VID;
        this.CROR_VNAME = CROR_VNAME;
        this.CROR_COMMISION = CROR_COMMISION;
    }

    public String getCRORDISCOUNT() {
        return CRORDISCOUNT;
    }

    public void setCRORDISCOUNT(String CRORDISCOUNT) {
        this.CRORDISCOUNT = CRORDISCOUNT;
    }

    public String getCRORTAG() {
        return CRORTAG;
    }

    public void setCRORTAG(String CRORTAG) {
        this.CRORTAG = CRORTAG;
    }

    public String getCRORCAT() {
        return CRORCAT;
    }

    public void setCRORCAT(String CRORCAT) {
        this.CRORCAT = CRORCAT;
    }

    public String getCROR_CID() {
        return CROR_CID;
    }

    public void setCROR_CID(String CROR_CID) {
        this.CROR_CID = CROR_CID;
    }

    public String getCROR_VID() {
        return CROR_VID;
    }

    public void setCROR_VID(String CROR_VID) {
        this.CROR_VID = CROR_VID;
    }

    public String getCROR_VNAME() {
        return CROR_VNAME;
    }

    public void setCROR_VNAME(String CROR_VNAME) {
        this.CROR_VNAME = CROR_VNAME;
    }

    public String getCROR_COMMISION() {
        return CROR_COMMISION;
    }

    public void setCROR_COMMISION(String CROR_COMMISION) {
        this.CROR_COMMISION = CROR_COMMISION;
    }
}
