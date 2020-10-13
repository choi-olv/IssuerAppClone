package jp.co.olv.choi.issuer_app_clone;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmResults;
import io.realm.RealmSchema;
import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.detail_list)
    ListView listView;

    @SneakyThrows
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Realm初期化
        Realm.init(this);
        setContentView(jp.co.olv.choi.issuer_app_clone.R.layout.activity_main);
        ButterKnife.bind(this);

        final List<commentsResponse> responses = (List<commentsResponse>) new RestApiTask().execute().get();

        // カラムの型を変えたのでマイグレーションする
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(4)
                .migration(new MyMigration())
                .build();
        final Realm realm = Realm.getInstance(config);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // 一旦データをリセットしてから入れる
                realm.deleteAll();
                for (Integer i = 0; i < responses.size(); i++) {
                    commentsResponse response = responses.get(i);
                    realm.copyToRealmOrUpdate(new PayDetail(i, response.content.substring(0, 10), response.id, response.createdAt, response.PostId));
                }
            }
        });

        DetailListAdapter detailAdapter = new DetailListAdapter();

        RealmResults<PayDetail> payDetails = realm.where(PayDetail.class).findAll();
        for (int i = 0; i < payDetails.size(); i++) {
            PayDetail payDetail = payDetails.get(i);
            detailAdapter.addItem(payDetail);
        }

        listView.setAdapter(detailAdapter);

        // Realm終了
        realm.close();
    }
}

class RestApiTask extends AsyncTask {

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

class MyMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

        RealmSchema schema = realm.getSchema();

        if (oldVersion <= 3) {
            schema.get("PayDetail")
                    .removeField("amount")
                    .removeField("payDate")
                    .removeField("payCount")
                    .addField("amount", String.class)
                    .addField("payDate", String.class)
                    .addField("payCount", String.class);
            oldVersion++;
        }
    }
}
