package org.lag.utlookup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.toolbox.StringRequest;

import org.lag.utlookup.back.AsyncStGeorgeCrawler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ChooseCampusActivity extends Activity {

    AsyncStGeorgeCrawler crawler;
    List<StringRequest> instructorStringRequests;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_campus);

        progressDialog = new ProgressDialog(this);
        crawler = new AsyncStGeorgeCrawler(this);
        instructorStringRequests = new ArrayList<>();
    }

    public synchronized boolean progressDialogShowing() {
        return progressDialog.isShowing();
    }

    public void showProgress() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public void hideProgress() {
        if (progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

    public void getInstructorRequestsButtonClicked(View view) {
        // can only work when we get course urls
        if (crawler.courseUrls.isEmpty()) {
            return;
        }

        for (String courseUrl : crawler.courseUrls) {
            instructorStringRequests.add(
                    crawler.getInstructorListForDepartmentStringRequest(courseUrl)
            );
        }

        crawler.requestCount.set(instructorStringRequests.size());

        Log.d("ChooseCampusActivity", "Request count is : " + crawler.requestCount);
    }

    public void getCoursesButtonClicked(View view) {
        StringRequest courseRequest = crawler.getCourseUrlsStringRequest();
        ApplicationController.getInstance().queueRequest(courseRequest);
    }

    public void executeInstructorRequestsButtonClicked(View view) {
        // can only work if we have some requests
        if (instructorStringRequests.isEmpty()) {
            return;
        }

        showProgress();

        for (StringRequest request : instructorStringRequests) {
            ApplicationController.getInstance().queueRequest(request);
        }
    }
}
