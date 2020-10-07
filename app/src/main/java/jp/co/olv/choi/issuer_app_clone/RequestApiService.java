package jp.co.olv.choi.issuer_app_clone;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RequestApiService {

    @GET("api/search?zipcode=0050004")
    Call<ZipcodeResponse> zipcode();
}
