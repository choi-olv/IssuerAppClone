package jp.co.olv.choi.issuer_app_clone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(jp.co.olv.choi.issuer_app_clone.R.layout.activity_main);

        String[] shopNameAndAmount = {
                "AMAZON.CO.JP      90,000円",
                "AMAZON.CO.JP       9,625円",
                "AMAZON.CO.JP       1,008円",
                "AMAZON.CO.JP       6,346円",
                "AMAZON.CO.JP      10,525円"};
        String[] payDateAndPayCount = {
                "19.11.10                            1回払い",
                "19.11.13                            1回払い",
                "19.11.14                            1回払い",
                "19.11.14                            1回払い",
                "19.11.14                            1回払い"};

        ArrayList detailList = new ArrayList<Map<String, String>>();
        for (int i = 0; i < shopNameAndAmount.length; i++) {
            Map<String, String> item = new HashMap<String, String>();
            item.put("FirstLine", shopNameAndAmount[i]);
            item.put("SecondLine", payDateAndPayCount[i]);
            detailList.add(item);
        }

        SimpleAdapter detailAdapter = new SimpleAdapter(this, detailList,
                android.R.layout.simple_list_item_2,
                new String[] { "FirstLine", "SecondLine" },
                new int[] { android.R.id.text1, android.R.id.text2});

        ListView listView = (ListView)findViewById(R.id.detail_list);
        listView.setAdapter(detailAdapter);
    }
}
