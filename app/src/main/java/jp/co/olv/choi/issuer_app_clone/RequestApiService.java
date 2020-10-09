package jp.co.olv.choi.issuer_app_clone;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RequestApiService {

    @GET("comments?")
    Call<List<commentsResponse>> getCommentsByPostId(@Query("postId") Integer postId);
}