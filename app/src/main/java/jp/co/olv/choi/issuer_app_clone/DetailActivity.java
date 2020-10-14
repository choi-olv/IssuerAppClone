package jp.co.olv.choi.issuer_app_clone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import lombok.SneakyThrows;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.detail_item_shop_name)
    TextView detailItemShopNameView;

    @BindView(R.id.detail_item_amount)
    TextView detailItemAmountView;

    @BindView(R.id.detail_item_pay_date)
    TextView detailItemPayDateView;

    @BindView(R.id.detail_item_pay_count)
    TextView detailItemPayCountView;

    @SneakyThrows
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_detail);
        ButterKnife.bind(this);
        MyApplication myApp = (MyApplication) this.getApplication();
        Realm realm = myApp.getRealm();

        Intent intent = getIntent();
        int position = intent.getIntExtra(MainActivity.DETAIL_LIST_POSITION, 1);

        RealmResults<PayDetail> payDetails = realm.where(PayDetail.class).findAll();
        PayDetail payDetail = payDetails.get(position);
        detailItemShopNameView.setText(payDetail.getShopName());
        detailItemAmountView.setText(payDetail.getAmount() + "円");
        detailItemPayDateView.setText(payDetail.getPayDate());
        detailItemPayCountView.setText(payDetail.getPayCount() + "回払い");
    }

    @OnClick(R.id.top_menu_button4) void clickReturnToList(){
        Intent intent = new Intent(getApplication(), MainActivity.class);
        startActivity(intent);
    }
}
