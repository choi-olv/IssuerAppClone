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
import io.realm.RealmResults;
import lombok.SneakyThrows;

public class MainActivity extends AppCompatActivity {

    public static final String DETAIL_LIST_POSITION = "jp.co.olv.choi.issuer_app_clone.DETAIL_LIST_POSITION";

    @BindView(R.id.detail_list)
    ListView detailListView;

    @SneakyThrows
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        MyApplication myApp = (MyApplication) this.getApplication();
        Realm realm = myApp.getRealm();

        final List<commentsResponse> responses = (List<commentsResponse>) new RestApiTask().execute().get();

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

        RealmResults<PayDetail> payDetailsFromRealm = realm.where(PayDetail.class).findAll();
        myApp.setPayDetails(payDetailsFromRealm);

        DetailListAdapter detailAdapter = new DetailListAdapter();
        RealmResults<PayDetail> payDetailsFromApp =  myApp.getPayDetails();
        for (int i = 0; i < payDetailsFromApp.size(); i++) {
            PayDetail payDetail = payDetailsFromApp.get(i);
            detailAdapter.addItem(payDetail);
        }

        detailListView.setAdapter(detailAdapter);
    }

    @OnItemClick(R.id.detail_list) void clickDetailList(int position){
        Intent intent = new Intent(getApplication(), DetailActivity.class);
        intent.putExtra(DETAIL_LIST_POSITION, position);
        startActivity(intent);
    }
}