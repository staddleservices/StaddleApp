package staddle.com.staddle.bean;

public class SavedAddressList {

    String nickName;
    String addressString;

    public SavedAddressList(String nickName, String addressString) {
        this.nickName = nickName;
        this.addressString = addressString;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAddressString() {
        return addressString;
    }

    public void setAddressString(String addressString) {
        this.addressString = addressString;
    }
}
