package staddle.com.staddle.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class CategorySubCategoryListModel implements Parcelable {

    private String Category;

    private ArrayList<SubCategoryModel> Sub_Category;

    protected CategorySubCategoryListModel(Parcel in) {
        Category = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Category);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CategorySubCategoryListModel> CREATOR = new Creator<CategorySubCategoryListModel>() {
        @Override
        public CategorySubCategoryListModel createFromParcel(Parcel in) {
            return new CategorySubCategoryListModel(in);
        }

        @Override
        public CategorySubCategoryListModel[] newArray(int size) {
            return new CategorySubCategoryListModel[size];
        }
    };

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    //=======================================
    public ArrayList<SubCategoryModel> getSub_Category() {
        return Sub_Category;
    }

    public void setSub_Category(ArrayList<SubCategoryModel> sub_Category) {
        Sub_Category = sub_Category;
    }


}
