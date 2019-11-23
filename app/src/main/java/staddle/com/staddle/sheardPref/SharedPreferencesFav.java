package staddle.com.staddle.sheardPref;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;

import staddle.com.staddle.bean.ProductListCategoryModel;

public class SharedPreferencesFav {

    public static final String PREFS_NAME = "PRODUCT_APP";
    public static final String FAVORITES = "Product_Favorite";

    public SharedPreferencesFav() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveFavorites(Context context, ArrayList<ProductListCategoryModel> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.commit();
    }

    public void addFavorite(Context context, ProductListCategoryModel product) {
        ArrayList<ProductListCategoryModel> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<ProductListCategoryModel>();
        favorites.add(product);
        saveFavorites(context, favorites);
    }

    public void removeFavorite(Context context, ProductListCategoryModel product) {
        ArrayList<ProductListCategoryModel> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(product);
            saveFavorites(context, favorites);
        }
    }

    public ArrayList<ProductListCategoryModel> getFavorites(Context context) {
        SharedPreferences settings;
        ArrayList<ProductListCategoryModel> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            ProductListCategoryModel[] favoriteItems = gson.fromJson(jsonFavorites,
                    ProductListCategoryModel[].class);

          // favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<ProductListCategoryModel>();
        } else
            return null;

        return (ArrayList<ProductListCategoryModel>) favorites;
    }
}