package vn.mn.quanlynhahang.model;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import vn.mn.quanlynhahang.fragment.TimeKeepingFragment;

public class HttpRequestSender extends AsyncTask<Void, Void, String> {
    @Override
    protected String doInBackground(Void... voids) {

        try {
            OkHttpClient client = new OkHttpClient();
            HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api-face-recognize.azurewebsites.net/api_recognize").newBuilder();
            urlBuilder.addQueryParameter("key1", TimeKeepingFragment.url1);
            urlBuilder.addQueryParameter("key2", TimeKeepingFragment.url2);
            urlBuilder.addQueryParameter("key3", "0.4");
            String url = urlBuilder.build().toString();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            Log.e("HttpRequestTask", "Error: " + e.getMessage());
            return null;
        }
    }
    private boolean parseResponse(String responseBody) {
        if (responseBody.equals("{\"result\":true}")){
            return true;
        }
        return false;
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s != null) {
            TimeKeepingFragment.isMatched.setValue(parseResponse(s));
            Log.d("HttpRequestTask", "Response body: " + s);
        } else {
            Log.e("HttpRequestTask", "Response is null");
        }
    }
}
