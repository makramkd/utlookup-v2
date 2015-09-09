package org.lag.courselookup;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by admin on 8/30/15.
 */
public class ApplicationController extends Application {

    public static final String TAG = ApplicationController.class.getSimpleName();

    /**
     * The volley request queue we will use to make async HTTP requests.
     */
    RequestQueue requestQueue;

    private static ApplicationController instance;

    public void onCreate() {
        super.onCreate();

        instance = this;
        requestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    /**
     * Get an instance of this ApplicationController class.
     * @return an instance of this ApplicationController class.
     */
    public static synchronized ApplicationController getInstance() {
        return instance;
    }

    /**
     * Queue a request with the given tag.
     * @param request the request to queue
     * @param tag the tag to attach to the request (useful for cancellation)
     * @param <T> the type of the request.
     */
    public <T> void queueRequest(Request<T> request, String tag) {
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        requestQueue.add(request);
    }

    /**
     * Queue a request in Volley's request queue.
     * @param request the request to queue
     * @param <T> the type of the request (could be a subclass of Volley's Request)
     */
    public <T> void queueRequest(Request<T> request) {
        request.setTag(TAG);
        requestQueue.add(request);
    }

    /**
     * Cancel all pending requests given to Volley with the given tag.
     * @param tag the tag of the requests to be cancelled.
     */
    public void cancelPendingRequests(Object tag) {
        requestQueue.cancelAll(tag);
    }
}
