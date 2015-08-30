package org.lag.utlookup;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.android.volley.toolbox.StringRequest;

import org.lag.utlookup.back.AsyncStGeorgeCrawler;

import java.util.ArrayList;
import java.util.List;

public class ChooseCampusActivity extends Activity {

    AsyncStGeorgeCrawler crawler;
    List<StringRequest> instructorStringRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_campus);

        crawler = new AsyncStGeorgeCrawler();
        instructorStringRequests = new ArrayList<>();
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

        for (StringRequest request : instructorStringRequests) {
            ApplicationController.getInstance().queueRequest(request);
        }
    }
}
