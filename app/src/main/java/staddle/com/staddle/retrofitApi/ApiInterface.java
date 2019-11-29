package staddle.com.staddle.retrofitApi;

import com.google.gson.JsonElement;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import staddle.com.staddle.ResponseClasses.AddFavouriteResponse;
import staddle.com.staddle.ResponseClasses.CategorySubCategoryListResponse;
import staddle.com.staddle.ResponseClasses.CheckFavoriteResponse;
import staddle.com.staddle.ResponseClasses.EditProfileResponse;
import staddle.com.staddle.ResponseClasses.FavouriteListResponse;
import staddle.com.staddle.ResponseClasses.FilterSubCategoryProductListResponse;
import staddle.com.staddle.ResponseClasses.ForgotPasswordResponse;
import staddle.com.staddle.ResponseClasses.GetVendorSubCategoryListResponse;
import staddle.com.staddle.ResponseClasses.GetVendorSubCategoryMenuListResponse;
import staddle.com.staddle.ResponseClasses.LoginResponse;
import staddle.com.staddle.ResponseClasses.MediaLoginResponse;
import staddle.com.staddle.ResponseClasses.MenuListResponse;
import staddle.com.staddle.ResponseClasses.MyHelpListResponse;
import staddle.com.staddle.ResponseClasses.MyOrderListResponse;
import staddle.com.staddle.ResponseClasses.OffersAndPromoResponses;
import staddle.com.staddle.ResponseClasses.ProductListCategoryResponse;
import staddle.com.staddle.ResponseClasses.SignUpResponse;
import staddle.com.staddle.ResponseClasses.SliderImagesResponse;
import staddle.com.staddle.ResponseClasses.SubcategoryTreeListResponse;
import staddle.com.staddle.ResponseClasses.UploadImageResponse;
import staddle.com.staddle.ResponseClasses.UserInfoResponse;
import staddle.com.staddle.ResponseClasses.VendorListResponse;
import staddle.com.staddle.ResponseClasses.VendorOfferListResponse;
import staddle.com.staddle.paytm.module.GetCheckSum;
import staddle.com.staddle.paytm.module.VerifyCheckSum;

public interface ApiInterface {

    @GET(EndApi.USER_SIGN_UP_API)
    Call<SignUpResponse> UserSignUp(@Query("username") String username,
                                    @Query("email") String email,
                                    @Query("password") String password,
                                    @Query("mobile") String mobile);

    @POST(EndApi.USER_LOGIN_API)
    Call<LoginResponse> UserLogin(@Query("email") String email,
                                  @Query("password") String password,
                                  @Query("device_id") String device_id,
                                  @Query("device_type") String device_type);

    @GET(EndApi.CHANGE_PASSWORD)
    Call<EditProfileResponse> changePassword(
            @Query("uid") String uid,
            @Query("password") String password,
            @Query("newpassword") String newpassword);

    @POST(EndApi.UPLOAD_PICTURE)
    @FormUrlEncoded
    Call<UploadImageResponse> uploadPicture(@Field("image") String image);


    @GET(EndApi.FORGOT_PASSWORD)
    Call<ForgotPasswordResponse> forgotPassword(@Query("email") String email);

    @POST(EndApi.LOGIN_GOOGLE)
    Call<MediaLoginResponse> socialGoogleLogin(@Query("google_id") String google_id,
                                               @Query("username") String username,
                                               @Query("email") String email,
                                               @Query("mobile") String mobile,
                                               @Query("image") String image,
                                              @Query("device_id") String device_id,
                                               @Query("device_type") String device_type);


    @POST(EndApi.LOGIN_LINKED_IN)
    Call<MediaLoginResponse> socialLinkedInLogin(@Query("linkedin_id") String linkedin_id,
                                                 @Query("username") String username,
                                                 @Query("email") String email,
                                                 @Query("mobile") String mobile,
                                                 @Query("image") String image);

    @POST(EndApi.LOGIN_FACEBOOK)
    Call<MediaLoginResponse> socialFacebookLogin(@Query("facebook_id") String facebook_id,
                                                 @Query("username") String username,
                                                 @Query("email") String email,
                                                 @Query("mobile") String mobile,
                                                 @Query("image") String image,
                                                 @Query("device_id") String device_id,
                                                 @Query("device_type") String device_type);

    @POST(EndApi.EDIT_PROFILE)
    @FormUrlEncoded
    Call<EditProfileResponse> editProfile(@Query("uid") String uid,
                                          @Query("username") String username,
                                          @Query("mobile") String mobile,
                                          @Field("image") String image);

    @POST(EndApi.GET_USER_INFO)
    Call<UserInfoResponse> getUserInfo(@Query("uid") String uid);

    @GET(EndApi.GET_SLIDER_IMAGES)
    Call<SliderImagesResponse> getImagesList();

