package jp.co.olv.choi.issuer_app_clone;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class MyMigration implements RealmMigration {
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