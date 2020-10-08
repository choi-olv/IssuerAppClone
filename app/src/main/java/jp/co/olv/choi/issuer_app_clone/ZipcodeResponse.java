package jp.co.olv.choi.issuer_app_clone;

import java.util.List;

public class ZipcodeResponse {
    public String message;
    public List<Result> results;
    public Integer status;

    public static class Result {
        public String address1;
        public String address2;
        public String address3;
        public String kana1;
        public String kana2;
        public String kana3;
        public String prefcode;
        public String zipcode;
    }
}