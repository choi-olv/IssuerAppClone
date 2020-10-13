package jp.co.olv.choi.issuer_app_clone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import lombok.SneakyThrows;

public class DetailActivity extends AppCompatActivity {

    @SneakyThrows
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_detail);
        ButterKnife.bind(this);
    }
}
