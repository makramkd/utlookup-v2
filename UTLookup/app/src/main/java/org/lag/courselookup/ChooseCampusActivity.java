package org.lag.courselookup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChooseCampusActivity extends Activity {

    /**
     * The URL to the database file we will use.
     */
    private static final String DB_URL =
            "***";
    private String DL_DIR;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_campus);

        progressDialog = new ProgressDialog(this);
        DL_DIR = this.getFilesDir().getAbsolutePath() + "db/courseDatabase.db";
    }

    public void updateButtonClicked(View view) {
        new DownloadDatabase(this).execute(DB_URL);

        File directory = getFilesDir();
        String[] subFiles = directory.list();
        if (subFiles != null) {
            for (String file : subFiles) {
                Log.d("CCA", file);
            }
        }
    }

    class DownloadDatabase extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock wakeLock;

        public DownloadDatabase(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            wakeLock.acquire();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            wakeLock.release();
            progressDialog.dismiss();

            if (result != null) {
                Toast.makeText(context, "Download error: " + result, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Download succeeded", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            InputStream input = null;
            OutputStream output = null;
            try {
                Request request = new Request.Builder()
                        .url(DB_URL)
                        .build();
                Response response = ((ApplicationController) getApplication()).getClient()
                        .newCall(request).execute();

                input = response.body().byteStream();
                output = openFileOutput("courseDatabase.db", MODE_PRIVATE);

                byte[] data = new byte[4096];
                int count;
                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                Log.e("CCA", "Exception in doInBackground of DownloadDatabaseTask : "
                        + e.getMessage());
            } finally {
                try {
                    if (output != null) {
                        output.close();
                    }
                    if (input != null) {
                        input.close();
                    }
                } catch (IOException ioe) {
                    // ignore
                }
            }

            return null;
        }
    }
}
