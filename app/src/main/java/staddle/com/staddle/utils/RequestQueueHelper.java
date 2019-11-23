package staddle.com.staddle.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestQueueHelper {

    @SuppressLint("StaticFieldLeak")
    private static RequestQueueHelper mInstance;
    private RequestQueue mRequestQueue;
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    private RequestQueueHelper(Context context) {
// Specify the application context
        mContext = context;
// Get the request queue
        mRequestQueue = getRequestQueue();
    }

    public static synchronized RequestQueueHelper getInstance(Context context) {
// If Instance is null then initialize new Instance
        if (mInstance == null) {
            mInstance = new RequestQueueHelper(context);
        }
// Return RequestQueueHelper new Instance
        return mInstance;
    }

    private RequestQueue getRequestQueue() {
// If RequestQueue is null the initialize new RequestQueue
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }

// Return RequestQueue
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
// Add the specified request to the request queue
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(request);
    }
}
