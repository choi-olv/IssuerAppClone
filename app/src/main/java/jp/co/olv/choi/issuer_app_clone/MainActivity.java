package jp.co.olv.choi.issuer_app_clone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import lombok.SneakyThrows;

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

    @OnItemClick(R.id.detail_list) void clickDetailList(){
        Intent intent = new Intent(getApplication(), DetailActivity.class);
        startActivity(intent);
    }
}