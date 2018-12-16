package com.my.boostcamppreassignment.data;

public enum XNaverClientInfo {
     ID("X-Naver-Client-Id", "dqXp4lLycNfqrcXov56T"),
     PWD("X-Naver-Client-Secret", "UtJ45UpPmj");

    XNaverClientInfo(String xNaverClientKey, String input) {
        this.xNaverClientKey = xNaverClientKey;
        this.xNaverClientValue = input;
    }

    String xNaverClientKey;
    String xNaverClientValue;

    public String getxNaverClientKey() {
        return xNaverClientKey;
    }
    public String getxNaverClientValue() {
        return xNaverClientValue;
    }

}
