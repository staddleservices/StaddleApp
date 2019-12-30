package staddle.com.staddle.bean;

import android.content.Context;

public class UpdateHelper {
    public static String KEY_UPDATE_ENABLE="isUpdate";
    public static String KEY_UPDATE_VERSION="version";
    public static String KEY_UPDATE_URL="update_url";

    public interface OnUpdateCheckListener{
        void onUpdatecheckListener(String url);
    }
    public static Builder with(Context context){
        return new Builder(context);
    }
    private OnUpdateCheckListener onUpdateCheckListener;
    private Context ctx;

    public UpdateHelper(Context ctx,OnUpdateCheckListener onUpdateCheckListener) {
        this.onUpdateCheckListener = onUpdateCheckListener;
        this.ctx = ctx;
    }

    public static class Builder{

        private Context context;
        private OnUpdateCheckListener onUpdateCheckListener;
        public Builder(Context context){
            this.context=context;
        }


        public Builder onUpdateCheck(OnUpdateCheckListener onUpdateCheckListener){
            this.onUpdateCheckListener=onUpdateCheckListener;
            return this;
        }
    }
}
