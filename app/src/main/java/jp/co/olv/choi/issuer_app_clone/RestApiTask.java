package jp.co.olv.choi.issuer_app_clone;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestApiTask extends AsyncTask {

    @Override
    protected List<commentsResponse> doInBackground(Object[] objects){
        RequestApiService apiService = HttpClient.getRequestApiService();
        Integer postId = 2;

        apiService.getCommentsByPostId(postId).enqueue(new Callback<List<commentsResponse>>() {
            @Override
            public void onResponse(Call<List<commentsResponse>> call, Response<List<commentsResponse>> response) {
                if (response.isSuccessful()) {
                    Log.d("succeeded", response.toString());
                } else {
                    Log.e("failed", response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<commentsResponse>> call, Throwable t) {
            }
        });

        try {
            return apiService.getCommentsByPostId(postId).execute().body();
        } catch (IOException e) {
        }
        return null;
    }
}