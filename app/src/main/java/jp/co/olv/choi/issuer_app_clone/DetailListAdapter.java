package jp.co.olv.choi.issuer_app_clone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailListAdapter extends BaseAdapter {

    @BindView(R.id.dedail_list_shop_name)
    TextView shopNameView;
    @BindView(R.id.dedail_list_amount)
    TextView amountView;
    @BindView(R.id.dedail_list_pay_date)
    TextView payDateView;
    @BindView(R.id.dedail_list_pay_count)
    TextView payCountView;

    private ArrayList<PayDetail> detailListItems = new ArrayList<PayDetail>() ;

    public DetailListAdapter() {
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.detail_list_item, parent, false);
        }

        ButterKnife.bind(this, convertView);
        PayDetail payDetail = detailListItems.get(position);

        shopNameView.setText(payDetail.getShopName());
        amountView.setText(payDetail.getAmount() + "円");
        payDateView.setText(payDetail.getPayDate());
        payCountView.setText(payDetail.getPayCount() + "回払い");

        return convertView;
    }

    @Override
    public int getCount() {
        return detailListItems.size() ;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return detailListItems.get(position) ;
    }

    public void addItem(PayDetail basePayDetail) {
        PayDetail payDetail = new PayDetail(basePayDetail.getShopName(), basePayDetail.getAmount(), basePayDetail.getPayDate(), basePayDetail.getPayCount());
        detailListItems.add(payDetail);
    }
}
