package jp.co.olv.choi.issuer_app_clone;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    @SneakyThrows
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Realm初期化
        Realm.init(this);
        setContentView(jp.co.olv.choi.issuer_app_clone.R.layout.activity_main);

        ZipcodeResponse response = (ZipcodeResponse) new RestApiTask().execute().get();

        final String shopName = response.results.get(0).address1;
        final String amount = response.status.toString();
        final String payDate = response.results.get(0).zipcode;
        final String payCount = response.results.get(0).prefcode;

        // カラムの型を変えたのでマイグレーションする
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(2)
                .migration(new MyMigration())
                .build();
        final Realm realm = Realm.getInstance(config);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // 一旦データをリセットしてから入れる
                realm.deleteAll();
                realm.copyToRealmOrUpdate(new PayDetail(1, shopName, amount, payDate, payCount));
            }
        });

        ArrayList detailList = new ArrayList<Map<String, String>>();
        RealmResults<PayDetail> payDetails = realm.where(PayDetail.class).findAll();
        for (int i = 0; i < payDetails.size(); i++) {
            PayDetail payDetail = payDetails.get(i);
            Map<String, String> item = new HashMap<String, String>();
            item.put("FirstLine", payDetail.getShopName() + "                                                        " + payDetail.getAmount());
            item.put("SecondLine", payDetail.getPayDate() + "                                                 " + payDetail.getPayCount());
            detailList.add(item);
        }

//        Realm使用
//        final Integer[] ids = {1, 2, 3, 4, 5};
//        final String[] shopNames = {
//                "AMAZON.CO.JP",
//                "AMAZON.CO.JP",
//                "AMAZON.CO.JP",
//                "AMAZON.CO.JP",
//                "AMAZON.CO.JP"};
//        final String[] amounts = {
//                "90,000",
//                "9,625",
//                "1,008",
//                "6,346",
//                "10,525"};
//        final Date[] payDates = {
//                new Date(119, 10, 10),
//                new Date(119, 10, 13),
//                new Date(119, 10, 14),
//                new Date(119, 10, 14),
//                new Date(119, 10, 14)};
//        final Integer[] payCounts = {1, 1, 1, 1, 1};
//
//        final Realm realm = Realm.getDefaultInstance();
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                for (Integer i = 0; i < ids.length; i++) {
//                    realm.copyToRealmOrUpdate(new PayDetail(i, shopNames[i], amounts[i], payDates[i], payCounts[i]));
//                }
//            }
//        });
//
//        ArrayList detailList = new ArrayList<Map<String, String>>();
//        RealmResults<PayDetail> payDetails = realm.where(PayDetail.class).findAll();
//        for (int i = 0; i < payDetails.size(); i++) {
//            PayDetail payDetail = payDetails.get(i);
//            Map<String, String> item = new HashMap<String, String>();
//            item.put("FirstLine", payDetail.getShopName() + "                                                        " + payDetail.getAmount());
//            item.put("SecondLine", payDetail.getPayDate() + "                                                 " + payDetail.getPayCount());
//            detailList.add(item);
//        }

//        直書き
//        String[] shopNameAndAmount = {
//                "AMAZON.CO.JP      90,000円",
//                "AMAZON.CO.JP       9,625円",
//                "AMAZON.CO.JP       1,008円",
//                "AMAZON.CO.JP       6,346円",
//                "AMAZON.CO.JP      10,525円"};
//        String[] payDateAndPayCount = {
//                "19.11.10                            1回払い",
//                "19.11.13                            1回払い",
//                "19.11.14                            1回払い",
//                "19.11.14                            1回払い",
//                "19.11.14                            1回払い"};
//
//        ArrayList detailList = new ArrayList<Map<String, String>>();
//        for (int i = 0; i < shopNameAndAmount.length; i++) {
//            Map<String, String> item = new HashMap<String, String>();
//            item.put("FirstLine", shopNameAndAmount[i]);
//            item.put("SecondLine", payDateAndPayCount[i]);
//            detailList.add(item);
//        }

        SimpleAdapter detailAdapter = new SimpleAdapter(this, detailList,
                android.R.layout.simple_list_item_2,
                new String[]{"FirstLine", "SecondLine"},
                new int[]{android.R.id.text1, android.R.id.text2});

        ListView listView = (ListView) findViewById(R.id.detail_list);
        listView.setAdapter(detailAdapter);

        // Realm終了
        realm.close();
    }
}

class RestApiTask extends AsyncTask {

    @Override
    protected ZipcodeResponse doInBackground(Object[] objects){
        RequestApiService apiService = HttpClient.getRequestApiService();

        apiService.zipcode().enqueue(new Callback<ZipcodeResponse>() {
            @Override
            public void onResponse(Call<ZipcodeResponse> call, Response<ZipcodeResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("succeeded", response.toString());
                } else {
                    Log.e("failed", response.toString());
                }
            }

            @Override
            public void onFailure(Call<ZipcodeResponse> call, Throwable t) {
            }
        });

        try {
            return apiService.zipcode().execute().body();
        } catch (IOException e) {
        }
        return null;
    }
}

class MyMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

        RealmSchema schema = realm.getSchema();

        if (oldVersion == 0) {
            schema.get("PayDetail")
                    .removeField("payDate")
                    .removeField("payCount")
                    .addField("payDate", String.class)
                    .addField("payCount", String.class);
            oldVersion++;
        }
    }
}
