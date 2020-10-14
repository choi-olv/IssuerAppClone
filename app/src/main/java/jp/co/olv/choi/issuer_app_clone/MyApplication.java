package jp.co.olv.choi.issuer_app_clone;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import lombok.Getter;
import lombok.Setter;

public class MyApplication extends Application {

    @Getter
    @Setter
    private RealmResults<PayDetail> payDetails;

    @Getter
    @Setter
    private Realm realm;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

        // カラムの型を変えたのでマイグレーションする
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(4)
                .migration(new MyMigration())
                .build();
        realm = Realm.getInstance(config);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        realm.close();
    }
}