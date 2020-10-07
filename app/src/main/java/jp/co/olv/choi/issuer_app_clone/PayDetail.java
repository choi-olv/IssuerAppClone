package jp.co.olv.choi.issuer_app_clone;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayDetail extends RealmObject {
    @PrimaryKey
    private Integer id;
    private String shopName;
    private String amount;
    private Date payDate;
    private Integer payCount;

    public PayDetail() {
    }

    public PayDetail(Integer id, String shopName, String amount, Date payDate, Integer payCount) {
        this.id = id;
        this.shopName = shopName;
        this.amount = amount;
        this.payDate = payDate;
        this.payCount = payCount;
    }
}
