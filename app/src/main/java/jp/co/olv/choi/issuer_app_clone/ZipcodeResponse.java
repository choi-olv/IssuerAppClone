package jp.co.olv.choi.issuer_app_clone;

public class ZipcodeResponse {
    public String status;
    public String message;
    public Results results;

    public static class Results {
        public String zipcode;
        public String prefcode;
        public String address1;
        public String address2;
        public String address3;
        public String kana1;
        public String kana2;
        public String kana3;
    }
}