    @GET(EndApi.GET_CATEGORY_SUBCATEGORY_LIST)
    Call<CategorySubCategoryListResponse> getCategorySubCategoryList();

    @GET(EndApi.GET_VENDOR_LIST)
    Call<VendorListResponse> getVendorList(@Query("lat") String lat,
                                           @Query("lng") String lng);

    @GET(EndApi.GET_FILTER_LIST)
    Call<SubcategoryTreeListResponse> getSubCategoryTreeDetails(@Query("cid") String cid);

    // means offers
    @GET(EndApi.GET_PRODUCT_LIST_CATEGORY)
    Call<ProductListCategoryResponse> getProductListCategory(@Query("cid") String cid, @Query("sid") String sid);

    @GET(EndApi.GET_VENDER_SUBCATEGORY__MENU_LIST)
    Call<GetVendorSubCategoryMenuListResponse> getVenderSubCatgoryMenuList(@Query("vid") String vid);

    @GET(EndApi.ADD_FAVOURITE)
    Call<AddFavouriteResponse> addFavourite(@Query("uid") String uid,
                                            @Query("vid") String vid);

    @GET(EndApi.GET_FAVOURITE_LIST)
    Call<FavouriteListResponse> getFavouriteList(@Query("uid") String uid);

    @GET(EndApi.ADD_ORDER_DETAILS)
    Call<AddFavouriteResponse> addOfferSave(@Query("uid") String uid,
                                            @Query("vid") String vid,
                                            @Query("order_list") String order_list,
                                            @Query("order_price") String order_price,
                                            @Query("discount") String discount,
                                            @Query("promocode") String promodcode,
                                            @Query("promovalue") String promovalue,
                                            @Query("promodiscount") String promodiscount,
                                            @Query("discount_price") String discount_price,
                                            @Query("commission") String commission,
                                            @Query("total_price") String total_price,
                                            @Query("nickname") String nickname,
                                            @Query("completeaddress") String completeAddress,
                                            @Query("booking_slot") String booking_slot,
                                            @Query("booked_date") String booked_date,
                                            @Query("payment") String payment);

    @GET(EndApi.UPDATE_PRODUCT_STATUS_ACCEPT)
    Call<AddFavouriteResponse> updateProductSatusAccepted(@Query("pid") String pid);


    @GET(EndApi.UPDATE_PRODUCT_STATUS_REJECTED)
    Call<AddFavouriteResponse> updateProductSatusRejected(@Query("pid") String pid);

    @GET(EndApi.HELP)
    Call<AddFavouriteResponse> help(@Query("uid") String uid,
                                    @Query("comment") String comment);

    @GET(EndApi.GET_OFFER_PROMO_LIST)
    Call<OffersAndPromoResponses> getOfferAndPromoList();

    @GET(EndApi.GET_MY_ORDER_LIST_USER_NEW)
    Call<MyOrderListResponse> getMyOrderListUser(@Query("uid") String uid);

    @GET(EndApi.MAKE_RATING)
    Call<MyOrderListResponse> makeRating(@Query("oid") String oid,
                                         @Query("rate") String rate);

    @GET(EndApi.GET_HELP_LIST)
    Call<MyHelpListResponse> getMyHelpListUser(@Query("uid") String uid);


    @GET(EndApi.CHECK_FAVOURITE)
    Call<CheckFavoriteResponse> checkFav(@Query("uid") String uid,
                                         @Query("vid") String vid);


    // means offers
    @GET(EndApi.GET_PRODUCT_LIST)
    Call<ProductListCategoryResponse> getProductList(@Query("cid") String cid,
                                                     @Query("sid") String sid,
                                                     @Query("lat") String lat,
                                                     @Query("lng") String lng,
                                                     @Query("gender") String gender,
                                                     @Query("subcat") String subcat);

    // Paytm
    @FormUrlEncoded
    @POST("generateChecksum.php")
    Call<GetCheckSum> getChecksum(@Field("MID") String MID,
                                  @Field("ORDER_ID") String ORDER_ID,
                                  @Field("CUST_ID") String CUST_ID,
                                  @Field("MOBILE_NO") String MOBILE_NO,
                                  @Field("EMAIL") String EMAIL,
                                  @Field("CHANNEL_ID") String CHANNEL_ID,
                                  @Field("TXN_AMOUNT") String TXN_AMOUNT,
                                  @Field("WEBSITE") String WEBSITE,
                                  @Field("INDUSTRY_TYPE_ID") String INDUSTRY_TYPE_ID,
                                  @Field("CALLBACK_URL") String CALLBACK_URL);

    @FormUrlEncoded
    @POST("verifyChecksum.php")
    Call<VerifyCheckSum> verifyChecksum(@Field("CHECKSUMHASH") String CHECKSUMHASH);


    @POST("verifyChecksum.php")
    Call<JsonElement> verifyChecksum(@Body Map<String, String> paramMap);

}
